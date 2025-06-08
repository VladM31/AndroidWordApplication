package com.generagames.happy.town.farm.wordandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.utils.setHost
import com.generagames.happy.town.farm.wordandroid.R
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get

class StartFragment : Fragment(can.lucky.of.core.R.layout.fragment_loading) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO){
            Firebase.remoteConfig.reset().await()
            Firebase.remoteConfig.fetchAndActivate().await()

            Firebase.remoteConfig.getString("local_host").let {
                setHost(it)
            }

            get<SubscribeCacheManager>().fetch()

            withContext(Dispatchers.Main){
                findNavController(). navigate(
                    StartFragmentDirections.actionStartFragmentToLoginFragment(),
                    NavOptions.Builder().setPopUpTo(R.id.startFragment, true).build())
            }
        }


    }
}