package com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.addword.domain.actions.AddWordByQrCodeAction
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.decorators.SpacesItemDecoration
import can.lucky.of.core.utils.onEnd
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.ScanPlayListAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentScanPlayListBinding
import com.generagames.happy.town.farm.wordandroid.domain.vms.ScanPlayListVm
import com.generagames.happy.town.farm.wordandroid.ui.adapters.ShortWordAdapter
import com.generagames.happy.town.farm.wordandroid.ui.navigations.ReFetchPlayListsNavigator
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScanPlayListFragment : Fragment(R.layout.fragment_scan_play_list) {
    private var binding: FragmentScanPlayListBinding? = null
    private val navigator by inject<ReFetchPlayListsNavigator>()
    private val scanQrCodeLauncher = registerForActivityResult(ScanQRCode(), this::handleQRResult)
    private val vm by viewModel<ScanPlayListVm>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScanPlayListBinding.bind(view)

        if (vm.state.value.playList == null) {
            scanQrCodeLauncher.launch(null)
        }

        binding?.wordsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.wordsRecyclerView?.addItemDecoration(SpacesItemDecoration(16))

        lifecycleScope.launch {
            vm.state.map { it.playList }.filterNotNull().collect { playList ->
                binding?.acceptButton?.visibility = View.VISIBLE
                binding?.playListNameText?.visibility = View.VISIBLE
                binding?.wordsRecyclerView?.visibility = View.VISIBLE
                binding?.reScanButton?.visibility = View.INVISIBLE
                binding?.playListNameText?.text = playList.name
                binding?.wordsRecyclerView?.adapter = ShortWordAdapter(playList)
            }
        }

        binding?.acceptButton?.setOnClickListener {
            it.isClickable = false
            vm.sent(ScanPlayListAction.Accept)

            lifecycleScope.launch {
                delay(1000)
                it.isClickable = true
            }
        }

        binding?.reScanButton?.setOnClickListener {
            scanQrCodeLauncher.launch(null)
        }


        vm.state.onEnd(lifecycleScope) {
            navigator.sendRequest(parentFragmentManager)
            findNavController().popBackStack()
        }

        ToolBarController(
            binding = binding?.toolBar ?: return,
            title = "Scan PlayList",
            navController = findNavController()
        ).setDefaultSettings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun handleQRResult(result: QRResult) {
        if (result is QRResult.QRSuccess) {
            vm.sent(ScanPlayListAction.SetId(result.content.rawValue.orEmpty()))
            return
        }
        if (result is QRResult.QRError) {
            vm.sent(ScanPlayListAction.SetError(result.exception.message ?: "Unknown error"))
            return
        }
        if (result is QRResult.QRMissingPermission) {
            vm.sent(ScanPlayListAction.SetError("Missing permission"))
            return
        }
        if (result is QRResult.QRUserCanceled) {
            vm.sent(ScanPlayListAction.SetError("User canceled"))
            return
        }
    }

}