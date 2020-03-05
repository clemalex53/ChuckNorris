package com.example.chucknorris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Display the list of jokes in the Logcat
        Log.d("list", JokeList.jokes.toString())
        //Manager of RecyclerView
        my_recycler_view.layoutManager = LinearLayoutManager(this)

        val adapter = JokeAdapter()

        my_recycler_view.adapter = adapter
    }
}
