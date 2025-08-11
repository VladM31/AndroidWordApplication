package com.generagames.happy.town.farm.wordandroid.ui.fragments.pays

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.onEnd
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.pay.ChoosePayAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentChoosePayBinding
import com.generagames.happy.town.farm.wordandroid.domain.handlers.GPayHandler
import com.generagames.happy.town.farm.wordandroid.domain.vms.pay.ChoosePayViewModel
import com.google.android.gms.wallet.contract.TaskResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChoosePayFragment : Fragment(R.layout.fragment_choose_pay) {
    private val gPayHandler by lazy {
        GPayHandler(requireActivity())
    }
    private val vm by viewModel<ChoosePayViewModel>()
    private var binding: FragmentChoosePayBinding? = null

    private val googlePayLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val data = result.data ?: return@registerForActivityResult
        if (result.resultCode != RESULT_OK) {
            vm.sent(ChoosePayAction.Error("Payment cancelled or error"))
            return@registerForActivityResult
        }
        val paymentData = com.google.android.gms.wallet.PaymentData.getFromIntent(data) ?: return@registerForActivityResult
        val token = gPayHandler.extractPaymentMethodToken(paymentData)
        Toast.makeText(
            requireContext(),
            "You can process the payment.",
            Toast.LENGTH_LONG
        ).show()
        vm.sent(ChoosePayAction.FetchedGPayToken(token))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentChoosePayBinding.bind(view)
        binding = newBinding

        setGPayEnableListener()
        initGooglePay()

        setOnClick()
        setToolBar()

        vm.state.onEnd(lifecycleScope){
            findNavController().popBackStack(R.id.subscribeFragment,false)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setGPayEnableListener() {
        lifecycleScope.launch {
            vm.state.map { it.googlePay }
                .map { it.isSubmitEnabled }
                .distinctUntilChanged()
                .collect {
                    binding?.googlePayBtn?.isEnabled = it

                    val backId = if (it) {
                        can.lucky.of.core.R.drawable.button_back
                    } else {
                        can.lucky.of.core.R.drawable.disable_back
                    }
                    binding?.googlePayBtn?.background = requireContext().getDrawable(backId)
                }
        }
    }

    private fun initGooglePay() {
        if (vm.state.value.googlePay.isInited) {
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val isReady = gPayHandler.isReadyToPay();
            vm.sent(ChoosePayAction.GooglePayReady(isReady))
        }
    }

    private fun setOnClick() {
        binding?.cardPayBtn?.setOnClickListener {
            findNavController().navigate(ChoosePayFragmentDirections.actionChoosePayFragmentToCardPayFragment())
        }

        binding?.googlePayBtn?.setOnClickListener {
            handleGPay()
        }
    }

    private fun handleGPay() {
        val costProposition = vm.state.value.costProposition
        val paymentDataTask = gPayHandler.createPaymentTask(costProposition.cost,costProposition.currency.name)

        paymentDataTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val paymentData = task.result
                val token = gPayHandler.extractPaymentMethodToken(paymentData)
                Toast.makeText(requireContext(), "You can process the payment.", Toast.LENGTH_LONG)
                    .show()
                vm.sent(ChoosePayAction.FetchedGPayToken(token))
            } else {
                when (val exception = task.exception) {
                    is com.google.android.gms.common.api.ResolvableApiException -> {
                        // Для разрешения ошибки (например, добавить карту) — запустите resolution
                        // Если нужно, используйте отдельный лаунчер для IntentSenderRequest
                        val intentSenderRequest =
                            androidx.activity.result.IntentSenderRequest.Builder(exception.resolution.intentSender)
                                .build()
                        googlePayLauncher.launch(intentSenderRequest)
                    }

                    is com.google.android.gms.common.api.ApiException -> {
                        when (exception.statusCode) {
                            com.google.android.gms.common.api.CommonStatusCodes.CANCELED -> vm.sent(
                                ChoosePayAction.Error("Платеж отменен")
                            )

                            com.google.android.gms.common.api.CommonStatusCodes.DEVELOPER_ERROR -> vm.sent(
                                ChoosePayAction.Error("Ошибка разработчика: ${exception.message}")
                            )

                            else -> vm.sent(ChoosePayAction.Error("Ошибка: ${exception.message}"))
                        }
                    }

                    else -> {
                        vm.sent(ChoosePayAction.Error("Непредвиденная ошибка: ${exception?.message}"))
                    }
                }
            }
        }
    }

    private fun setToolBar() {
        val newBinding = binding ?: return
        ToolBarController(
            binding = newBinding.toolBar,
            title = "Pay methods",
            navController = findNavController()
        ).setDefaultSettings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}