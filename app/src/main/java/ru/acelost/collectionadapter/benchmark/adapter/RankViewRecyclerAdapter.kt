package ru.acelost.collectionadapter.benchmark.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ru.acelost.collectionadapter.benchmark.Rank
import ru.acelost.collectionadapter.benchmark.binding.ViewBinding

class RankViewRecyclerAdapter : RecyclerView.Adapter<RankViewRecyclerAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewBinding<View>) : RecyclerView.ViewHolder(binding.root)

    private val origin = RankViewAdapter()

    fun setModel(rank: Rank) {
        origin.setModel(rank)
        notifyDataSetChanged()
    }

    override fun getItemCount() = origin.getItemCount()

    override fun getItemViewType(position: Int) = origin.getViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = origin.onCreateView(parent, viewType)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        origin.onBindView(holder.binding, position)
    }

}