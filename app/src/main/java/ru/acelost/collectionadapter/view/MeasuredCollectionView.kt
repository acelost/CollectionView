package ru.acelost.collectionadapter.view

import android.content.Context
import android.util.AttributeSet
import ru.acelost.collectionadapter.measurement.Measurement

class MeasuredCollectionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HorizontalCollectionView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val start = System.currentTimeMillis()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val duration = System.currentTimeMillis() - start
        Measurement.getInstance().increment("CollectionMeasure", duration.toInt())
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val start = System.currentTimeMillis()
        super.onLayout(changed, l, t, r, b)
        val duration = System.currentTimeMillis() - start
        Measurement.getInstance().increment("CollectionLayout", duration.toInt())
    }

}