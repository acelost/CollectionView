package ru.acelost.collectionadapter.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import ru.acelost.collectionadapter.benchmark_vm_adapter.CustomAdapter

class InnerRecyclerView : MeasuredRecyclerView {

    private val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    constructor(context: Context) : super(context) {
        layoutManager = linearLayoutManager
        adapter = CustomAdapter()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        layoutManager = linearLayoutManager
        adapter = CustomAdapter()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        layoutManager = linearLayoutManager
        adapter = CustomAdapter()
    }
}