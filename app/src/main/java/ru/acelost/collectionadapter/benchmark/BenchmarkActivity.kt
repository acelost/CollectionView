package ru.acelost.collectionadapter.benchmark

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log

private const val BENCHMARK_FPS_TAG = "FPS"

abstract class BenchmarkActivity : AppCompatActivity() {

    private lateinit var span40: Any
    private lateinit var span30: Any
    private lateinit var span20: Any

    protected open val fpsErrorThreshold = 20
    protected open val fpsWarningThreshold = 40

    override fun onCreate(savedInstanceState: Bundle?) {
        span40 = ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.holo_orange_light))
        span30 = ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
        span20 = ForegroundColorSpan(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        FPSMeter(this) { _, fps -> onFpsChanged(fps) }
        super.onCreate(savedInstanceState)
    }

    protected open fun onFpsChanged(fps: Long) {
        showFpsInToolbar(fps)
        logFps(fps)
    }

    private fun showFpsInToolbar(fps: Long) {
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

    private fun logFps(fps: Long) {
        val message = "$fps fps"
        if (fps <= fpsErrorThreshold) {
            Log.e(BENCHMARK_FPS_TAG, message)
        } else if (fps <= fpsWarningThreshold) {
            Log.w(BENCHMARK_FPS_TAG, message)
        } else {
            Log.i(BENCHMARK_FPS_TAG, message)
        }
    }

}