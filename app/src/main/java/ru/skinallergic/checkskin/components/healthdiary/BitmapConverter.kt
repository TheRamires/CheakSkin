package ru.skinallergic.checkskin.components.healthdiary

import android.content.Context
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object BitmapConverter {

    fun compressBitmap(bitmap: Bitmap): Bitmap {
        val halfWidth = bitmap.getWidth() / 7
        val halfHeight = bitmap.getWidth() / 7

        val bmHalf = Bitmap.createScaledBitmap(bitmap, halfWidth,
                halfHeight, false)
        return bmHalf
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun fileFromBitmap(yourBitmap: Bitmap?, count: Int, context: Context): File? {
        //create a file to write bitmap data
        val f = File(context.cacheDir, "image$count.png")
        f.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        yourBitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        return f.absoluteFile
    }
}