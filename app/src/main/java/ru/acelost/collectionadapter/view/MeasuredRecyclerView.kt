package ru.acelost.collectionadapter.view

import android.content.Context
import android.util.AttributeSet
import ru.acelost.collectionadapter.measurement.Measurement

open class MeasuredRecyclerView : androidx.recyclerview.widget.RecyclerView {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

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