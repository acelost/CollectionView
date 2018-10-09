package ru.acelost.collectionadapter.benchmark_vm_adapter

import android.util.Log
import ru.acelost.collectionadapter.R
import ru.acelost.collectionadapter.ViewModelAdapter
import ru.acelost.collectionadapter.benchmark.Person

class CustomAdapter: ViewModelAdapter() {

    init {
        Log.d("CustomAdapterInit", "CustomAdapterInit")
        cell<Person>(R.layout.view_part_avatar)
        cell<String>(R.layout.view_part_name)
        cell<Integer>(R.layout.view_part_star)
    }

}