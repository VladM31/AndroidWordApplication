package can.lucky.of.auth.ui.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.auth.R
import can.lucky.of.auth.databinding.FragmentSignUpBinding
import can.lucky.of.auth.domain.actions.SighUpAction
import can.lucky.of.auth.domain.models.data.LogInBundle
import can.lucky.of.auth.domain.vms.SignUpViewModel
import can.lucky.of.auth.ui.navigations.AuthNavigator
import can.lucky.of.core.domain.models.enums.Currency
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.setContent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("UseCompatLoadingForDrawables")
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private var binding: FragmentSignUpBinding? = null
    private val vm by viewModel<SignUpViewModel>()
    private val authNavigator: AuthNavigator by inject()
    private val disableDraw by lazy {
        requireContext().getDrawable(can.lucky.of.core.R.drawable.disable_back)
    }
    private val enableDraw by lazy {
        requireContext().getDrawable(can.lucky.of.core.R.drawable.button_back)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)

        initOrientation()
        setValues()

        setAgreedListener()

        setListeners()
        setToolController()

        setErrorListener()
        setSuccessListener()
    }

    private fun setAgreedListener() {
        binding?.agreeCheckboxMaterial?.setOnCheckedChangeListener { _, isChecked ->
            vm.sent(SighUpAction.SetAgreed(isChecked))
        }

        lifecycleScope.launch {
            vm.state.map { it.agreed }
                .distinctUntilChanged()
                .collectLatest {
                    binding?.signUpSubmit?.isEnabled = it
                    binding?.signUpSubmit?.background = if (it) {
                        enableDraw
                    } else {
                        disableDraw
                    }
                }
        }
    }

    private fun setErrorListener() {
        lifecycleScope.launch {
            vm.state.map { it.error }.distinctUntilChanged()
                .drop(1)
                .filterNotNull()
                .shareIn(lifecycleScope, SharingStarted.WhileSubscribed(), 0)
                .collectLatest {
                    requireContext().showError(it.message).show()
                    binding?.signUpSubmit?.isEnabled = true
                }
        }
    }

    private fun setSuccessListener() {
        lifecycleScope.launch {
            vm.state.map { it.success }
                .filter { it }
                .take(1)
                .collectLatest {
                    val bundle = LogInBundle(
                        phoneNumber = vm.state.value.phoneNumber,
                        password = vm.state.value.password
                    )

                    authNavigator.navigateToConfirmation(findNavController(), bundle)
                }
        }
    }

    private fun initOrientation() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding?.menuGridLayout?.columnCount = 2
        } else {
            binding?.menuGridLayout?.columnCount = 1
        }
    }

    private fun setListeners() {
        binding?.inputPhoneNumber?.addTextChangedListener {
            vm.sent(SighUpAction.SetPhoneNumber(it.toString()))
        }

        binding?.inputPassword?.addTextChangedListener {
            vm.sent(SighUpAction.SetPassword(it.toString()))
        }

        binding?.inputFirstName?.addTextChangedListener {
            vm.sent(SighUpAction.SetFirstName(it.toString()))
        }

        binding?.inputLastName?.addTextChangedListener {
            vm.sent(SighUpAction.SetLastName(it.toString()))
        }

        binding?.inputEmail?.addTextChangedListener {
            vm.sent(SighUpAction.SetEmail(it.toString()))
        }

        binding?.spinnerCurrency?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    vm.sent(SighUpAction.SetCurrency(Currency.entries[position]))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding?.signUpSubmit?.setOnClickListener {
            binding?.signUpSubmit?.isEnabled = false
            vm.sent(SighUpAction.Submit)
        }
    }

    private fun setValues() {
        binding?.inputPhoneNumber?.setText(vm.state.value.phoneNumber)
        binding?.inputPassword?.setText(vm.state.value.password)
        binding?.inputFirstName?.setText(vm.state.value.firstName)
        binding?.inputLastName?.setText(vm.state.value.lastName)
        binding?.inputEmail?.setText(vm.state.value.email)

        binding?.spinnerCurrency?.setContent(Currency.entries.toTypedArray())
        binding?.spinnerCurrency?.setSelection(Currency.entries.indexOf(vm.state.value.currency))
    }

    private fun setToolController() {
        val toolBar = binding?.toolBar ?: return

        ToolBarController(
            navController = findNavController(),
            binding = toolBar,
            title = "Sign Up"
        ).setDefaultSettings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}