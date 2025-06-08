package can.lucky.of.addword.ui.fragments

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.addword.R
import can.lucky.of.addword.databinding.FragmentDefaultAddWordBinding
import can.lucky.of.addword.domain.actions.DefaultAddWordAction
import can.lucky.of.addword.domain.models.states.DefaultAddWordState
import can.lucky.of.addword.domain.vms.DefaultAddWordVm
import can.lucky.of.addword.ui.listeners.ItemSelectedListener
import can.lucky.of.core.choosers.AbstractChooser
import can.lucky.of.core.choosers.ImageChooser
import can.lucky.of.core.choosers.SoundChooser
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.loadDialog
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.listeners.SoundClickListener
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import can.lucky.of.core.utils.setContent
import kotlinx.coroutines.delay
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR

class DefaultAddWordFragment : Fragment(R.layout.fragment_default_add_word) {
    private var binding: FragmentDefaultAddWordBinding? = null
    private val vm by viewModel<DefaultAddWordVm>()
    private val loadDialog by lazy { loadDialog(requireContext()) }

    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentDefaultAddWordBinding.bind(view)
        binding = newBinding

        DefaultAddWordFragmentArgs.fromBundle(requireArguments()).word.let {
            vm.sent(DefaultAddWordAction.Init(it))
        }

        setLoadListener()
        setSelectParams(newBinding)
        setErrorHandle()

        binding?.addWordBtn?.setOnClickListener {
            vm.sent(DefaultAddWordAction.Add)
        }

        lifecycleScope.launch {
            vm.state.filter { it.isInited }.take(1).collectLatest { state ->
                setState(state)

                setTextChangeListeners()

                setSpinnerListeners()

                setImageStateListener()

                setSoundStateListener()

                loadDialog.dismiss()
            }
        }


        vm.state.onEnd(lifecycleScope) {
            findNavController().popBackStack(R.id.chooseAddWordFragment, true)
        }


        lifecycleScope.launch {
            vm.state.map { it.isSubscribe }.filterNotNull().distinctUntilChanged().collectLatest {
                hidePremiumFeatures(it)
            }
        }

        lifecycleScope.launch {
            vm.state.map { it.isLoading }.distinctUntilChanged().collectLatest {
                newBinding.loading.root.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        setToolBar(newBinding)
    }

    private fun setSoundStateListener() {
        lifecycleScope.launch {
            vm.state.map { it.sound }.distinctUntilChanged().drop(1).shareIn(
                lifecycleScope,
                SharingStarted.WhileSubscribed(),
                0
            ).collectLatest {
                mediaPlayer.reset()
                if (it == null) {
                    binding?.switchGenerateSound?.visibility = View.VISIBLE
                    binding?.playSoundBtn?.isClickable = false
                    binding?.playSoundBtn?.setImageResource(CoreR.drawable.sound_disable)
                    return@collectLatest
                }
                binding?.switchGenerateSound?.visibility = View.GONE


                mediaPlayer.setDataSource(requireContext(), it.toUri())
                mediaPlayer.prepareAsync()
            }
        }
    }

    private fun setImageStateListener() {


        lifecycleScope.launch {
            vm.state.map { it.image }.distinctUntilChanged().collectLatest {
                if (it == null) {
                    binding?.wordImage?.setImageResource(CoreR.drawable.image_icon)
                    return@collectLatest
                }
                binding?.wordImage?.setImageURI(it.toUri())
            }
        }
    }

    private fun setFileChoosers(){
        val imageChooser = ImageChooser(this)
        val soundChooser = SoundChooser(this)

        handleChooserLaunch(imageChooser) {
            vm.sent(DefaultAddWordAction.SetImage(it?.toFile()))
        }

        handleChooserLaunch(soundChooser) {
            vm.sent(DefaultAddWordAction.SetSound(it?.toFile()))
        }

        val subNotActive = vm.state.value.isSubscribe?.not() ?: return

        if (subNotActive) {
            return
        }

        setListeners(imageChooser, soundChooser)
    }


    private fun setListeners(imageChooser: ImageChooser, soundChooser: SoundChooser) {
        binding?.resetImageBtn?.setOnClickListener {
            imageChooser.start()
        }

        binding?.resetSoundBtn?.setOnClickListener {
            binding?.playSoundBtn?.isClickable = true
            binding?.playSoundBtn?.setImageResource(CoreR.drawable.sound)
            soundChooser.start()
        }

        binding?.deleteSoundBtn?.setOnClickListener {
            vm.sent(DefaultAddWordAction.SetSound(null))

            if (vm.state.value.sound == null) {
                binding?.playSoundBtn?.isClickable = false
                binding?.playSoundBtn?.setImageResource(CoreR.drawable.sound_disable)
                return@setOnClickListener
            }

            binding?.playSoundBtn?.isClickable = true
            binding?.playSoundBtn?.setImageResource(CoreR.drawable.sound)
        }


        binding?.playSoundBtn?.let {
            SoundClickListener(
                mediaPlayer = mediaPlayer,
                btn = it
            )
        }

        binding?.deleteImageBtn?.setOnClickListener {
            vm.sent(DefaultAddWordAction.SetImage(null))
        }

        mediaPlayer.setOnCompletionListener {
            binding?.playSoundBtn?.apply {
                setImageResource(CoreR.drawable.sound)
            }
        }

        binding?.playSoundBtn?.setOnClickListener {
            try {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                    return@setOnClickListener
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }



        binding?.switchGenerateSound?.setOnCheckedChangeListener { _, isChecked ->
            vm.sent(DefaultAddWordAction.SetNeedSound(isChecked))
        }

    }

    private fun hidePremiumFeatures(hasSub: Boolean){
        val visibility = if (hasSub) View.VISIBLE else View.GONE

        binding?.switchGenerateSound?.visibility = visibility
        binding?.wordImage?.visibility = visibility
        binding?.resetImageBtn?.visibility = visibility
        binding?.deleteImageBtn?.visibility = visibility
        binding?.wordSoundTitle?.visibility = visibility
        binding?.resetSoundBtn?.visibility = visibility
        binding?.deleteSoundBtn?.visibility = visibility
        binding?.playSoundBtn?.visibility = visibility
    }

    private fun setState(state: DefaultAddWordState){
        binding?.inputWord?.setText(state.word)
        binding?.inputTranslate?.setText(state.translation)
        binding?.inputCategory?.setText(state.category)
        binding?.inputDescription?.setText(state.description)
        binding?.spinnerCefr?.setSelection(CEFR.entries.indexOf(state.cefr))
        binding?.switchGenerateSound?.isChecked = state.needSound
        binding?.spinnerWordLang?.setSelection(Language.entries.indexOf(state.language))
        binding?.spinnerTranLang?.setSelection(Language.entries.indexOf(state.translationLanguage))
    }

    private fun handleChooserLaunch(
        chooser: AbstractChooser,
        actionFactory: (Uri?) -> Unit
    ) {
        lifecycleScope.launch {
            chooser.chooserState.collect {
                if (it.isSet.not()) {
                    return@collect
                }
                if (it.isSusses) {
                    actionFactory(it.file)
                }
            }
        }
    }

    private fun setTextChangeListeners() {
        binding?.inputWord?.addTextChangedListener(afterTextChanged =  {
            vm.sent(DefaultAddWordAction.SetWord(it?.toString().orEmpty()))
        })

        binding?.inputTranslate?.addTextChangedListener(afterTextChanged =  {
            vm.sent(DefaultAddWordAction.SetTranslation(it?.toString().orEmpty()))
        })

        binding?.inputCategory?.addTextChangedListener(afterTextChanged =  {
            vm.sent(DefaultAddWordAction.SetCategory(it?.toString().orEmpty()))
        })

        binding?.inputDescription?.addTextChangedListener(afterTextChanged = {
            vm.sent(DefaultAddWordAction.SetDescription(it?.toString().orEmpty()))
        })
    }

    private fun setSpinnerListeners() {
        binding?.spinnerWordLang?.onItemSelectedListener = ItemSelectedListener<String> { title ->
            vm.sent(DefaultAddWordAction.ChangeLanguage(Language.fromTitleCase(title)))
        }

        binding?.spinnerTranLang?.onItemSelectedListener = ItemSelectedListener<String> { title ->
            vm.sent(DefaultAddWordAction.ChangeTranslationLanguage(Language.fromTitleCase(title)))
        }

        binding?.spinnerCefr?.onItemSelectedListener = ItemSelectedListener<CEFR> {
            vm.sent(DefaultAddWordAction.SetCefr(it))
        }
    }

    private fun setErrorHandle() {
        vm.state.onError(lifecycleScope) {
            requireContext().showError(it.message).show()
        }
    }



    private fun setLoadListener() {
        lifecycleScope.launch {
            vm.state.map { it.isSubscribe }.distinctUntilChanged().collectLatest {
                if (it == null) {
                    loadDialog.show()
                    return@collectLatest
                }

                if (it) {
                    setFileChoosers()
                    loadDialog.dismiss()
                    return@collectLatest
                }

                delay(200)
                loadDialog.dismiss()
            }
        }
    }

    private fun setSelectParams(newBinding: FragmentDefaultAddWordBinding) {
        val langs =
            Language.entries.filter { it.isDefined }.map { it.titleCase }.toTypedArray()

        newBinding.spinnerWordLang.setContent(langs)
        newBinding.spinnerTranLang.setContent(langs)
        newBinding.spinnerCefr.setContent(CEFR.entries.toTypedArray())
    }

    private fun setToolBar(newBinding: FragmentDefaultAddWordBinding) {
        ToolBarController(
            findNavController(),
            newBinding.toolBar,
            "Add Word"
        ).setDefaultSettings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}