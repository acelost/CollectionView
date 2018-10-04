package ru.acelost.collectionadapter.benchmark

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.benchmark.adapter.RankViewCollectionAdapter
import ru.acelost.collectionadapter.benchmark.adapter.RankViewRecyclerAdapter
import ru.acelost.collectionadapter.view.HorizontalCollectionView
import java.lang.IllegalArgumentException

private const val VIEW_TYPE_RECYCLER = 1
private const val VIEW_TYPE_COLLECTION = 2

class InfiniteRecyclerAdapter(private val childType: ViewType) : RecyclerView.Adapter<BaseViewHolder>() {

    enum class ViewType {
        RECYCLER_VIEW,
        COLLECTION_VIEW,
        BOTH
    }

    private lateinit var lastRank: Rank

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    override fun getItemViewType(position: Int): Int {
        return when (childType) {
            ViewType.RECYCLER_VIEW -> VIEW_TYPE_RECYCLER
            ViewType.COLLECTION_VIEW -> VIEW_TYPE_COLLECTION
            else -> if (position % 2 == 0) VIEW_TYPE_RECYCLER else VIEW_TYPE_COLLECTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_RECYCLER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_with_recycler_view, parent, false)
                return RecyclerViewHolder(view)
            }
            VIEW_TYPE_COLLECTION -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_with_collection_view, parent, false)
                return CollectionViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown root type $viewType.")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (position % 2 == 0 || childType != ViewType.BOTH) {
            lastRank = Rank.random(Any().hashCode().toLong())
        }
        holder.setModel(lastRank)
    }

}

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun setModel(rank: Rank)

}

class RecyclerViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val recycler = itemView.findViewById<RecyclerView>(R.id.recycler_view)
    private val adapter = RankViewRecyclerAdapter()

    init {
        recycler.layoutManager = object : LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false) {
            override fun canScrollHorizontally() = false
        }
        recycler.adapter = adapter
        recycler.recycledViewPool.setMaxRecycledViews(0, 0)
        recycler.setItemViewCacheSize(0)
    }

    override fun setModel(rank: Rank) {
        adapter.setModel(rank)
    }

}

class CollectionViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val collection = itemView.findViewById<HorizontalCollectionView>(R.id.collection_view)
    private val adapter = RankViewCollectionAdapter(collection)

    init {
        adapter.attachView(collection)
    }

    override fun setModel(rank: Rank) {
        adapter.setModel(rank)
    }

}