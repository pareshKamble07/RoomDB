package com.example.andysoft.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "book")
data class Book (

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "book_name")
    var book_name: String,

    @ColumnInfo(name = "author_name")
    var author_name: String,

    @ColumnInfo(name = "price")
    var price: String,

    @ColumnInfo(name = "doi")
    var doi: String,

    @ColumnInfo(name = "imagesList")
    var imagesList: ArrayList<String>?
)