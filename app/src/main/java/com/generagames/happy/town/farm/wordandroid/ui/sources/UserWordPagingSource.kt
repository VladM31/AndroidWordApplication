package com.generagames.happy.town.farm.wordandroid.ui.sources

import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.core.ui.sources.AbstractPagingSource
import can.lucky.of.core.ui.sources.TemplatePageLoader

typealias UserWordsPageLoader = TemplatePageLoader<UserWord>


class UserWordPagingSource(
    pageLoader: UserWordsPageLoader,
    pageSize: Int
) : AbstractPagingSource<UserWord>(pageLoader,pageSize)