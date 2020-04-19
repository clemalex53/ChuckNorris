package com.example.chucknorris

import android.R.string
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.serializer
import org.xmlpull.v1.XmlSerializer
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private val adapter = JokeAdapter()
    private val serializer: KSerializer<List<Joke>> = Joke.serializer().list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        my_recycler_view.layoutManager = LinearLayoutManager(this)
        my_recycler_view.adapter = adapter

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        fun newJokes(n:Long){
            progressBar.visibility = View.VISIBLE
            val singleJoke =
                JokeApiServiceFactory
                    .createService()
                    .giveMeAJoke()
                    .repeat(n)
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

        if(savedInstanceState != null){
            adapter.listOfJokes =
                Json.parse(
                    serializer,
                    savedInstanceState.getString("keyListOfJokes").toString()
                )
        }else{
            newJokes(10)
        }

        my_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    newJokes(3);
                }
            }
        })
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString(
            "keyListOfJokes",
            Json.stringify(serializer,adapter.listOfJokes)
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear();
    }
}