package ru.acelost.collectionadapter.benchmark

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.fragment.app.FragmentActivity
import android.view.Choreographer

private const val SECOND = 1_000_000_000 // in nanos

class FPSMeter(
        activity: androidx.fragment.app.FragmentActivity,
        private val foregroundOnly: Boolean = true,
        private val action: (frameStartNanos: Long, fps: Long) -> Unit
) : LifecycleObserver {

    private var inForeground = false

    private val frameCallback = object : Choreographer.FrameCallback {
        private var prevFrameNanos = System.nanoTime()
        override fun doFrame(frameTimeNanos: Long) {
            val delta = frameTimeNanos - prevFrameNanos
            prevFrameNanos = frameTimeNanos
            if (!foregroundOnly || inForeground) {
                onFrame(frameTimeNanos, delta)
            }
            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    init {
        activity.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        inForeground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        inForeground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        Choreographer.getInstance().removeFrameCallback(frameCallback)
    }

    private fun onFrame(frameTimeNanos: Long, delta: Long) {
        val fps = SECOND / delta
        action(frameTimeNanos, fps)
    }

}