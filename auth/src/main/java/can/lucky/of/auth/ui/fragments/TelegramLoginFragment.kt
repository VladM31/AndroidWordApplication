package can.lucky.of.auth.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.auth.R
import can.lucky.of.auth.databinding.FragmentTelegramLoginBinding
import can.lucky.of.auth.domain.actions.TelegramLoginAction
import can.lucky.of.auth.domain.vms.TelegramLoginVm
import can.lucky.of.auth.ui.navigations.TelegramLoginNavigator
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.addDebounceAfterTextChangedListener
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TelegramLoginFragment : Fragment(R.layout.fragment_telegram_login) {
    private var binding: FragmentTelegramLoginBinding? = null
    private val vm by viewModel<TelegramLoginVm>()
    private val navigator by inject<TelegramLoginNavigator>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTelegramLoginBinding.bind(view)

        binding?.inputPhoneNumber?.setText(vm.state.value.phoneNumber)

        binding?.inputPhoneNumber?.addDebounceAfterTextChangedListener(100){
            vm.sent(TelegramLoginAction.SetPhoneNumber(it))
        }

        binding?.telegramLogInSubmit?.setOnClickListener {
            vm.sent(TelegramLoginAction.Submit)
        }

        binding?.openTelegramBot?.setOnClickListener {
            requireContext().startActivity(Intent(Intent.ACTION_VIEW, "https://t.me/needlework_number_bot".toUri()))
        }

        vm.state.onError(lifecycleScope){
            requireActivity().showError(it.message).show()
        }


        lifecycleScope.launch {
            vm.state.map { it.isLoading }.distinctUntilChanged().collectLatest {
                binding?.telegramLogInSubmit?.isEnabled = !it
                binding?.inputPhoneNumber?.isEnabled = !it
                binding?.telegramLogInSubmit?.visibility = if (!it) View.VISIBLE else View.GONE
                binding?.progressBar?.root?.visibility = if (it) View.VISIBLE else View.GONE

                if (vm.state.value.code.isNotBlank()) {
                    binding?.telegramLogInSubmit?.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            vm.state.map { it.code.isNotBlank() }.filter { it }.take(1).collectLatest {
                binding?.progressBar?.root?.visibility = View.GONE
                binding?.telegramLogInSubmit?.visibility = View.GONE
                binding?.inputPhoneNumber?.visibility = View.GONE
                binding?.inputPhoneNumberLayout?.visibility = View.GONE
                binding?.openTelegramBot?.visibility = View.VISIBLE
                binding?.telegramLoginText?.visibility = View.VISIBLE
            }
        }

        ToolBarController(
            navController = findNavController(),
            binding = binding?.toolbar ?: return,
            title = "Telegram login",
        ).setDefaultSettings()

        vm.state.onEnd(lifecycleScope){
            if (this.isHidden) return@onEnd
            navigator.navigateFromTelegramLogin(this)
        }
    }


    override fun onResume() {
        super.onResume()
        vm.sent(TelegramLoginAction.CheckLogin)
        if (vm.state.value.isEnd) {
            navigator.navigateFromTelegramLogin(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}