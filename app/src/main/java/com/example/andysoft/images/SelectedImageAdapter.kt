package com.example.andysoft.images

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andysoft.R
import java.util.*

public class SelectedImageAdapter(
    var context: Context,
    var stringArrayList: ArrayList<String>
) :
    RecyclerView.Adapter<SelectedImageAdapter.ViewHolder?>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.selected_image_list, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(stringArrayList[position])
            .placeholder(R.color.codeGray)
            .centerCrop()
            .into(holder.image)
        holder.image.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    FullImageActivity::class.java
                ).putExtra("image", stringArrayList[position])
            )
        }
    }


    override fun getItemCount(): Int {
        return stringArrayList.size
    }

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView

        init {
            image = itemView.findViewById<View>(R.id.image) as ImageView
        }
    }


}