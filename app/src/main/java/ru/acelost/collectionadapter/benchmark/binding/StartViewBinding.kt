package ru.acelost.collectionadapter.benchmark.binding

import android.view.ViewGroup
import android.widget.ImageView
import ru.acelost.collectionadapter.R

class StartViewBinding(parent: ViewGroup) : ViewBinding<ViewGroup>(R.layout.view_part_star, parent) {

    private val image = root.findViewById<ImageView>(R.id.star_view)

    fun setStarState(state: Int) {
        image.setImageResource(
                when (state) {
                    0 -> R.drawable.star_outline
                    1 -> R.drawable.star_half
                    else -> R.drawable.star
                }
        )
    }

}