package com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.playlists

import android.view.View
import androidx.fragment.app.Fragment
import can.lucky.of.core.domain.managers.spotlight.SpotlightManager
import can.lucky.of.core.ui.utils.buildRectangleTarget
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPlayListBinding
import com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists.PlayListFragment
import com.generagames.happy.town.farm.wordandroid.ui.listeners.spotlights.AbstractSpotlightsFragmentListener
import com.takusemba.spotlight.Target

class PlayListSpotlightsFragmentListener(
    spotlightManager: SpotlightManager
) : AbstractSpotlightsFragmentListener<FragmentPlayListBinding>(
    spotlightManager,
    PlayListFragment::class
) {

    override fun getViewBinding(v: View) = FragmentPlayListBinding.bind(v)

    override fun createTargets(
        newBinding: FragmentPlayListBinding,
        fragment: Fragment
    ): List<Target> {


        return listOf(
            fragment.buildRectangleTarget(
                newBinding.addPlayListButton,
                "Add Playlist",
                "Create a new playlist by clicking this button",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.findButton,
                "Search Playlists",
                "Use this button to search for specific playlists",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
            fragment.buildRectangleTarget(
                newBinding.playListContainer,
                "Playlist List",
                "Browse through your playlists here. Click on any playlist to view details.",
                onClick = { spotlight?.next() },
                vertical = can.lucky.of.core.ui.utils.VerticalAlign.BOTTOM
            ),
        )
    }
}