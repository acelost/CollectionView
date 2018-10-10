package ru.acelost.collectionadapter.benchmark_collection_vm_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import ru.acelost.collectionadapter.BR
import ru.acelost.collectionadapter.adapter.CollectionView
import java.lang.IllegalStateException
import java.util.*

open class CollectionViewModelAdapter : CollectionView.Adapter<CollectionViewHolder>() {

    protected val items = LinkedList<Any>()

    val cellMap = LinkedHashMap<Class<out Any>, CellInfo>()

    // Public functions:
    @JvmOverloads
    open fun reload(newItems: List<Any>, refreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? = null) {
        /*val diffCallback = DiffCallBack(
                items,
                newItems,
                checkAreItemsTheSame = { oldItem: Any, newItem: Any ->
                    getCellInfo(oldItem).checkAreItemsTheSame.invoke(oldItem, newItem)
                },
                checkAreContentsTheSame = { oldItem: Any, newItem: Any ->
                    getCellInfo(oldItem).checkAreContentsTheSame.invoke(oldItem, newItem)
                })
        val diffResult = DiffUtil.calculateDiff(diffCallback)*/
        items.clear()
        items.addAll(newItems)
        /*diffResult.dispatchUpdatesTo(object : androidx.recyclerview.widget.ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
                mCallback?.onItemsInsertedOnTop(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                notifyItemRangeChanged(position, count, payload)
            }
        })*/
        notifyDataChanged()
        refreshLayout?.isRefreshing = false
    }

    @JvmOverloads
    inline fun <reified T : Any> cell(@LayoutRes layoutId: Int,
                                      bindingId: Int = BR.viewModel,
                                      noinline areItemsTheSame: (T, T) -> Boolean = { a: T, b: T -> a == b },
                                      noinline areContentsTheSame: (T, T) -> Boolean = { a: T, b: T -> a == b }
    ) {
        @Suppress("UNCHECKED_CAST")
        val cellInfo = CellInfo(layoutId, bindingId, areItemsTheSame as (Any, Any) -> Boolean, areContentsTheSame as (Any, Any) -> Boolean)
        cellMap[T::class.java] = cellInfo
    }

    protected fun getViewModel(position: Int) = items[position]

    private fun getCellInfo(viewModel: Any): CellInfo {
        // Find info with simple class check:
        cellMap.entries
                .find { it.key == viewModel.javaClass }
                ?.apply { return value }

        // Find info with inheritance class check:
        cellMap.entries
                .find { it.key.isInstance(viewModel) }
                ?.apply {
                    cellMap[viewModel.javaClass] = value
                    return value
                }

        throw Exception("Cell info for class ${viewModel.javaClass.name} not found.")
    }

    private fun onBind(binding: ViewDataBinding,
                       cellInfo: CellInfo,
                       position: Int) {

        val viewModel = getViewModel(position)
        if (cellInfo.bindingId != 0 ) {
            binding.setVariable(cellInfo.bindingId, viewModel)
            binding.executePendingBindings()
        }
    }

    // RecyclerView.Adapter
    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return getCellInfo(getViewModel(position)).layoutId
    }

    override fun onCreateViewHolder(parent: CollectionView, viewType: Int): CollectionViewHolder {
        if (parent is ViewGroup) {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(viewType, parent, false)

            return CollectionViewHolder(view.rootView)
        }
        throw IllegalStateException()
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val cellInfo = getCellInfo(getViewModel(position))
        onBind(holder.binding, cellInfo, position)
    }

    @Suppress("unused")
    fun getViewModelType(itemPosition: Int): Class<out Any> {
        return items[itemPosition]::class.java
    }

    override fun getStashSize(): Int {
        return 15
    }

}

data class CellInfo(val layoutId: Int,
                    val bindingId: Int,
                    val checkAreItemsTheSame: (Any, Any) -> Boolean,
                    val checkAreContentsTheSame: (Any, Any) -> Boolean)

open class CollectionViewHolder(view: View) : CollectionView.ViewHolder(view) {

    val binding: ViewDataBinding = DataBindingUtil.bind(view)!!

}