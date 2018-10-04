package ru.acelost.collectionadapter.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import ru.acelost.collectionadapter.adapter.CollectionView

open class HorizontalCollectionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), CollectionView {

    init { orientation = HORIZONTAL }

    override fun addChildInLayout(view: View, position: Int) {
        addViewInLayout(view, position, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT), true)
    }

}