package ru.acelost.collectionadapter

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import ru.acelost.collectionadapter.benchmark_collection_view.CollectionViewBenchmarkActivity
import ru.acelost.collectionadapter.benchmark_recycler_view.RecyclerViewBenchmarkActivity
import ru.acelost.collectionadapter.benchmark_split_screen.SplitScreenBenchmarkActivity

class RouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router)
        findViewById<Button>(R.id.button_benchmark_recycler_view)
                .setOnClickListener { startActivity(Intent(this, RecyclerViewBenchmarkActivity::class.java)) }
        findViewById<Button>(R.id.button_benchmark_collection_view)
                .setOnClickListener { startActivity(Intent(this, CollectionViewBenchmarkActivity::class.java)) }
        findViewById<Button>(R.id.button_benchmark_recycler_vs_collection)
                .setOnClickListener { startActivity(Intent(this, SplitScreenBenchmarkActivity::class.java)) }
    }

}