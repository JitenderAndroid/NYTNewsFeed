package com.jitenderkumar.newsfeed

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    fun Int.convertToUppercase() {
        this+10
    }

    data class Person(var name: String, var roll: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var sum = "jhf"
        sum.toUpperCase()

        var varet = 101
        varet.convertToUppercase()


       // this add "kbhk"
    }

    var person = Person("sdf", "sdfs")

    var recyclerView: RecyclerView? = null

    fun useWith() {
        with(person) {
            Log.e("hello", ""+name +"and"+roll)
        }
    }

    fun useApply() {
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    fun useRun() {
        recyclerView?.let {
            //Todo this
        } ?: kotlin.run {
            //Todo this
        }
    }

    infix fun add(value: String) = "hjkh"

}
