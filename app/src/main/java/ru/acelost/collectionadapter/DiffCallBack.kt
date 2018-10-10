package ru.acelost.collectionadapter

import androidx.recyclerview.widget.DiffUtil

class DiffCallBack(private val oldList: List<Any>,
                   private val newList: List<Any>,
                   private val checkAreItemsTheSame: (Any, Any) -> Boolean,
                   private val checkAreContentsTheSame: (Any, Any) -> Boolean) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int) : Boolean  {
            val old = oldList[oldPos]
            val new = newList[newPos]
        if (old::class != new::class) {
            return false
        }
        return checkAreItemsTheSame(old, new)
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int) = checkAreContentsTheSame(oldList[oldPos], newList[newPos])
}