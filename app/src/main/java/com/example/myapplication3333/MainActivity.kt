package com.example.myapplication3333

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3333.utils.entityes.ImageFolder
import com.example.myapplication3333.utils.MarginDecoration
import com.example.myapplication3333.utils.adapters.PictureFolderAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class MainActivity : AppCompatActivity() {

    val categories = mutableListOf<String>("cats", "dogs", "cars")
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) ActivityCompat.requestPermissions(
            this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
        )
        val empty = findViewById<TextView>(R.id.empty)
        val folderRecycler = findViewById<RecyclerView>(R.id.folderRecycler)
        folderRecycler.addItemDecoration(MarginDecoration(this))
        folderRecycler.hasFixedSize()
        var folds: java.util.ArrayList<ImageFolder>? = null
        lifecycleScope.launch() {
            withContext(Dispatchers.IO) {
                launch {
                    folds = getExtraFolders()
                }.join()
            }
            withContext(Dispatchers.Main) {
                launch {
                    if (folds!!.isEmpty()) {
                        empty.visibility = View.VISIBLE
                    } else {
                        val folderAdapter = PictureFolderAdapter(folds!!, this@MainActivity)
                        folderRecycler.adapter = folderAdapter
                    }
                }.join()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getExtraFolders(): ArrayList<ImageFolder>? {
        return withContext(Dispatchers.IO) {
            val picFolders: ArrayList<ImageFolder> = ArrayList<ImageFolder>()
            launch {
                picFolders.add(getMainFolder())
            }
            launch {
                for (s in categories) {
                    async {
                        println(System.nanoTime())
                        var extraFolder = ImageFolder()
                        extraFolder.FolderName = s
                        if (Files.exists(Paths.get(filesDir.toString() + s))) {
                            for (file in File(filesDir.toString() + s).listFiles()) {
                                extraFolder.addpics()
                                extraFolder.addpath(file.path)
                                extraFolder.firstPic = file.path
                            }
                        } else {
                            File(filesDir.toString() + s).mkdir()
                        }
                        picFolders.add(extraFolder)
                    }
                }
            }.join()
            return@withContext picFolders
        }
    }


    private suspend fun getMainFolder(): ImageFolder {
        val allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID
        )
        val cursor = this.contentResolver.query(allImagesuri, projection, null, null, null)
        val folds = ImageFolder()
        folds.FolderName = "Pictures"
        try {
            cursor?.moveToFirst()
            do {
                val name =
                    cursor!!.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val folder =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val datapath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                folds.addpath(datapath)
                folds.addpics()
                folds.firstPic = datapath
            } while (cursor!!.moveToNext())
            cursor.close()
            return folds
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return folds
    }

}