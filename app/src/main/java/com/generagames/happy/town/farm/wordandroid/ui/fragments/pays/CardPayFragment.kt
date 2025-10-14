package com.generagames.happy.town.farm.wordandroid.ui.fragments.pays

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.ui.utils.NumberInputFilter
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import can.lucky.of.core.utils.onSelect
import can.lucky.of.core.utils.setContent
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.CardPayAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentCardPayBinding
import com.generagames.happy.town.farm.wordandroid.domain.vms.pay.CardPayViewModel
import com.generagames.happy.town.farm.wordandroid.ui.handels.ScanCardHandler
import com.generagames.happy.town.farm.wordandroid.ui.utils.CardNumberFormatTextWatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Year
import can.lucky.of.core.R as AppR


@SuppressLint("UseCompatLoadingForDrawables")
class CardPayFragment : Fragment(R.layout.fragment_card_pay) {
    private var binding: FragmentCardPayBinding? = null
    private val viewModel by viewModel<CardPayViewModel>()

    private val scanCardHandler = ScanCardHandler(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentCardPayBinding.bind(view)
        binding = newBinding

        newBinding.scanCardBtn.setOnClickListener {
            scanCardHandler.checkCameraPermissionAndScan()
        }

        scanCardHandler.setOnSuccessScanListener {
            newBinding.cardNumberInput.setText(it)
        }

        newBinding.cardNumberInput.addTextChangedListener(
            CardNumberFormatTextWatcher(
                newBinding.cardNumberInput
            )
        )

        initStateValues()
        setSubmitEnabledListener()
        setDateCacheListener()
        initStateListeners()
        initToolBar()
        setConfirmPayBtnClickListener()
    }

    private fun setConfirmPayBtnClickListener() {
        val newBinding = binding ?: return
        newBinding.confirmPayButton.setOnClickListener {
            viewModel.sent(
                CardPayAction.ConfirmPay(
                    cardNumber = newBinding.cardNumberInput.text.toString(),
                    cardName = newBinding.cardNameInput.text.toString(),
                    cvv2 = newBinding.cvv2Input.text.toString(),
                    phoneNumber = newBinding.phoneNumberInput.text.toString(),
                    email = newBinding.emailInput.text.toString()
                )
            )
        }
    }

    private fun initStateValues() {
        val newBinding = binding ?: return
        newBinding.gridLayout.columnCount = getString(AppR.string.card_pay_column_count).toInt()
        newBinding.gridLayout.rowCount = getString(AppR.string.card_pay_row_count).toInt()
        newBinding.phoneNumberInput.setText(viewModel.state.value.phoneNumber)
        newBinding.phoneNumberInput.filters = arrayOf(NumberInputFilter())
        newBinding.emailInput.setText(viewModel.state.value.email)
        newBinding.cvv2Input.filters =
            arrayOf(NumberInputFilter(), android.text.InputFilter.LengthFilter(3))

        newBinding.monthSpinner.setContent((1..12).toList())
        newBinding.monthSpinner.setSelection(viewModel.state.value.expiryMonth - 1)
        newBinding.monthSpinner.onSelect<Int> {
            it?.let { month ->
                viewModel.sent(CardPayAction.SetExpiryMonth(month))
            }
        }

        val currentYear = Year.now().value
        val years = (currentYear until currentYear + 40).toList()
        newBinding.yearSpinner.setContent(years)
        newBinding.yearSpinner.setSelection(years.indexOf(viewModel.state.value.expiryYear))
        newBinding.yearSpinner.onSelect<Int> {
            it?.let { year ->
                viewModel.sent(CardPayAction.SetExpiryYear(year))
            }
        }
    }

    private fun setSubmitEnabledListener() {
        lifecycleScope.launch {
            viewModel.state.map { it.submitEnabled }.distinctUntilChanged().collectLatest {
                binding?.confirmPayButton?.isClickable = it
                binding?.confirmPayButton?.visibility = if (it) View.VISIBLE else View.GONE
                binding?.progressBar?.root?.visibility = if (it.not()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setDateCacheListener() {
        lifecycleScope.launch {
            viewModel.state.map { it.dateCacheId }.distinctUntilChanged().drop(1)
                .collectLatest {
                    AlertDialog.Builder(requireActivity(), can.lucky.of.core.R.style.DialogStyle)
                        .setMessage("Open telegram bot. Please confirm the payment in the next 5 minutes.")
                        .setTitle("Information")
                        .setPositiveButton("Ok") { _, _ -> }
                        .create()
                        .show()
                }
        }
    }

    private fun initStateListeners() {
        viewModel.state.onError(lifecycleScope) {
            requireActivity().showError(it.message).show()
        }
    }

    private fun initToolBar() {
        val toolBar = binding?.toolBar ?: return

        ToolBarController(
            binding = toolBar,
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

    override fun onResume() {
        super.onResume()
        viewModel.state.onEnd(lifecycleScope) {
            findNavController().popBackStack(R.id.subscribeFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}