package com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.dp
import can.lucky.of.core.utils.formatSecondsToMinutes
import can.lucky.of.core.utils.onEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.SharePlayListAction
import com.generagames.happy.town.farm.wordandroid.actions.ShareUserWordAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentShareByQrCodeBinding
import com.generagames.happy.town.farm.wordandroid.domain.vms.SharePlayListVm
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SharePlayListFragment : Fragment(R.layout.fragment_share_by_qr_code) {
    private var binding: FragmentShareByQrCodeBinding? = null
    private val vm by viewModel<SharePlayListVm>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShareByQrCodeBinding.bind(view)

        val image = binding?.qrCodeImage ?: return

        if (vm.state.value.isInit.not()) {
            vm.sent(
                SharePlayListAction.Init(
                    playListId = SharePlayListFragmentArgs.fromBundle(requireArguments()).playListId,
                    width = 320.dp.toInt(),
                    height = 320.dp.toInt()
                )
            )
        }


        lifecycleScope.launch {
            vm.state.filter { it.isInit }.take(1).collectLatest { state ->
                Glide.with(image)
                    .asBitmap()
                    .load(state.qrCode)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(image)

            }
        }

        lifecycleScope.launch {
            vm.state.filter { it.isInit }.map { it.time }.distinctUntilChanged().collectLatest {
                binding?.timeClock?.text = it.formatSecondsToMinutes()
            }
        }

        vm.state.onEnd(lifecycleScope) {
            findNavController().popBackStack()
        }

        ToolBarController(
            binding = binding?.toolBar ?: return,
            navController = findNavController(),
            title = "Share play list"
        ).setDefaultSettings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}