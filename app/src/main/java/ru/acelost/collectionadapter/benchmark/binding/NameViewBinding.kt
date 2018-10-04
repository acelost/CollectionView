package ru.acelost.collectionadapter.benchmark.binding

import android.view.ViewGroup
import android.widget.TextView
import ru.acelost.collectionadapter.R

class NameViewBinding(parent: ViewGroup) : ViewBinding<TextView>(R.layout.view_part_name, parent) {

    fun setName(name: String?) {
        root.text = name
    }

}