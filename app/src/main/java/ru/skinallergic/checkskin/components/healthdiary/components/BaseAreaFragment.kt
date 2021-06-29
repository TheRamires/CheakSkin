package ru.skinallergic.checkskin.components.healthdiary.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.healthdiary.CameraPermission
import ru.skinallergic.checkskin.components.healthdiary.PhotoController
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaRedactViewModel
import ru.skinallergic.checkskin.view_models.DateViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern

abstract class BaseAreaFragment : Fragment() {
    lateinit var viewModel: AffectedAreaRedactViewModel
    lateinit var dateViewModel: DateViewModel

    lateinit var photoController: PhotoController
    lateinit var cameraPermission: CameraPermission
    lateinit var manager: FragmentManager

    lateinit var imageView: ImageView
    var currentPhotoId: Int=0

    fun toPhoto(imageView:ImageView) {
        when (imageView.id) {
            R.id.photo_rash_0 -> currentPhotoId = 0
            R.id.photo_rash_1 -> currentPhotoId = 1
            R.id.photo_rash_2 -> currentPhotoId = 2
        }
        photoController.madePhoto()
    }

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
        if (path == null) {
            return
        }
        Picasso.with(requireContext())
                .load(path)
                .into(imageView)
    }

    //*******************************************save temp photo
    //save image
   fun showByPicasso(path: String?, name: String, imageView: ImageView):String? {
        if (path==null){return null}
        Picasso.with(requireContext())
                .load(path)
                .into(imageView)
        return imageDownload(requireContext(), name, path)
    }
    fun imageDownload(ctx: Context, name: String, pathHttp: String): String {
        val dir=ctx.getExternalFilesDir("photo")
        if (!dir!!.exists()){
            dir.mkdirs()
        }
        dir.createNewFile()

        val filePath = dir.absolutePath + "/" + name
        Picasso.with(ctx)
                .load(pathHttp)
                .placeholder(R.drawable.ic_launcher_background)
                .into(getTarget(filePath))
        return filePath
    }

    fun getTarget(filePath: String):Target{
        return object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                Thread {
                    val file = File(filePath)
                    if (!file.parentFile.exists())
                        file.parentFile.mkdirs();

                    if (!file.exists())
                        file.createNewFile();
                    try {
                        val ostream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
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

    @Throws(IOException::class, ClassNotFoundException::class)
    fun reportRequest() {

        viewModel.addReport(dateViewModel.getDateUnix())
    }

    fun save(view: View?) {
        if (viewModel.isChanged()) {
            try {
                reportRequest()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        } else Navigation.findNavController(requireView()).popBackStack(R.id.affectedAreasFragment, false)
    }
}