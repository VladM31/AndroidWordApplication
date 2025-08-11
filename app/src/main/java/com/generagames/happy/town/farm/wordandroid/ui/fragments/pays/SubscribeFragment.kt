package com.generagames.happy.town.farm.wordandroid.ui.fragments.pays

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentSubscribeBinding
import can.lucky.of.core.ui.controllers.ToolBarController
import com.generagames.happy.town.farm.wordandroid.actions.SubscribeAction
import com.generagames.happy.town.farm.wordandroid.domain.vms.pay.SubscribeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR


class SubscribeFragment : Fragment(R.layout.fragment_subscribe) {
    private var binding: FragmentSubscribeBinding? = null
    private val viewModel by viewModel<SubscribeViewModel>()
    private val subTextTemplate by lazy {
        getString(CoreR.string.subscription_s)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentSubscribeBinding.bind(view)
        binding = newBinding

        newBinding.payButton.setOnClickListener {
            findNavController().navigate(SubscribeFragmentDirections.actionSubscribeFragmentToSubscribeListFragment())
        }

        ToolBarController(
            binding = newBinding.toolBar,
            title = getString(CoreR.string.subscription),
            navController = findNavController()
        ).setDefaultSettings()
    }

    override fun onStart() {
        super.onStart()
        viewModel.sent(SubscribeAction.UpdateState)
        lifecycleScope.launch {
            viewModel.state.collect {
                binding?.apply {
                    subscriptionExpirationDateText.text = it.expirationDate
                    subscriptionStateText.text = subTextTemplate.format(it.condition.toTitleCase())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}