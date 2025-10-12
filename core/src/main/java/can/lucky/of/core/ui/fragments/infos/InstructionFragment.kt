package can.lucky.of.core.ui.fragments.infos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.R
import can.lucky.of.core.actions.infos.InstructionAction
import can.lucky.of.core.databinding.FragmentInstructionBinding
import can.lucky.of.core.domain.vms.infos.InstructionViewModel
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


class InstructionFragment : Fragment(R.layout.fragment_instruction) {
    private val vm by viewModel<InstructionViewModel>()
    private var binding: FragmentInstructionBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentInstructionBinding.bind(view)
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

    private fun initToolBarController() {
        ToolBarController(
            navController = findNavController(),
            binding = binding?.toolBar ?: return,
            title = "Instruction",
        ).setDefaultSettings()
    }

    private fun showFile(file: File) {
        val currentBinding = binding ?: return
        currentBinding.pdfView.fromFile(file)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .onPageChange { page, pageCount -> println("Page: $page of $pageCount") }
            .onError { t -> vm.sent(InstructionAction.Error(t.message.orEmpty())) }
            .load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}