package ru.acelost.collectionadapter.benchmark.binding

import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

open class ViewBinding<out ROOT : View>(@LayoutRes layoutRes: Int, parent: ViewGroup) {

    val root: ROOT = LayoutInflater.from(parent.context)
            .inflate(layoutRes, parent, false) as ROOT

}