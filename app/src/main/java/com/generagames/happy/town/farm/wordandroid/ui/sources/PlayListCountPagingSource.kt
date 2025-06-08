package com.generagames.happy.town.farm.wordandroid.ui.sources

import can.lucky.of.core.domain.models.data.playlists.PlayListCount
import can.lucky.of.core.ui.sources.AbstractPagingSource
import can.lucky.of.core.ui.sources.TemplatePageLoader

typealias PlayListCountLoader = TemplatePageLoader<PlayListCount>


class PlayListCountPagingSource(
    pageLoader: PlayListCountLoader,
    pageSize: Int
) : AbstractPagingSource<PlayListCount>(pageLoader,pageSize)