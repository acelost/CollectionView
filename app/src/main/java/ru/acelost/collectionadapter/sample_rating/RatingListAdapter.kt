package ru.acelost.collectionadapter.sample_rating

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class RatingListAdapter(
        private val context: Context
) : RecyclerView.Adapter<RatingListAdapter.Holder>() {

    class Holder(val view: RatingView) : RecyclerView.ViewHolder(view)

    private val mList = mutableListOf<Int>()

    fun setRatings(ratings: List<Int>?) {
        mList.clear()
        if (ratings != null) {
            mList.addAll(ratings)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = RatingView(context)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.setRating(mList[position])
    }

}