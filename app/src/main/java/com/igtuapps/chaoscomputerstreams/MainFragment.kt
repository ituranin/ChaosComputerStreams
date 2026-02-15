package com.igtuapps.chaoscomputerstreams

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.core.content.ContextCompat
import android.util.DisplayMetrics
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.igtuapps.chaoscomputerstreams.network.Room

class MainFragment : BrowseSupportFragment() {

    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        prepareBackgroundManager()
        setupUIElements()
        setupEventListeners()

        viewModel.conferences.observe(viewLifecycleOwner) { state ->
            val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

            when (state) {
                is ConferenceDataState.Success -> {
                    title = getString(R.string.browse_title)
                    val cardPresenter = CardPresenter()

                    for (conference in state.conferences) {
                        for (group in conference.groups) {
                            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
                            for (room in group.rooms) {
                                listRowAdapter.add(room)
                            }
                            if (listRowAdapter.size() > 0) {
                                val header = HeaderItem((conference.slug + group.group).hashCode().toLong(), conference.conference + " - " + group.group)
                                rowsAdapter.add(ListRow(header, listRowAdapter))
                            }
                        }
                    }
                }
                is ConferenceDataState.Error -> {
                    title = state.message
                }
                is ConferenceDataState.Loading -> {
                    title = "Loading..."
                }
            }

            val settingsAdapter = ArrayObjectAdapter(StringPresenter())
            settingsAdapter.add(getString(R.string.refresh))
            val settingsHeader = HeaderItem(getString(R.string.settings))
            rowsAdapter.add(ListRow(settingsHeader, settingsAdapter))
            adapter = rowsAdapter
        }

        viewModel.fetchConferences()
    }

    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(requireActivity().window)
        mDefaultBackground = ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
        mMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        headersState = BrowseSupportFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireContext(), R.color.fastlane_background)
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.search_opaque)
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is Room) {
                val intent = Intent(requireContext(), PlaybackActivity::class.java)
                val stream = item.streams.firstOrNull { it.slug == "hd-native" && it.type == "video" }
                val url = stream?.urls?.get("hls")?.url
                if (url != null) {
                    intent.putExtra(PlaybackActivity.VIDEO_URL, url)
                    startActivity(intent)
                }
            } else if (item is String && item == getString(R.string.refresh)) {
                viewModel.fetchConferences()
            }
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is Room) {
                // For now, we don't have images for the background
                // mBackgroundUri = item.thumb
                // startBackgroundTimer()
                updateBackground(null)
            }
        }
    }

    private fun updateBackground(uri: String?) {
        val width = mMetrics.widthPixels
        val height = mMetrics.heightPixels
        Glide.with(requireContext())
            .load(uri)
            .centerCrop()
            .error(mDefaultBackground)
            .into<SimpleTarget<Drawable>>(
                object : SimpleTarget<Drawable>(width, height) {
                    override fun onResourceReady(
                        drawable: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        mBackgroundManager.drawable = drawable
                    }
                })
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}
