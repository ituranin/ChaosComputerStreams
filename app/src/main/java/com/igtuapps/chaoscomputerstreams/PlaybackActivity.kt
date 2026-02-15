package com.igtuapps.chaoscomputerstreams

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.util.UnstableApi

/** Loads [PlaybackVideoFragment]. */
class PlaybackActivity : FragmentActivity() {

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fragment = PlaybackVideoFragment().apply {
                arguments = Bundle().apply {
                    putString(VIDEO_URL, intent.getStringExtra(VIDEO_URL))
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit()
        }
    }

    companion object {
        const val VIDEO_URL = "video_url"
    }
}