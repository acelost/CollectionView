package ru.acelost.collectionadapter.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import ru.acelost.collectionadapter.measurement.Measurement

class MeasuredRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val start = System.currentTimeMillis()
        super.onMeasure(widthSpec, heightSpec)
        val duration = System.currentTimeMillis() - start
        Measurement.getInstance().increment("RecyclerMeasure", duration.toInt())
        Measurement.getInstance().printCounters()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val start = System.currentTimeMillis()
        super.onLayout(changed, l, t, r, b)
        val duration = System.currentTimeMillis() - start
        Measurement.getInstance().increment("RecyclerLayout", duration.toInt())
    }

}