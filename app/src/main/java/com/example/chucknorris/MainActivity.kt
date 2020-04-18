package com.example.chucknorris

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Display the list of jokes in the Logcat
        Log.d("list", JokeList.jokesString.toString())
        //Manager of RecyclerView
        my_recycler_view.layoutManager = LinearLayoutManager(this)

        val adapter = JokeAdapter()
        my_recycler_view.adapter = adapter

        val button = findViewById<Button>(R.id.load_button)
        button.setOnClickListener()
        {
            val singleJoke =
                JokeApiServiceFactory
                    .createService()
                    .giveMeAJoke()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                        onError = { Log.e("JOKE", "error found", it) },
                        onSuccess = {
                            adapter.listOfJokes = adapter.listOfJokes.plus(it);
                            Log.d("list", JokeList.jokesString.toString())
                        }
                    )

            compositeDisposable.add(singleJoke);
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear();
    }
}