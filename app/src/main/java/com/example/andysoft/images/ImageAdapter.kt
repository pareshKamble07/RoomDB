package com.example.andysoft.images

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.andysoft.R
import java.util.*

class ImageAdapter(
    private val context: Context,
    imageList: ArrayList<ImageModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val imageList: ArrayList<ImageModel>
    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == IMAGE_LIST) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.image_list, parent, false)
            ImageListViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_picker_list, parent, false)
            ImagePickerViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 2) IMAGE_PICKER else IMAGE_LIST
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType === IMAGE_LIST) {
            val viewHolder =
                holder as ImageListViewHolder
            Glide.with(context)

                .load(imageList.get(position).image)
                .placeholder(R.color.codeGray)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(viewHolder.image)

            if (imageList[position].isSelected) {
                viewHolder.checkBox.isChecked = true
            } else {
                viewHolder.checkBox.isChecked = false
            }
        } else {
            val viewHolder =
                holder as ImagePickerViewHolder
            viewHolder.image.setImageResource(imageList[position].resImg)
            viewHolder.title.text = imageList[position].title
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImageListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var checkBox: CheckBox

        init {
            image = itemView.findViewById(R.id.image)
            checkBox = itemView.findViewById(R.id.circle)
            itemView.setOnClickListener { v ->
                onItemClickListener!!.onItemClick(
                    adapterPosition,
                    v
                )
            }
        }
    }

    inner class ImagePickerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var title: TextView

        init {
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            itemView.setOnClickListener { v ->
                onItemClickListener!!.onItemClick(
                    adapterPosition,
                    v
                )
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, v: View?)
    }

    companion object {
        private val onItemClickListener: OnItemClickListener? = null
        private const val IMAGE_LIST = 0
        private const val IMAGE_PICKER = 1
    }

    init {
        this.imageList = imageList
    }
}
