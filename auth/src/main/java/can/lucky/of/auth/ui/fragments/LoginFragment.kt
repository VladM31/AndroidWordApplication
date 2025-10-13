package can.lucky.of.auth.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.auth.R
import can.lucky.of.auth.databinding.FragmentLoginBinding
import can.lucky.of.auth.domain.actions.LoginAction
import can.lucky.of.auth.domain.vms.LoginViewModel
import can.lucky.of.auth.ui.navigations.AuthNavigator
import can.lucky.of.auth.ui.navigations.TelegramLoginNavigator
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.ui.utils.NumberInputFilter
import can.lucky.of.core.ui.utils.setColumnCountByOrientation
import can.lucky.of.core.utils.addDebounceAfterTextChangedListener
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var binding: FragmentLoginBinding? = null
    private val viewModel by viewModel<LoginViewModel>()
    private val authNavigator: AuthNavigator by inject()
    private val telegramNavigator: TelegramLoginNavigator by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.state.value.isNotExpired) {
            authNavigator.navigateFromLogin(findNavController())
            return
        }

        val newBinding = FragmentLoginBinding.bind(view)
        binding = newBinding

        newBinding.menuGridLayout.setColumnCountByOrientation(1, 2)



        initStateListener()
        initInputs(newBinding)
        initOnClickListeners(newBinding)
        initOrientationView()

        lifecycleScope.launch {
            viewModel.state.map { it.isAvailableBiometric }.distinctUntilChanged().collectLatest {
                newBinding.logInBiometric.visibility = if (it) View.VISIBLE else View.GONE
                newBinding.logInBiometric.isEnabled = it
                newBinding.logInBiometric.isClickable = it
            }
        }
    }

    private fun initOrientationView() {
        val orientation = resources.configuration.orientation

        val params = binding?.menuGridLayout?.layoutParams ?: return

        params.height = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private fun initOnClickListeners(newBinding: FragmentLoginBinding) {
        newBinding.sighUp.setOnClickListener {
            authNavigator.navigateToSignUp(findNavController())
        }

        newBinding.button.setOnClickListener {
            newBinding.button.isClickable = false
            viewModel.sent(LoginAction.Submit)
            lifecycleScope.launch {
                delay(1000)
                newBinding.button.isClickable = true
            }
        }

        newBinding.logInWithTelegram.setOnClickListener {
            telegramNavigator.navigateToTelegramLogin(this)
        }

        newBinding.logInBiometric.setOnClickListener {
            showBiometricPrompt()
        }
    }

    private fun initStateListener() {
        viewModel.state.onEnd(lifecycleScope) {
            authNavigator.navigateFromLogin(findNavController())
        }

        viewModel.state.onError(lifecycleScope) {
            requireActivity().showError(it.message).apply {
                setTitle("Log in errors")
                show()
            }
        }

    }

    private fun initInputs(newBinding: FragmentLoginBinding) {
        newBinding.inputPassword.setText(viewModel.state.value.password)
        newBinding.inputPassword.addDebounceAfterTextChangedListener(200) {
            viewModel.sent(LoginAction.SetPassword(it))
        }

        newBinding.inputPhone.setText(viewModel.state.value.phoneNumber)
        newBinding.inputPhone.filters = arrayOf(NumberInputFilter())
        newBinding.inputPhone.addDebounceAfterTextChangedListener(200) {
            viewModel.sent(LoginAction.SetPhoneNumber(it))
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    viewModel.sent(LoginAction.BiometricAuthFailed)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.sent(LoginAction.BiometricAuthSuccess)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    viewModel.sent(LoginAction.BiometricAuthFailed)
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Authenticate with password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}