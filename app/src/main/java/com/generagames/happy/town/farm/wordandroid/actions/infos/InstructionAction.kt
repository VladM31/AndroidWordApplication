package com.generagames.happy.town.farm.wordandroid.actions.infos

interface InstructionAction {

    data object End : InstructionAction

    data class Error(val message: String) : InstructionAction
}