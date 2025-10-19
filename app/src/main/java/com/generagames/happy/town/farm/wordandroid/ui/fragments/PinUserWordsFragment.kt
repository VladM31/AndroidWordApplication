package com.generagames.happy.town.farm.wordandroid.ui.fragments

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.choosers.AbstractChooser
import can.lucky.of.core.choosers.ImageChooser
import can.lucky.of.core.choosers.SoundChooser
import can.lucky.of.core.domain.factories.GlideHeaderFactory
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.listeners.SoundClickListener
import can.lucky.of.core.utils.onEnd
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.PinUserWordsAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPinUserWordsBinding
import com.generagames.happy.town.farm.wordandroid.domain.vms.PinUserWordsViewModel
import com.generagames.happy.town.farm.wordandroid.ui.handels.pin.NumberTextCollectHandler
import com.generagames.happy.town.farm.wordandroid.ui.handels.pin.PinUserWordCollectHandler
import com.generagames.happy.town.farm.wordandroid.ui.handels.pin.SetImageCollectHandler
import com.generagames.happy.town.farm.wordandroid.ui.handels.pin.SetSoundCollectHandler
import com.generagames.happy.town.farm.wordandroid.ui.handels.pin.WordTextCollectHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR


class PinUserWordsFragment : Fragment(R.layout.fragment_pin_user_words) {
    private var binding: FragmentPinUserWordsBinding? = null
    private val pinViewModel by viewModel<PinUserWordsViewModel>()
    private val headerFactory by inject<GlideHeaderFactory>()


    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageChooser = ImageChooser(this)
        val soundChooser = SoundChooser(this)

        val newBinding = FragmentPinUserWordsBinding.bind(view)
        binding = newBinding

        pinViewModel.state.onEnd(lifecycleScope) {
            findNavController().navigate(
                PinUserWordsFragmentDirections.actionPinUserWordsFragmentToUserWordsFragment(),
                NavOptions.Builder().setPopUpTo(R.id.menuFragment, false).build()
            )
        }

        lifecycleScope.launch {
            pinViewModel.state.map { it.isInited }.filter { it }.take(1).collectLatest {
                binding?.loadingLayout?.root?.visibility = View.INVISIBLE
                binding?.addAll?.visibility = View.VISIBLE
            }
        }

        initIndexListener()

        initToolBarController()

        val args = PinUserWordsFragmentArgs.fromBundle(requireArguments())

        handleStateLaunch(imageChooser, soundChooser)

        handleChooserLaunch(imageChooser) {
            PinUserWordsAction.SetImage(it)
        }

        handleChooserLaunch(soundChooser) {
            PinUserWordsAction.SetSound(it)
        }

        createCollectHandlers().forEach {
            lifecycleScope.launch {
                it.handleCollect(binding, pinViewModel.state)
            }
        }

        pinViewModel.sent(PinUserWordsAction.Load(args.wordIds.toList()))


    }

    private fun initIndexListener() {
        lifecycleScope.launch {
            pinViewModel.state.distinctUntilChanged { old, new ->
                old.index == new.index && old.words.size == new.words.size
            }
                .collectLatest { stateValue ->
                    updateNavigationButtonsVisibility(stateValue.index, stateValue.words.size)
                }
        }
    }

    private fun updateNavigationButtonsVisibility(index: Int, size: Int) {
        if (size == 1) {
            binding?.nextBtn?.visibility = View.INVISIBLE
            binding?.previousBtn?.visibility = View.INVISIBLE
            return
        }
        if (index == 0) {
            binding?.previousBtn?.visibility = View.INVISIBLE
            binding?.nextBtn?.visibility = View.VISIBLE
            return
        }
        if (index == size - 1) {
            binding?.nextBtn?.visibility = View.INVISIBLE
            binding?.previousBtn?.visibility = View.VISIBLE
            return
        }
        binding?.nextBtn?.visibility = View.VISIBLE
        binding?.previousBtn?.visibility = View.VISIBLE
    }


    private fun handleStateLaunch(imageChooser: ImageChooser, soundChooser: SoundChooser) {

        lifecycleScope.launch {
            pinViewModel.state.map { it.isInited }
                .distinctUntilChanged()
                .filter { it }
                .collectLatest {
                    setListeners(imageChooser, soundChooser)
                }
        }
    }

    private fun handleChooserLaunch(
        chooser: AbstractChooser,
        actionFactory: (Uri?) -> PinUserWordsAction
    ) {
        lifecycleScope.launch {
            chooser.chooserState.collect {
                if (it.isSet.not()) {
                    return@collect
                }
                if (it.isSusses) {
                    pinViewModel.sent(actionFactory(it.file))
                }
            }
        }
    }


    private fun initToolBarController() {
        ToolBarController(
            navController = findNavController(),
            binding = binding?.toolBar ?: return,
            title = "Set custom files",
        ).setDefaultSettings()
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
            pinViewModel.sent(PinUserWordsAction.SetSound(null))

            val word = pinViewModel.state.value.run { words[index] }

            if (word.soundLink == null) {
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
            pinViewModel.sent(PinUserWordsAction.SetImage(null))
        }

        mediaPlayer.setOnCompletionListener {
            binding?.playSoundBtn?.apply {
                setImageResource(CoreR.drawable.sound)
            }
        }


        binding?.addAll?.setOnClickListener {
            it?.isClickable = false
            pinViewModel.sent(PinUserWordsAction.Pin)
        }

        binding?.nextBtn?.setOnClickListener {
            pinViewModel.sent(PinUserWordsAction.NextWord)
        }

        binding?.previousBtn?.setOnClickListener {
            pinViewModel.sent(PinUserWordsAction.PreviousWord)
        }

        binding?.saveChangesBtn?.setOnClickListener {
            pinViewModel.sent(PinUserWordsAction.SaveFiles)
            Toast.makeText(requireContext(), CoreR.string.changes_saved, Toast.LENGTH_SHORT).show()
        }


    }

    private fun createCollectHandlers(): List<PinUserWordCollectHandler> {
        return listOf(
            NumberTextCollectHandler,
            WordTextCollectHandler(getString(CoreR.string.text_template)),
            SetImageCollectHandler(
                context = requireContext(),
                headers = headerFactory.createHeaders()
            ),
            SetSoundCollectHandler(
                context = requireContext(),
                headers = headerFactory.createHeaders(),
                mediaPlayer = mediaPlayer
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        mediaPlayer.setOnPreparedListener(null)
    }
}

