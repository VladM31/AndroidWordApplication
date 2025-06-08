package com.generagames.happy.town.farm.wordandroid.domain.models.states

data class VersionState<T>(
    val previousState : T? = null,
    val newState : T
){

    fun newVersion(newState: T) : VersionState<T> {
        return copy(
            previousState = this.newState,
            newState = newState
        )
    }

    fun newVersion(factory: (T) -> T) : VersionState<T> {
        return copy(
            previousState = this.newState,
            newState = factory(this.newState)
        )
    }
}
