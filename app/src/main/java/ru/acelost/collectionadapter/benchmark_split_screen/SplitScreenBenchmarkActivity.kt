package ru.acelost.collectionadapter.benchmark_split_screen

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.benchmark.BenchmarkActivity
import ru.acelost.collectionadapter.benchmark.InfiniteRecyclerAdapter

class SplitScreenBenchmarkActivity : BenchmarkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benchmark_split_screen)

        val recycler1 = findViewById<RecyclerView>(R.id.recycler_view_1)
        recycler1.layoutManager = LinearLayoutManager(this)
        recycler1.adapter = InfiniteRecyclerAdapter(InfiniteRecyclerAdapter.ViewType.RECYCLER_VIEW)

        val recycler2 = findViewById<RecyclerView>(R.id.recycler_view_2)
        recycler2.layoutManager = LinearLayoutManager(this)
        recycler2.adapter = InfiniteRecyclerAdapter(InfiniteRecyclerAdapter.ViewType.COLLECTION_VIEW)
    }

}