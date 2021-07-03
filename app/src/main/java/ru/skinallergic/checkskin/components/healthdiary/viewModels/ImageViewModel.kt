package ru.skinallergic.checkskin.components.healthdiary.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import ru.skinallergic.checkskin.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject

class ImageViewModel @Inject constructor(val context: Context, val model :ImageModel) : ViewModel() {
   // @SuppressLint("StaticFieldLeak")
   // val context = application.applicationContext

    fun imageDownload(pathHttp: String, dirCallBack: (dir: File?)->Unit): String? {
        val dir = context.getExternalFilesDir("photo")
        if (!dir!!.exists()) {
            dir.mkdirs()
        }
        dir.createNewFile()

        dirCallBack(dir)

        val filePath = dir.absolutePath + "/" + UUID.randomUUID() + ".png" //testing png+ , Date+
        try {
            Picasso.with(context)
                    .load(pathHttp)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(model.getTarget(filePath))
            return filePath

        } catch (t: Throwable) {
            return null
        }
    }
}
class ImageModel @Inject constructor(){
    fun getTarget(filePath: String): Target {
        return object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom) {
                Thread {
                    val file = File(filePath)
                    if (!file.parentFile.exists())
                        file.parentFile.mkdirs();

                    if (!file.exists())
                        file.createNewFile();
                    try {
                        val ostream = FileOutputStream(file)
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
                        ostream.flush()
                        ostream.close()
                        println(filePath)
                    } catch (e: IOException) {
                        println("IOException" + e.localizedMessage)
                    }
                }.start()
            }

            override fun onBitmapFailed(errorDrawable: Drawable) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
        }
    }
}