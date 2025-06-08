package can.lucky.of.profile.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import can.lucky.of.core.domain.models.enums.Currency
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.addDebounceAfterTextChangedListener
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import can.lucky.of.core.utils.onSelect
import can.lucky.of.core.utils.setContent
import can.lucky.of.profile.R
import can.lucky.of.profile.databinding.FragmentEditUserProfileBinding
import can.lucky.of.profile.domain.actions.EditUserProfileAction
import can.lucky.of.profile.domain.vms.EditUserProfileVm
import can.lucky.of.profile.ui.navigators.UserProfileNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditUserProfileFragment : Fragment(R.layout.fragment_edit_user_profile) {
    private var binding: FragmentEditUserProfileBinding? = null
    private val vm by viewModel<EditUserProfileVm>()
    private val navigator by inject<UserProfileNavigator>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentEditUserProfileBinding.bind(view)
        binding = newBinding

        lifecycleScope.launch {
            vm.state.filter { it.isInited }.take(1).collectLatest {state ->
                newBinding.firstNameInput.setText(state.firstName)
                newBinding.firstNameInput.addDebounceAfterTextChangedListener(200) {
                    vm.sent(EditUserProfileAction.FirstNameChanged(it))
                }
                newBinding.lastNameInput.setText(state.lastName)
                newBinding.lastNameInput.addDebounceAfterTextChangedListener(200) {
                    vm.sent(EditUserProfileAction.LastNameChanged(it))
                }
                newBinding.spinnerCurrency.setContent(Currency.entries)
                newBinding.spinnerCurrency.setSelection(state.currency.ordinal)
                newBinding.spinnerCurrency.onSelect<Currency>{ cur ->
                    cur?.run { vm.sent(EditUserProfileAction.CurrencyChanged(this)) }
                }
            }
        }

        vm.state.onEnd(lifecycleScope){
            navigator.popBack(this)
        }

        vm.state.onError(lifecycleScope){
            requireActivity().showError(it.message).show()
        }

        newBinding.editButton.setOnClickListener {
            vm.sent(EditUserProfileAction.Submit)
        }

        ToolBarController(
            title = "Edit profile",
            binding = newBinding.toolBar,
            fragment = this
        ).setDefaultSettings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}