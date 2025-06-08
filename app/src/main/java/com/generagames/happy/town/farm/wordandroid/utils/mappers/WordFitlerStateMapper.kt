package com.generagames.happy.town.farm.wordandroid.utils.mappers

import can.lucky.of.core.domain.models.filters.PageFilter
import can.lucky.of.core.domain.models.filters.WordFilter
import com.generagames.happy.town.farm.wordandroid.domain.models.states.WordFilterState

fun WordFilterState.toWordFilter(): WordFilter {
    return WordFilter(
        original = this.original,
        originalLang = this.originalLang,
        translate = this.translate,
        translateLang = this.translateLang,
        categories = this.categories?.toSet(),
        pagination = PageFilter(
            asc = this.asc,
            sort = this.sortBy
        ),
        cefrs = cefrs
    )
}

fun WordFilter.toWordFilterState(): WordFilterState{
    return WordFilterState(
        asc = this.pagination?.asc ?: false,
        categories = this.categories?.toList(),
        original = this.original,
        originalLang = this.originalLang,
        sortBy = this.pagination?.sort,
        translate = this.translate,
        translateLang = this.translateLang,
        cefrs = this.cefrs
    )
}