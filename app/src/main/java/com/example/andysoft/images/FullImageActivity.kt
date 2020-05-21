package com.example.andysoft.images

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.andysoft.R

class FullImageActivity : AppCompatActivity() {

    var myImage: ImageView? = null
    var back: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)


        myImage?.let {
            Glide.with(this)
                .load(intent.getStringExtra("image"))
                .placeholder(R.color.codeGray)
                .into(it)
        }

        back!!.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
    }
}
