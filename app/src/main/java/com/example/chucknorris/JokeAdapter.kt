package com.example.chucknorris

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JokeAdapter : RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {

    var listOfJokes: List<String> = JokeList.jokes
        set(newListOfJokes)
    {
        field = newListOfJokes
        notifyDataSetChanged()
    }

    class JokeViewHolder(val joke: TextView) : RecyclerView.ViewHolder(joke)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.layout_view, parent, false) as TextView
        return JokeViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return listOfJokes.size
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.joke.text = listOfJokes[position]
    }


}