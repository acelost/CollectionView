package ru.acelost.collectionadapter.benchmark_collection_view

import android.os.Bundle
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.benchmark.BenchmarkActivity
import ru.acelost.collectionadapter.benchmark.InfiniteRecyclerAdapter

class CollectionViewBenchmarkActivity : BenchmarkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benchmark_collection_view)

        val recycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler.adapter = InfiniteRecyclerAdapter(InfiniteRecyclerAdapter.ViewType.COLLECTION_VIEW)
    }

}