package com.example.andysoft.adapter


import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.andysoft.R
import java.io.File


class ListAdapter(list: ArrayList<String>, context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mList: ArrayList<String>
    private val mContext: Context

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        i: Int
    ): RecyclerView.ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_slider_images, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(
        @NonNull viewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {

        val viewHolder =
            viewHolder as ViewHolder


        val photoUri: Uri = Uri.fromFile(File(mList.get(position)))

        Log.e("listAdapter",photoUri.toString()+"  "+mList.get(position))


        Glide.with(mContext)

            .load(photoUri)
            .placeholder(R.color.codeGray)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(viewHolder.slider_images)


       // viewHolder.slider_images.setImageResource(mList.get(position).toInt())
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var slider_images: ImageView

        init {

            slider_images = itemView.findViewById(R.id.slider_images) as ImageView
        }
    }

    init {
        mList = list
        mContext = context
    }
}