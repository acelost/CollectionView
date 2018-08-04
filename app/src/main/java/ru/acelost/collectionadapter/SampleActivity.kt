package ru.acelost.collectionadapter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ru.acelost.collectionadapter.view.ListView
import ru.acelost.collectionadapter.view.ListViewAdapter

class SampleActivity : AppCompatActivity() {

    private val mocks = listOf(
            listOf(
                    "Красный",
                    "!Синий",
                    "Зеленый"
            ),
            listOf(
                    "00",
                    "01",
                    "!10",
                    "11"
            ),
            listOf(
                    "!Действующие вещества",
                    "Вспомогательные вещества"
            )
    )

    private var mAdapter = ListViewAdapter(this)
    private var mIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        val listView = findViewById<ListView>(R.id.list_view)
        mAdapter.attachView(listView)
        findViewById<Button>(R.id.change_data_button).setOnClickListener {
            val data = mocks[++mIndex % mocks.size]
            mAdapter.setData(data)
        }
    }

}
