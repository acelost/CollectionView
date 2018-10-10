package ru.acelost.collectionadapter.view

import android.content.Context
import androidx.appcompat.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import ru.acelost.collectionadapter.adapter.CollectionView

open class HorizontalCollectionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), CollectionView {

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    override fun addChildInLayout(view: View, position: Int) {
        addViewInLayout(view, position, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT), true)
    }

}