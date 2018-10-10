package ru.acelost.collectionadapter.view

import android.content.Context
import android.util.AttributeSet
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.adapter.CollectionView
import ru.acelost.collectionadapter.benchmark.Person
import ru.acelost.collectionadapter.benchmark_collection_vm_adapter.CollectionViewModelAdapter

class ViewModelCollectionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalCollectionView(context, attrs, defStyleAttr) {

    var pool: CollectionView.RecycledViewPool? = null
        set(value) {
            field = value
            adapter.setRecycledViewPool(value)
        }

    val adapter = object : CollectionViewModelAdapter() {
        init {
            cell<Person>(R.layout.view_part_avatar)
            cell<String>(R.layout.view_part_name)
            cell<Integer>(R.layout.view_part_star)
        }
    }

    fun getRecycledViewPool(): CollectionView.RecycledViewPool {
        return adapter.recycledViewPool
    }

    fun setRecycledViewPool(pool: CollectionView.RecycledViewPool) {
        adapter.setRecycledViewPool(pool)
    }

    init {
        adapter.attachView(this)
    }

}