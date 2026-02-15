package com.igtuapps.chaoscomputerstreams

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.OptIn
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.leanback.LeanbackPlayerAdapter
import com.igtuapps.chaoscomputerstreams.PlaybackActivity.Companion.VIDEO_URL

/** Handles video playback with media controls. */
@UnstableApi
class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<LeanbackPlayerAdapter>
    private lateinit var player: ExoPlayer

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoUrl = arguments?.getString(VIDEO_URL)
        Log.d(TAG, "Video URL: $videoUrl")

        player = ExoPlayer.Builder(requireContext()).build()
        val playerAdapter = LeanbackPlayerAdapter(requireContext(), player, 1000)

        // Add a listener for debugging
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                Log.d(TAG, "Playback state changed: $playbackState")
            }

            override fun onPlayerError(error: PlaybackException) {
                Log.e(TAG, "Player error", error)
            }
        })

        player.repeatMode = Player.REPEAT_MODE_ALL

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)

        mTransportControlGlue = PlaybackTransportControlGlue(requireActivity(), playerAdapter)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.playWhenPrepared()

        // Create a DataSource.Factory with a user agent
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("ChaosComputerStreams/1.0")

        val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoUrl!!))

        player.setMediaSource(hlsMediaSource)
        player.prepare()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.keepScreenOn = true
    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    companion object {
        private const val TAG = "PlaybackVideoFragment"
    }
}