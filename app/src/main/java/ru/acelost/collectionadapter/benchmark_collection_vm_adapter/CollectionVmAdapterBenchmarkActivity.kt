package ru.acelost.collectionadapter.benchmark_collection_vm_adapter

import android.os.Bundle
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.ViewModelAdapter
import ru.acelost.collectionadapter.adapter.CollectionView
import ru.acelost.collectionadapter.benchmark.BenchmarkActivity
import ru.acelost.collectionadapter.benchmark.Person
import ru.acelost.collectionadapter.view.ViewModelCollectionView
import java.util.*

class CollectionVmAdapterBenchmarkActivity : BenchmarkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benchmark_collection_view_vm)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = object : ViewModelAdapter() {
            init {
                cell<List<Any>>(R.layout.item_with_collection_view_vm)
            }
        }.apply {
            collectionPool = CollectionView.RecycledViewPool()
            val list = LinkedList<Any>()
            for (i in 1..1000) {
                val person = Person.random(Random().nextLong())
                list.add(LinkedList<Any>().apply {
                    add(person)
                    add(i.toString() + " " + person.firstName)
                    person.middleName?.apply { add(person.middleName) }
                    person.lastName?.apply { add(person.lastName) }

                    for (j in 1..Random().nextInt(10)) {
                        add(when (Random().nextInt(2)) {
                            0 -> R.drawable.star_outline
                            1 -> R.drawable.star_half
                            else -> R.drawable.star
                        })
                    }
                })
            }
            reload(list)
        }
    }

}

@BindingAdapter("list")
fun setContent(view: ViewModelCollectionView, content: List<Any>) {
    view.adapter.reload(content)
}

@BindingAdapter("pool")
fun setImageResource(view: ViewModelCollectionView, pool: CollectionView.RecycledViewPool) {
    if (view.pool == pool) {
        return
    }
    view.pool = pool
}