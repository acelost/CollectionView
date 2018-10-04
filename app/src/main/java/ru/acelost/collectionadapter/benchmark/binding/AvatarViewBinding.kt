package ru.acelost.collectionadapter.benchmark.binding

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.benchmark.IconBackground

class AvatarViewBinding(parent: ViewGroup) : ViewBinding<FrameLayout>(R.layout.view_part_avatar, parent) {

    private val textView = root.findViewById<TextView>(R.id.avatar)

    fun setAvatar(char: String, background: IconBackground) {
        textView.text = char
        textView.setBackgroundResource(background.drawableRes)
    }

}