package com.igtuapps.chaoscomputerstreams

import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.Presenter

class StringPresenter : Presenter() {
    private val cardWidth = 300
    private val cardHeight = 150

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val context = parent.context
        val textView = TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                cardWidth,
                cardHeight
            )
            isFocusable = true
            isFocusableInTouchMode = true
            background = ContextCompat.getDrawable(context, R.drawable.default_background)
            gravity = Gravity.CENTER
        }
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        (viewHolder.view as TextView).text = item as? String
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
    }
}