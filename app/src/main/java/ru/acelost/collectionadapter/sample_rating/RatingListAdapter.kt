package ru.acelost.collectionadapter.sample_rating

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.acelost.collectionadapter.adapter.CollectionView
import java.util.*

class RatingListAdapter(
        private val context: Context
) : RecyclerView.Adapter<RatingListAdapter.Holder>() {

    class Holder(val view: RatingView) : RecyclerView.ViewHolder(view)

    private val random = Random()
    private val pool = CollectionView.RecycledViewPool().apply {
        setMaxRecycledViews(0, 300)
        setMaxRecycledViews(1, 100)
        setMaxRecycledViews(2, 100)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = RatingView(context)
        view.setRecycledViewPool(pool)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.setRating(Math.abs(random.nextInt() % 20))
    }

}