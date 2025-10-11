package com.generagames.happy.town.farm.wordandroid.domain.managers.pdf

import java.io.File

interface PolicyManager {

    suspend fun getPolicyFile(): File
}