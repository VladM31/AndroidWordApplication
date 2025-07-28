package com.generagames.happy.town.farm.wordandroid.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentMenuBinding
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentUpdateVersionBinding
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig


class UpdateVersionFragment : Fragment(R.layout.fragment_update_version) {
    private var binding: FragmentUpdateVersionBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentUpdateVersionBinding.bind(view)
        binding = newBinding

        val currentVersion = getString(R.string.current_version)
        val remoteVersion = Firebase.remoteConfig.getString("version")

        "${newBinding.updateText.text}. Your current version: $currentVersion, new version: $remoteVersion".also {
            newBinding.updateText.text = it
        }

        val updateLink = Firebase.remoteConfig.getString("update_link")

        newBinding.updateButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateLink))
            startActivity(intent)
        }

        if (updateLink.isBlank()){
            findNavController().navigate(
                UpdateVersionFragmentDirections.actionUpdateVersionFragmentToLoginFragment(),
                NavOptions.Builder().setPopUpTo(R.id.updateVersionFragment, true).build(),
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}