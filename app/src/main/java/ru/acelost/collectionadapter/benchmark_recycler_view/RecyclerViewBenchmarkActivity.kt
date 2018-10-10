package ru.acelost.collectionadapter.benchmark_recycler_view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.benchmark.BenchmarkActivity
import ru.acelost.collectionadapter.benchmark.InfiniteRecyclerAdapter

class RecyclerViewBenchmarkActivity : BenchmarkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benchmark_recycler_view)

        val recycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler.adapter = InfiniteRecyclerAdapter(InfiniteRecyclerAdapter.ViewType.RECYCLER_VIEW)
    }

}