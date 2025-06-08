package can.lucky.of.core.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import can.lucky.of.core.R
import can.lucky.of.core.databinding.FragmentLoadingBinding

class LoadingFragment : Fragment(R.layout.fragment_loading) {
    private var binding: FragmentLoadingBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentLoadingBinding.bind(view)
        binding = newBinding

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}