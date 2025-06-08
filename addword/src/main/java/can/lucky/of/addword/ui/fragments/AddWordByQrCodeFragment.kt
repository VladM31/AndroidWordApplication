package can.lucky.of.addword.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.addword.R
import can.lucky.of.addword.databinding.FragmentAddWordByQrCodeBinding
import can.lucky.of.addword.domain.actions.AddWordByQrCodeAction
import can.lucky.of.addword.domain.models.ShareUserWord
import can.lucky.of.addword.domain.vms.AddWordByQrCodeVm
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddWordByQrCodeFragment : Fragment(R.layout.fragment_add_word_by_qr_code) {
    private var binding: FragmentAddWordByQrCodeBinding? = null
    private val vm by viewModel<AddWordByQrCodeVm>()


    private val scanQrCodeLauncher = registerForActivityResult(ScanQRCode(),this::handleQRResult)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddWordByQrCodeBinding.bind(view)

        if (vm.state.value.isInit.not()){
            vm.sent(AddWordByQrCodeAction.Init)
            scanQrCodeLauncher.launch(null)
        }

        binding?.acceptButton?.setOnClickListener {
            vm.sent(AddWordByQrCodeAction.Accept)
        }

        binding?.reScanButton?.setOnClickListener {
            scanQrCodeLauncher.launch(null)
        }

        vm.state.onEnd(lifecycleScope){
            findNavController().popBackStack()
        }

        vm.state.onError(lifecycleScope){
            requireActivity().showError(it.message).show()
        }

        handleSharedWord()

        ToolBarController(
            binding = binding?.toolBar ?: return,
            title = "Add word by QR code",
            navController = findNavController()
        ).setDefaultSettings()
    }

    @SuppressLint("SetTextI18n")
    private fun handleSharedWord() {
        lifecycleScope.launch {
            vm.state.map { it.word }.filterNotNull().take(1).collectLatest { shareWord ->
                binding?.acceptButton?.visibility = View.VISIBLE
                binding?.reScanButton?.visibility = View.INVISIBLE
                binding?.contentLayout?.visibility = View.VISIBLE

                binding?.wordText?.let {
                    it.text = it.text?.toString()?.format(shareWord.lang, shareWord.original)
                }

                binding?.translateText?.let {
                    it.text =
                        it.text?.toString()?.format(shareWord.translateLang, shareWord.translate)
                }

                binding?.descriptionText?.let {
                    it.text = it.text.toString() + ": " + shareWord.description.orEmpty()
                }

                binding?.detailsText?.text = shareWord.toDetails()
            }

        }
    }

    private fun ShareUserWord.toDetails() : String {
        val builder = StringBuilder()

        if (category != null){
            builder.append("Category: $category\n")
        }
        builder.append("CEFR: $cefr\n")
        builder.append("Has sound: $hasSound\n")
        builder.append("Has image: $hasImage")

        return builder.toString()
    }

    private fun handleQRResult(result: QRResult){
        if (result is QRResult.QRSuccess){
            vm.sent(AddWordByQrCodeAction.FetchWord(result.content.rawValue.orEmpty()))
            return
        }
        if (result is QRResult.QRError){
            vm.sent(AddWordByQrCodeAction.ErrorMessage(result.exception.message ?: "Unknown error"))
            return
        }
        if (result is QRResult.QRMissingPermission){
            vm.sent(AddWordByQrCodeAction.ErrorMessage("Missing permission"))
            return
        }
        if (result is QRResult.QRUserCanceled){
            vm.sent(AddWordByQrCodeAction.ErrorMessage("User canceled"))
            return
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}