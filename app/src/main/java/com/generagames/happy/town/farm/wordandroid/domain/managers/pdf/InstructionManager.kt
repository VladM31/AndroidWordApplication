package com.generagames.happy.town.farm.wordandroid.domain.managers.pdf

import java.io.File

interface InstructionManager {

    suspend fun getInstructionFile(): File
}