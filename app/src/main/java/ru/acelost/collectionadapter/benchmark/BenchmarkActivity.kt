package ru.acelost.collectionadapter.benchmark

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

private const val BENCHMARK_FPS_TAG = "FPS"

abstract class BenchmarkActivity : AppCompatActivity() {

    protected open val fpsErrorThreshold = 20
    protected open val fpsWarningThreshold = 40

    override fun onCreate(savedInstanceState: Bundle?) {
        FPSMeter(this) { _, fps -> onFpsChanged(fps) }
        super.onCreate(savedInstanceState)
    }

    protected open fun onFpsChanged(fps: Long) {
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