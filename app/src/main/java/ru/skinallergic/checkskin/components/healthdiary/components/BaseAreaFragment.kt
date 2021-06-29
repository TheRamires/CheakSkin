package ru.skinallergic.checkskin.components.healthdiary.components

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import ru.skinallergic.checkskin.Loger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern

abstract class BaseAreaFragment : Fragment(){

    fun checkServerPath(file: File): Boolean {
        val path = file.absolutePath
        val regex = "http"
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(path)
        if (matcher.find()) {
            Loger.log("matcher true")
            return true
        } else Loger.log("matcher false")
        return false
    }

    fun showByPicasso(file: File, imageView: ImageView) {
        val isServerPath = checkServerPath(file)
        Loger.log("isServerPath $isServerPath")
        if (isServerPath) {
            Picasso.with(requireContext())
                    .load(file.toString())
                    .into(imageView)
        } else Picasso.with(requireContext()).load(file).into(imageView)
    }
    fun showByPicasso(path: String?, imageView: ImageView) {
        if (path==null){return}
        Picasso.with(requireContext())
                .load(path)
                .into(imageView)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun fileFromBitmap(yourBitmap: Bitmap, count: Int): File? {
        //create a file to write bitmap data
        val f = File(requireContext().cacheDir, "image$count.png")
        f.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        yourBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        return f.absoluteFile
    }
}