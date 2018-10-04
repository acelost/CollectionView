package ru.acelost.collectionadapter.benchmark.adapter

import android.view.View
import android.view.ViewGroup
import ru.acelost.collectionadapter.adapter.CollectionView
import ru.acelost.collectionadapter.benchmark.Rank
import ru.acelost.collectionadapter.benchmark.binding.ViewBinding

class RankViewCollectionAdapter(private val parent: ViewGroup) : CollectionView.Adapter<RankViewCollectionAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewBinding<View>) : CollectionView.ViewHolder(binding.root)

    private val origin = RankViewAdapter()

    fun setModel(rank: Rank) {
        origin.setModel(rank)
        notifyDataChanged()
    }

    override fun getItemCount() = origin.getItemCount()

    override fun getItemViewType(position: Int) = origin.getViewType(position)

    override fun onCreateViewHolder(parent: CollectionView, viewType: Int): ViewHolder {
        val binding = origin.onCreateView(this.parent, viewType)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        origin.onBindView(holder.binding, position)
    }

}