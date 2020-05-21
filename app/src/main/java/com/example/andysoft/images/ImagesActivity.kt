package com.example.andysoft.images

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.andysoft.R
import com.example.andysoft.view.AddNewBookActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImagesActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    val PICK_IMAGES = 2
    val STORAGE_PERMISSION = 100

    var imageList: ArrayList<ImageModel>? = null
    var selectedImageList: ArrayList<String>? = null
    var imageRecyclerView: RecyclerView? = null
    var selectedImageRecyclerView: RecyclerView? = null
    var resImg =
        intArrayOf(R.drawable.ic_camera_white_30dp, R.drawable.ic_folder_white_30dp)
    var title = arrayOf("Camera", "Folder")
    var mCurrentPhotoPath: String = ""
    var selectedImageAdapter: SelectedImageAdapter? = null
    var imageAdapter: ImageAdapter? = null
    var projection = arrayOf(
        MediaStore.MediaColumns.DATA
    )
    var image: File? = null
    var done: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)

        if (isStoragePermissionGranted()) {
            init()
            getAllImages()
            setImageList()
            setSelectedImageList()
        }
    }

    fun init() {
        imageRecyclerView = findViewById(R.id.recycler_view)
        selectedImageRecyclerView = findViewById(R.id.selected_recycler_view)
        done = findViewById(R.id.done)
        selectedImageList = ArrayList()
        imageList = ArrayList()
        done!!.setOnClickListener(View.OnClickListener {
            for (i in selectedImageList!!.indices) {

                var intent = Intent(this, AddNewBookActivity::class.java)
                intent.putExtra("selectedImageList", selectedImageList)
                startActivity(intent)
                finish()

            }
        })
    }

    fun setImageList() {
        imageRecyclerView!!.layoutManager = GridLayoutManager(applicationContext, 4)
        imageAdapter = ImageAdapter(applicationContext, imageList!!)
        imageRecyclerView!!.adapter = imageAdapter
        imageAdapter!!.setOnItemClickListener(object : ImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, v: View?) {
                if (position == 0) {
                    takePicture()
                } else if (position == 1) {
                    getPickImageIntent()
                } else {
                    try {
                        if (!imageList!![position].isSelected) {
                            selectImage(position)
                        } else {
                            unSelectImage(position)
                        }
                    } catch (ed: ArrayIndexOutOfBoundsException) {
                        ed.printStackTrace()
                    }
                }
            }
        })
        setImagePickerList()
    }

    fun setSelectedImageList() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        selectedImageRecyclerView!!.layoutManager = layoutManager
        selectedImageAdapter = SelectedImageAdapter(this, selectedImageList!!)
        selectedImageRecyclerView!!.adapter = selectedImageAdapter
    }

    // Add Camera and Folder in ArrayList
    fun setImagePickerList() {
        for (i in resImg.indices) {
            val imageModel = ImageModel()
            imageModel.resImg = (resImg[i])
            imageModel.title = (title[i])
            imageList!!.add(i, imageModel)
        }
        imageAdapter!!.notifyDataSetChanged()
    }

    // get all images from external storage
    fun getAllImages() {
        imageList!!.clear()
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            val absolutePathOfImage =
                cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
            val ImageModel = ImageModel()
            ImageModel.image = (absolutePathOfImage)
            imageList!!.add(ImageModel)
        }
        cursor.close()
    }


    // start the image capture Intent
    fun takePicture() {
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Continue only if the File was successfully created;
        val photoFile = createImageFile()
        if (photoFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    fun getPickImageIntent() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, PICK_IMAGES)
    }

    // Add image in selectedImageList
    fun selectImage(position: Int) {
        // Check before add new item in ArrayList;
        if (!selectedImageList!!.contains(imageList!![position].image)) {
            imageList!![position].isSelected = (true)
            selectedImageList!!.add(0, imageList!![position].image)
            selectedImageAdapter!!.notifyDataSetChanged()
            imageAdapter!!.notifyDataSetChanged()
        }
    }

    fun unSelectImage(position: Int) {
        for (i in selectedImageList!!.indices) {
            if (imageList!!.get(position).image != null) {

                if (selectedImageList!![i] == imageList!!.get(position).image) {
                    imageList!![position].isSelected = (false)
                    selectedImageList!!.removeAt(i)
                    selectedImageAdapter!!.notifyDataSetChanged()
                    imageAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }

    fun createImageFile(): File? {
        // Create an image file name
        val dateTime =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMG_" + dateTime + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image!!.absolutePath
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    addImage(mCurrentPhotoPath)
                }
            } else if (requestCode == PICK_IMAGES) {
                if (data!!.clipData != null) {
                    val mClipData = data.clipData
                    for (i in 0 until mClipData!!.itemCount) {
                        val item = mClipData.getItemAt(i)
                        val uri = item.uri
                        getImageFilePath(uri)
                    }
                } else if (data.data != null) {
                    val uri = data.data
                    getImageFilePath(uri)
                }
            }
        }
    }


    // Get image file path
    fun getImageFilePath(uri: Uri?) {
        val cursor =
            contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val absolutePathOfImage =
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
                if (absolutePathOfImage != null) {
                    checkImage(absolutePathOfImage)
                } else {
                    checkImage(uri.toString())
                }
            }
        }
    }

    fun checkImage(filePath: String?) {
        // Check before adding a new image to ArrayList to avoid duplicate images
        if (!selectedImageList!!.contains(filePath!!)) {
            for (pos in imageList!!.indices) {
                if (imageList!![pos].image != null) {
                    if (imageList!![pos].image.equals(filePath)) {
                        imageList!!.removeAt(pos)
                    }
                }
            }
            addImage(filePath)
        }
    }

    // add image in selectedImageList and imageList
    fun addImage(filePath: String) {
        val imageModel = ImageModel()
        imageModel.image = filePath
        imageModel.isSelected = (true)
        imageList!!.add(2, imageModel)
        selectedImageList!!.add(0, filePath!!)
        selectedImageAdapter!!.notifyDataSetChanged()
        imageAdapter!!.notifyDataSetChanged()
    }

    fun isStoragePermissionGranted(): Boolean {
        val ACCESS_EXTERNAL_STORAGE: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (ACCESS_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == STORAGE_PERMISSION && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            init()
            getAllImages()
            setImageList()
            setSelectedImageList()
        }
    }


}
