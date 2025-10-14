package can.lucky.of.auth.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.auth.R
import can.lucky.of.auth.databinding.FragmentConfirmSignUpBinding
import can.lucky.of.auth.domain.actions.ConfirmSignUpAction
import can.lucky.of.auth.domain.models.data.LogInBundle
import can.lucky.of.auth.domain.vms.ConfirmSignUpVm
import can.lucky.of.auth.ui.navigations.AuthNavigator
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.AppConstants
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmSignUpFragment : Fragment(R.layout.fragment_confirm_sign_up) {
    private var binding: FragmentConfirmSignUpBinding? = null
    private var navigateJob: Job? = null
    private val vm by viewModel<ConfirmSignUpVm>()
    private val navigator by inject<AuthNavigator>()

    private val text = """
        1. Click the button below to open the Telegram bot
        2. Press the start button in the bot
        3. Click the Contact button in the bot
        4. Wait 10-30 seconds
        5. Under the bot message, click the confirm registration button
        6. Return to the app and log in
    """.trimIndent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConfirmSignUpBinding.bind(view)


        binding?.openTelegramBot?.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    AppConstants.telegramBotLink.toUri()
                )
            )
        }

        binding?.confirmSignUpText?.text = text

        LogInBundle.parse(requireArguments())?.let {
            vm.sent(ConfirmSignUpAction.Init(it.phoneNumber, it.password))
        }


        binding?.toolbar?.let { toolbar ->
            ToolBarController(
                navController = findNavController(),
                binding = toolbar,
                title = "Confirm Sign Up",
            ).setDefaultSettings()
        }


    }

    override fun onStart() {
        super.onStart()
        navigateJob = lifecycleScope.launch {
            vm.state.map { it.isLoading }.distinctUntilChanged().filter { it.not() }.collectLatest {
                navigator.navigateFromConfirmation(findNavController())
            }
        }
    }

    override fun onStop() {
        super.onStop()
        navigateJob?.cancel()
        navigateJob = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}