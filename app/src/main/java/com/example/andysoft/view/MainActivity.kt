package com.example.andysoft.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.andysoft.R
import com.example.andysoft.adapter.MoviesAdapter
import com.example.andysoft.db.AppDb
import com.example.andysoft.db.Book

class MainActivity : AppCompatActivity() {


    var btn_addNew: Button? = null
    var rv_books: RecyclerView? = null
    private val TAG = "MainActivity"
    var adapter: MoviesAdapter? = null

    // DB :
    private var db: AppDb? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDb.getInstance(applicationContext)

        rv_books = findViewById(R.id.rv_books)
        btn_addNew = findViewById(R.id.btn_addNew)

        btn_addNew!!.setOnClickListener {

            var intent = Intent(this,AddNewBookActivity::class.java)
            intent.putExtra("selectedImageList","")
            startActivity(intent)
        }


        val thread = Thread {


            rv_books!!.setLayoutManager(LinearLayoutManager(this))

            adapter = MoviesAdapter(db?.bookDao()!!.loadAll(), this@MainActivity)
            rv_books!!.setAdapter(adapter)

        }
        thread.start()
    }

}
