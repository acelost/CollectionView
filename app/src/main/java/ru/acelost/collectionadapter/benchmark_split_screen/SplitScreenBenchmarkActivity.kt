package ru.acelost.collectionadapter.benchmark_split_screen

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.benchmark.BenchmarkActivity
import ru.acelost.collectionadapter.benchmark.InfiniteRecyclerAdapter

class SplitScreenBenchmarkActivity : BenchmarkActivity() {

    private lateinit var span40: Any
    private lateinit var span30: Any
    private lateinit var span20: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        span40 = ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.holo_orange_light))
        span30 = ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
        span20 = ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benchmark_split_screen)
        val recycler1 = findViewById<RecyclerView>(R.id.recycler_view_1)
        recycler1.layoutManager = LinearLayoutManager(this)
        recycler1.adapter = InfiniteRecyclerAdapter(InfiniteRecyclerAdapter.ViewType.RECYCLER_VIEW)

        val recycler2 = findViewById<RecyclerView>(R.id.recycler_view_2)
        recycler2.layoutManager = LinearLayoutManager(this)
        recycler2.adapter = InfiniteRecyclerAdapter(InfiniteRecyclerAdapter.ViewType.COLLECTION_VIEW)
    }

    override fun onFpsChanged(fps: Long) {
        super.onFpsChanged(fps)
        val title = SpannableString("$fps fps")
        val span = when {
            fps <= 20 -> span20
            fps <= 30 -> span30
            fps <= 40 -> span40
            else -> null
        }

        if (span != null) {
            title.setSpan(span, 0, title.length, 0)
        }
        supportActionBar?.title = title
    }

}