package com.generagames.happy.town.farm.wordandroid.domain.managers.media

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import can.lucky.of.core.domain.factories.GlideHeaderFactory
import can.lucky.of.core.domain.managers.media.MediaManager
import java.io.File


class ExoCacheMediaManager(
    private val context: Context,
    private val headerFactory: GlideHeaderFactory
) : MediaManager {

    private val player: ExoPlayer = ExoPlayer.Builder(context)
        .build()
    private val cacheDir = File(context.cacheDir, "media")
    @UnstableApi
    private val cacheEvictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024) // 100 MB
    @UnstableApi
    private val simpleCache = SimpleCache(cacheDir, cacheEvictor)


    init {
        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Log.e("ExoPlayerError", "Error: ${error.message}")
            }
        })
    }


    @OptIn(UnstableApi::class)
    override fun load(url: String, playWhenReady: Boolean) {
        val dataSourceFactory = DefaultHttpDataSource.Factory().apply {
            setDefaultRequestProperties(headerFactory.createHeaders().headers)
        }

        val cacheDataSourceFactory = CacheDataSource.Factory().apply {
            setCache(simpleCache)
            setUpstreamDataSourceFactory(dataSourceFactory)
            setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }

        val mediaItem = MediaItem.fromUri(url)

        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)
        player.playWhenReady = playWhenReady
        player.prepare()

    }

    override val isPlaying: Boolean
        get() = player.isPlaying

    override fun start() {
        if (player.isPlaying.not()) {
            player.play()
        }
    }

    override fun stop() {
        if (player.isPlaying) {
            player.stop()
        }
    }

    override fun close() {
        player.release()
    }
}