package can.lucky.of.profile.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.profile.R
import can.lucky.of.profile.databinding.FragmentUserProfileBinding
import can.lucky.of.profile.domain.actions.UserProfileAction
import can.lucky.of.profile.domain.vms.UserProfileVm
import can.lucky.of.profile.ui.navigators.UserProfileNavigator
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {
    private var binding: FragmentUserProfileBinding? = null
    private val vm by viewModel<UserProfileVm>()
    private val navigator by inject<UserProfileNavigator>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserProfileBinding.bind(view)

        lifecycleScope.launch {
            vm.state.collect { state ->
                binding?.apply {
                    firstNameText.text = state.firstName
                    lastNameText.text = state.lastName
                    phoneText.text = state.phone
                    currencyText.text = state.currency
                    if (state.email != null) {
                        emailText.text = state.email
                        emailText.visibility = View.VISIBLE
                        emailLabel.visibility = View.VISIBLE
                    } else {
                        emailText.visibility = View.GONE
                        emailLabel.visibility = View.GONE
                    }
                }
            }
        }

        parentFragmentManager

        ToolBarController(
            title = "Profile",
            binding = binding?.toolBar ?: return,
            fragment = this,
            buttonImage = can.lucky.of.core.R.drawable.setting,
            buttonAction = {
                navigator.navigateToEdit(this)
            }
        ).setDefaultSettings()

        navigator.listenPopBack(this) {
            vm.sent(UserProfileAction.Refresh)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}