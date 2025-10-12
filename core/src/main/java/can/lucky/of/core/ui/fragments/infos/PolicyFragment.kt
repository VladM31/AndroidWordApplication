package can.lucky.of.core.ui.fragments.infos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.R
import can.lucky.of.core.actions.infos.PolicyAction
import can.lucky.of.core.databinding.FragmentPolicyBinding
import can.lucky.of.core.domain.vms.infos.PolicyViewModel
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class PolicyFragment : Fragment(R.layout.fragment_policy) {
    private var binding: FragmentPolicyBinding? = null
    private val vm by viewModel<PolicyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentPolicyBinding.bind(view)
        binding = newBinding

        initToolBarController()

        vm.state.onError(lifecycleScope) {
            requireActivity().showError(it.message).show()
        }

        vm.state.onEnd(lifecycleScope) {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            vm.state.map { it.file }
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest {
                    showFile(it)
                }
        }
    }

    private fun showFile(file: File) {
        val currentBinding = binding ?: return

        currentBinding.pdfView.fromFile(file)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .onPageChange { page, pageCount -> println("Page: $page of $pageCount") }
            .onError { t -> vm.sent(PolicyAction.Error(t.message.orEmpty())) }
            .load()
    }

    private fun initToolBarController() {
        ToolBarController(
            navController = findNavController(),
            binding = binding?.toolBar ?: return,
            title = "Policy",
        ).setDefaultSettings()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}