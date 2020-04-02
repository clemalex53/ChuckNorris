package com.example.chucknorris

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Display the list of jokes in the Logcat
        Log.d("list", JokeList.jokesString.toString())
        //Manager of RecyclerView
        my_recycler_view.layoutManager = LinearLayoutManager(this)

        val adapter = JokeAdapter()
        my_recycler_view.adapter = adapter

        val compositeDisposable = CompositeDisposable()
        val singleJoke =
            JokeApiServiceFactory
                .createService()
                .giveMeAJoke()
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onError = {Log.e("JOKE", "error found", it)},
                    onSuccess = {Log.d("joke",it.toString())}
                )
        compositeDisposable.add(singleJoke);
        Log.d("joke",singleJoke.toString())
        compositeDisposable.clear();
    }
}