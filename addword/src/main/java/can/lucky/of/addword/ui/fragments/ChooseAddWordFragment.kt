package can.lucky.of.addword.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.addword.R
import can.lucky.of.addword.databinding.FragmentChooseAddWordBinding
import can.lucky.of.addword.domain.vms.ChooseAddWordVm
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.utils.setColumnCountByOrientation
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("UseCompatLoadingForDrawables")
class ChooseAddWordFragment : Fragment(R.layout.fragment_choose_add_word) {
    private var binding: FragmentChooseAddWordBinding? = null
    private val vm by viewModel<ChooseAddWordVm>()
    private val disableDraw by lazy {
        requireContext().getDrawable(can.lucky.of.core.R.drawable.disable_back)
    }
    private val enableDraw by lazy {
        requireContext().getDrawable(can.lucky.of.core.R.drawable.button_back)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChooseAddWordBinding.bind(view)

        initButtons()

        initOrientation()

        initToolBar()

        initOnClickListeners()

        initSubscribeListener()
    }

    private fun initSubscribeListener() {
        lifecycleScope.launch {
            vm.state.collect {
                if (it.isSubscribed != true) {
                    return@collect
                }
//                binding?.byImageBtn?.background = enableDraw
//                binding?.byImageBtn?.isClickable = true
                binding?.byTextBtn?.background = enableDraw
                binding?.byTextBtn?.isClickable = true
            }
        }
    }

    private fun initOnClickListeners() {
        binding?.byDefaultBtn?.setOnClickListener {
            findNavController().navigate(ChooseAddWordFragmentDirections.actionChooseAddWordFragmentToDefaultAddWordFragment())
        }

        binding?.byImageBtn?.setOnClickListener {
            findNavController().navigate(ChooseAddWordFragmentDirections.actionChooseAddWordFragmentToAddWordByImageFragment())
        }

        binding?.byQrCodeBtn?.setOnClickListener {
            findNavController().navigate(ChooseAddWordFragmentDirections.actionChooseAddWordFragmentToAddWordByQrCodeFragment())
        }

        binding?.byTextBtn?.setOnClickListener {
            findNavController().navigate(ChooseAddWordFragmentDirections.actionChooseAddWordFragmentToAnalyzeWordTasksFragment())
        }
    }


    private fun initOrientation() {
        binding?.menuGridLayout?.setColumnCountByOrientation(1, 2)
    }

    private fun initButtons() {
        binding?.byImageBtn?.background = disableDraw
        binding?.byImageBtn?.isClickable = false
        binding?.byImageBtn?.visibility = View.GONE

        binding?.byQrCodeBtn?.background = disableDraw
        binding?.byQrCodeBtn?.isClickable = false
        binding?.byQrCodeBtn?.visibility = View.GONE // For now, hide QR code option

        binding?.byTextBtn?.background = disableDraw
        binding?.byTextBtn?.isClickable = false
    }

    private fun initToolBar() {
        ToolBarController(
            findNavController(),
            binding?.toolBar ?: return,
            "Choose add word",
        ).setDefaultSettings()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}