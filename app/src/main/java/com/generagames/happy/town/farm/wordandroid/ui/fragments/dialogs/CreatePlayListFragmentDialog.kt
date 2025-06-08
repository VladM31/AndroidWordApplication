package com.generagames.happy.town.farm.wordandroid.ui.fragments.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.CreatePlayListAction
import com.generagames.happy.town.farm.wordandroid.databinding.DialogFragmentCreatePlayListBinding
import com.generagames.happy.town.farm.wordandroid.domain.vms.CreatePlayListViewModel
import com.generagames.happy.town.farm.wordandroid.ui.navigations.ReFetchPlayListsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlayListFragmentDialog : DialogFragment(R.layout.dialog_fragment_create_play_list) {
    private val viewModel by viewModel<CreatePlayListViewModel>()
    private var binding: DialogFragmentCreatePlayListBinding? = null
    private val navigator by inject<ReFetchPlayListsNavigator>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogFragmentCreatePlayListBinding.bind(view)

        handleError()

        binding?.apply {
            createButton.setOnClickListener {
                viewModel.sent(
                    CreatePlayListAction.Create(
                        playListNameInput.text?.toString().orEmpty()
                    )
                )
                createButton.isClickable = false
                lifecycleScope.launch {
                    delay(1000)
                    createButton.isClickable = true
                }
            }
        }

        handleDismiss()
    }

    private fun handleError() {
        lifecycleScope.launch {
            viewModel.state
                .map { it.messageError }
                .filter { it != null }
                .distinctUntilChanged()
                .collectLatest {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    delay(1000)
                }
        }
    }

    private fun handleDismiss() {
        lifecycleScope.launch {
            viewModel.state
                .filter { it.dismiss }
                .distinctUntilChanged()
                .collectLatest {
                    if (it.saveResult == true){
                        navigator.sendRequest(parentFragmentManager)
                    }
                    dismiss()
                }
        }
    }

    override fun onDestroyView()  {
        super.onDestroyView()
        binding = null
        viewModel.sent(CreatePlayListAction.ClearError)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.sent(CreatePlayListAction.ClearError)
    }

    companion object {
        const val TAG = "CreatePlayListFragmentDialog_TAG"
    }
}