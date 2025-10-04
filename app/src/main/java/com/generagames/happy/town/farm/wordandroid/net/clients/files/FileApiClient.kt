package com.generagames.happy.town.farm.wordandroid.net.clients.files

import com.generagames.happy.town.farm.wordandroid.net.models.requests.files.AudioGenerationRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.files.SaveFileRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.UploadRespond

interface FileApiClient {
    suspend fun uploadFile(request: SaveFileRequest): UploadRespond

    suspend fun textToAudioFile(request: AudioGenerationRequest): UploadRespond
}