package ru.acelost.collectionadapter.benchmark_vm_adapter

import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.ViewModelAdapter
import ru.acelost.collectionadapter.benchmark.BenchmarkActivity
import ru.acelost.collectionadapter.benchmark.Person
import java.util.*

class VmAdapterActivity : BenchmarkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benchmark_recycler_view_vm)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = object : ViewModelAdapter() {
            init {
                cell<List<Any>>(R.layout.item_with_recycler_view_vm)
            }
        }.apply {
            pool = recyclerView.recycledViewPool
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
fun setContent(view: RecyclerView, content: List<Any>) {
    val adapter = view.adapter as? ViewModelAdapter
    adapter?.reload(content)
}

@BindingAdapter("background")
fun setBackground(view: View, content: Int) {
    view.setBackgroundResource(content)
}

@BindingAdapter("imageResource")
fun setImageResource(view: ImageView, content: Int) {
    view.setImageResource(content)
}

@BindingAdapter("pool")
fun setImageResource(view: RecyclerView, pool: RecyclerView.RecycledViewPool) {
    if (view.recycledViewPool == pool) {
        return
    }
    view.setRecycledViewPool(pool)
}