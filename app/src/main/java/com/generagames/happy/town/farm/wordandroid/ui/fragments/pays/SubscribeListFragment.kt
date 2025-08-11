package com.generagames.happy.town.farm.wordandroid.ui.fragments.pays

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentSubscribeListBinding
import com.generagames.happy.town.farm.wordandroid.ui.adapters.SubCostAdapter
import can.lucky.of.core.ui.controllers.ToolBarController
import com.generagames.happy.town.farm.wordandroid.actions.SubCostAction
import com.generagames.happy.town.farm.wordandroid.domain.vms.pay.SubCostViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR


class SubscribeListFragment : Fragment(R.layout.fragment_subscribe_list) {
    private var binding: FragmentSubscribeListBinding? = null
    private val subCostViewModel by viewModel<SubCostViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSubscribeListBinding.bind(view)

        binding?.subCostRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            subCostViewModel.state
                .map { it.content }
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }.collect {
                    binding?.subCostRecyclerView?.adapter = SubCostAdapter(it) { cost ->
                        subCostViewModel.sent(SubCostAction.Selected(cost))

                    }
                }
        }

        lifecycleScope.launch {
            subCostViewModel.state
                .map { it.isSelected }
                .distinctUntilChanged()
                .filter { it }
                .collect {
                    findNavController().navigate(
                        SubscribeListFragmentDirections.actionSubscribeListFragmentToChoosePayFragment()
                    )
                }
        }

        ToolBarController(
            binding = binding?.toolBar ?: return,
            title = getString(CoreR.string.costs_title),
            navController = findNavController()
        ).setDefaultSettings()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}