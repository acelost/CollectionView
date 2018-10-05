package ru.acelost.collectionadapter.benchmark_recycler_view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.benchmark.BenchmarkActivity
import ru.acelost.collectionadapter.benchmark.InfiniteRecyclerAdapter

class RecyclerViewBenchmarkActivity : BenchmarkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benchmark_recycler_view)

        val recycler = findViewById<RecyclerView>(R.id.recycler_view)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = InfiniteRecyclerAdapter(InfiniteRecyclerAdapter.ViewType.RECYCLER_VIEW)
    }

}