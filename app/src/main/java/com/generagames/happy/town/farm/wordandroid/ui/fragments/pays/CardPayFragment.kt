package com.generagames.happy.town.farm.wordandroid.ui.fragments.pays

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.CardPayAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentCardPayBinding
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.onError
import com.generagames.happy.town.farm.wordandroid.domain.vms.pay.CardPayViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as AppR


class CardPayFragment : Fragment(R.layout.fragment_card_pay) {
    private var binding: FragmentCardPayBinding? = null
    private val viewModel by viewModel<CardPayViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentCardPayBinding.bind(view)
        binding = newBinding

        newBinding.gridLayout.columnCount = getString(AppR.string.card_pay_column_count).toInt()
        newBinding.gridLayout.rowCount = getString(AppR.string.card_pay_row_count).toInt()
        newBinding.phoneNumberInput.setText(viewModel.state.value.phoneNumber)
        newBinding.emailInput.setText(viewModel.state.value.email)

        viewModel.state.onError(lifecycleScope){
            newBinding.confirmPayButton.isClickable = false
            requireActivity().showError(it.message).show()
            delay(1000)
            newBinding.confirmPayButton.isClickable = true
        }

        lifecycleScope.launch {
            viewModel.state.map { it.dateCacheId }.filter { it.isNotBlank() }.take(1)
                .collectLatest {
                    newBinding.confirmPayButton.isClickable = false
                    Toast.makeText(
                        requireContext(),
                        "Open telegram bot. Please confirm the payment in the next 5 minutes.",
                        Toast.LENGTH_LONG
                    ).show()

                }
        }

        lifecycleScope.launch {
            viewModel.state.map { it.isBack }.distinctUntilChanged().filter { it }.take(1).collectLatest {
                findNavController().popBackStack(R.id.subscribeFragment,false)
            }
        }



        newBinding.confirmPayButton.setOnClickListener {
            viewModel.sent(
                CardPayAction.ConfirmPay(
                    cardNumber = newBinding.cardNumberInput.text.toString(),
                    cardName = newBinding.cardNameInput.text.toString(),
                    expiryDate = newBinding.expiryDateInput.text.toString(),
                    cvv2 = newBinding.cvv2Input.text.toString(),
                    phoneNumber = newBinding.phoneNumberInput.text.toString(),
                    email = newBinding.emailInput.text.toString()
                )
            )
        }

        ToolBarController(
            binding = newBinding.toolBar,
            title = getString(AppR.string.card_pay),
            navController = findNavController(),
            buttonImage = can.lucky.of.core.R.drawable.telegram_image,
            buttonAction = {
                requireContext().startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://t.me/needlework_number_bot".toUri()
                    )
                )
            }
        ).setDefaultSettings()
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            viewModel.state.map { it.isBack }.filter { it }.take(1).collectLatest {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}