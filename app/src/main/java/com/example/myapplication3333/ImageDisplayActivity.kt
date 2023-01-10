package com.example.myapplication3333

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3333.srvices.NeurelNetService
import com.example.myapplication3333.utils.MarginDecoration
import com.example.myapplication3333.utils.adapters.PicturesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class ImageDisplayActivity : AppCompatActivity() {

    lateinit var folder: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_display)
        folder = intent.getStringExtra("folderName")!!
        val folderName = findViewById<TextView>(R.id.foldername)
        folderName.setText(folder)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        var imgs = intent.getStringArrayListExtra("imgs")
        var imageRecycler = findViewById<RecyclerView>(R.id.recycler)
        imageRecycler.addItemDecoration(MarginDecoration(this))
        imageRecycler.hasFixedSize()
        var load = findViewById<ProgressBar>(R.id.loader)
        if (imgs != null) {
            imageRecycler.adapter = PicturesAdapter(
                imgs, this@ImageDisplayActivity,
                supportFragmentManager, folder
            )
            load.visibility = View.GONE
        }
        val sortBtn: Button = findViewById(R.id.sort)
        val progressBar: ProgressBar = findViewById(R.id.loader)
        sortBtn.setOnClickListener {
            imageRecycler.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                withContext(Dispatchers.Default) {
                    println(filesDir)
                    val model = NeurelNetService.getModel(this@ImageDisplayActivity)
                    for (i in imgs!!) {
                        var result = model.classify(NeurelNetService.getTensorImgFromPath(i))
                        var category = result.get(0).categories.get(0).index
                        async(Dispatchers.IO) {
                            moveImg(category,i)
                        }.start()
                    }
                }
                progressBar.visibility = View.GONE
                imageRecycler.visibility = View.VISIBLE
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun moveImg(category: Int, path: String) {
        try {
            if (category == 0) {
                File(path).let { sourceFile ->
                    sourceFile.copyTo(File(filesDir.toString() + "cats/" + sourceFile.name))
                }
            }
            if (category == 1) {
                File(path).let { sourceFile ->
                    sourceFile.copyTo(File(filesDir.toString() + "cars/" + sourceFile.name))
                }
            }
            if (category == 2) {
                File(path).let { sourceFile ->
                    sourceFile.copyTo(File(filesDir.toString() + "dogs/" + sourceFile.name))
                }
            }
        } catch (e: FileAlreadyExistsException) {
        }
    }




}

