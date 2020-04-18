package com.example.chucknorris

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        my_recycler_view.layoutManager = LinearLayoutManager(this)

        val adapter = JokeAdapter()
        my_recycler_view.adapter = adapter

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val button = findViewById<Button>(R.id.load_button)
        button.setOnClickListener()
        {

            progressBar.visibility = View.VISIBLE
            val singleJoke =
                JokeApiServiceFactory
                    .createService()
                    .giveMeAJoke()
                    .repeat(10)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                        onNext = {
                            adapter.listOfJokes = adapter.listOfJokes.plus(it)
                        },
                        onError = {
                            Log.e("JOKE", "error found", it);
                            progressBar.visibility = View.INVISIBLE
                        },
                        onComplete = {
                            progressBar.visibility = View.INVISIBLE
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