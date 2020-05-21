package com.example.andysoft.view

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.andysoft.R
import com.example.andysoft.db.AppDb
import com.example.andysoft.db.Book
import com.example.andysoft.images.ImagesActivity
import java.util.*
import kotlin.collections.ArrayList


class AddNewBookActivity : AppCompatActivity() {

    private var edt_book_name: EditText? = null
    private var edt_price: EditText? = null
    private var edt_date: EditText? = null
    private var btn_submit: Button? = null
    private var btn_images: Button? = null
    private var spn_author_name: Spinner? = null
    private var str_author_name = ""

    var selectedImageList: ArrayList<String>? = ArrayList()


    // DB :
    private var db: AppDb? = null
    var cal = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_book)

        edt_book_name = findViewById(R.id.edt_book_name)
        edt_price = findViewById(R.id.edt_price)
        edt_date = findViewById(R.id.edt_date)
        btn_submit = findViewById(R.id.btn_submit)
        btn_images = findViewById(R.id.btn_images)

        if (intent.getStringArrayListExtra("selectedImageList") != null)
        {
            selectedImageList = intent.getStringArrayListExtra("selectedImageList")

            for (i in selectedImageList!!.indices) {

                Log.e("Hiiiiiii",selectedImageList!![i])
                Log.e("selectedImageList",selectedImageList.toString())

            }
        }


        db = AppDb.getInstance(applicationContext)


        btn_submit!!.setOnClickListener { validation() }
        btn_images!!.setOnClickListener {

            var intent = Intent(this,ImagesActivity::class.java)
            startActivity(intent)

        }


        // create an OnDateSetListener


        edt_date!!.setOnClickListener {

            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    edt_date!!.setText("" + dayOfMonth + "/" + month + "/" + year)
                },
                year,
                month,
                day
            )
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            dpd.show()

        }

        // access the items of the list
        val authors = resources.getStringArray(R.array.AuthorNames)

        // access the spinner
        spn_author_name = findViewById<Spinner>(R.id.spn_author_name)

        if (spn_author_name != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, authors
            )
            spn_author_name!!.adapter = adapter

            spn_author_name!!.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    str_author_name = authors[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

    }


    fun validation() {

        var str_book_name = edt_book_name!!.text.toString()
        var str_price = edt_price!!.text.toString()
        var str_date = edt_date!!.text.toString()

        Log.e("Hiiii", str_author_name + " " + str_book_name + " " + str_price + " " + str_date)

        if (str_book_name.trim().equals("")) {
            Toast.makeText(this, "Enter book name", Toast.LENGTH_LONG).show()
        } else if (str_price.trim().equals("")) {
            Toast.makeText(this, "Enter price", Toast.LENGTH_LONG).show()
        } else if (str_date.trim().equals("")) {
            Toast.makeText(this, "Enter date", Toast.LENGTH_LONG).show()
        } else if (str_author_name.trim().equals("")) {
            Toast.makeText(this, "Select author", Toast.LENGTH_LONG).show()
        } else {



            val thread = Thread {
                // Toast.makeText(this, "Nice", Toast.LENGTH_LONG).show()



                var book = Book(null, str_book_name, str_author_name, str_price, str_date,selectedImageList)

                db?.bookDao()!!.insert(book)

                //fetch Records
                db?.bookDao()!!.loadAll().forEach()
                {
                    Log.e("Fetch Records", "Id:  : ${it.book_name}")
                    Log.e("Fetch Records", "Name:  : ${it.author_name}")
                }


            }
            thread.start()

            edt_book_name!!.setText("")
            edt_date!!.setText("")
            edt_price!!.setText("")
            spn_author_name!!.adapter = null

            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)


        }
    }


}
