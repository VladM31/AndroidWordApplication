package com.generagames.happy.town.farm.wordandroid.utils.mappers

import can.lucky.of.core.domain.models.enums.WordSortBy
import can.lucky.of.core.domain.models.filters.WordFilter
import com.generagames.happy.town.farm.wordandroid.domain.models.states.WordFilterState

fun WordFilterState.toWordFilter(): WordFilter {
    return WordFilter(
        original = this.original,
        translate = this.translate,
        languages = this.originalLang?.let { setOf(it) },
        translateLanguages = this.translateLang?.let { setOf(it) },
        categories = this.categories?.toSet(),
        asc = this.asc,
        sortField = this.sortBy ?: WordSortBy.ORIGIN,
        cefrs = cefrs
    )
}

fun WordFilter.toWordFilterState(): WordFilterState{
    return WordFilterState(
        asc = this.asc,
        categories = this.categories?.toList(),
        original = this.original,
        originalLang = this.languages?.firstOrNull(),
        sortBy = this.sortField,
        translate = this.translate,
        translateLang = this.translateLanguages?.firstOrNull(),
        cefrs = this.cefrs
    )
}