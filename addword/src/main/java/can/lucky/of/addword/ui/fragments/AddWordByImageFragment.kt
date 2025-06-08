package can.lucky.of.addword.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import can.lucky.of.addword.R
import can.lucky.of.addword.databinding.FragmentAddWordByImageBinding
import can.lucky.of.core.R as CoreR
import can.lucky.of.core.choosers.ImageChooser
import can.lucky.of.addword.domain.vms.AddWordByImageVm
import can.lucky.of.addword.domain.actions.AddWordByImageAction
import can.lucky.of.addword.ui.listeners.ItemSelectedListener
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.setContent
import can.lucky.of.core.utils.setImageByUri
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddWordByImageFragment : Fragment(R.layout.fragment_add_word_by_image) {
    private var binding: FragmentAddWordByImageBinding? = null

    private val vm by viewModel<AddWordByImageVm>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val newBinding = FragmentAddWordByImageBinding.bind(view)
        binding = newBinding

        val imageChooser = ImageChooser(this)

        lifecycleScope.launch {
            vm.state.map { it.image }.distinctUntilChanged().collect {
                if (it == null) {
                    newBinding.selectedImage.setImageResource(CoreR.drawable.image_icon)
                } else {
                    newBinding.selectedImage.setImageByUri(it)
                }
            }
        }

        handleImageChooser(imageChooser)

        newBinding.makePhotoButton.setOnClickListener {
            imageChooser.start()
        }

        handleError()

        setSelectParams()

        setSpinnerListeners()

        newBinding.addWordButton.setOnClickListener {
            vm.sent(AddWordByImageAction.Confirm)
        }

        setLanguages()

        binding?.let {
            setToolBar(it)
        }

        lifecycleScope.launch {
            vm.state.map { it.word }
                .distinctUntilChanged()
                .filterNotNull()
                .take(1)
                .collectLatest {

                    findNavController().navigate(
                        AddWordByImageFragmentDirections.actionAddWordByImageFragmentToDefaultAddWordFragment(it),
                        NavOptions.Builder().setPopUpTo(R.id.chooseAddWordFragment, false).build()
                    )
                }

        }

        lifecycleScope.launch {
            vm.state.map { it.isLoading }.distinctUntilChanged().collectLatest {
                newBinding.addWordButton.isEnabled = it.not()
                newBinding.addWordButton.visibility = if (it) View.INVISIBLE else View.VISIBLE
                newBinding.loading.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    private fun setToolBar(newBinding: FragmentAddWordByImageBinding) {
        ToolBarController(
            findNavController(),
            newBinding.toolBar,
            "Recognize word by image"
        ).setDefaultSettings()
    }

    private fun setSelectParams() {
        val langs =
            Language.entries.filter { it.isDefined }.map { it.titleCase }.toTypedArray()

        binding?.spinnerWordLang?.setContent(langs)
        binding?.spinnerTranLang?.setContent(langs)
    }

    private fun setSpinnerListeners() {
        binding?.spinnerWordLang?.onItemSelectedListener = ItemSelectedListener<String> { title ->
            vm.sent(AddWordByImageAction.SetLanguage(Language.fromTitleCase(title)))
        }

        binding?.spinnerTranLang?.onItemSelectedListener = ItemSelectedListener<String> { title ->
            vm.sent(AddWordByImageAction.SetTranslateLanguage(Language.fromTitleCase(title)))
        }


    }

    private fun setLanguages() {
        vm.state.value.language.let {
            if (it != Language.UNDEFINED) {
                binding?.spinnerWordLang?.setSelection(Language.entries.indexOf(it))
            }

        }

        vm.state.value.translateLanguage.let {
            if (it != Language.UNDEFINED) {
                binding?.spinnerTranLang?.setSelection(Language.entries.indexOf(it))
            }
        }
    }

    private fun handleError() {
        lifecycleScope.launch {
            vm.state.map { it.error }
                .distinctUntilChanged()
                .drop(1)
                .filterNotNull()
                .shareIn(lifecycleScope, SharingStarted.WhileSubscribed(), 0)
                .collectLatest {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage(it.message)
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
        }
    }

    private fun handleImageChooser(imageChooser: ImageChooser) {
        lifecycleScope.launch {
            imageChooser.chooserState.collect {
                if (it.isSet.not()) {
                    return@collect
                }
                if (it.isSusses) {
                    it.file?.let { uri ->
                        vm.sent(AddWordByImageAction.SetImage(uri))
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}