package ru.skinallergic.checkskin.components.healthdiary.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
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
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaCommonViewModel
import ru.skinallergic.checkskin.components.profile.ActionFunction
import ru.skinallergic.checkskin.components.profile.DialogOnlyOneFunc
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment
import ru.skinallergic.checkskin.components.profile.NavigationFunction
import ru.skinallergic.checkskin.handlers.HandleOnce
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.view_models.DateViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.UUID.randomUUID
import java.util.regex.Pattern
import javax.inject.Inject

abstract class BaseAreaFragment : Fragment() {
    lateinit var viewModelCommon: AffectedAreaCommonViewModel
    lateinit var dateViewModel: DateViewModel

    lateinit var photoController: PhotoController
    lateinit var cameraPermission: CameraPermission
    lateinit var manager: FragmentManager
    lateinit var toastyManager: ToastyManager

    lateinit var imageView: ImageView
    var currentPhotoId: Int=0

    fun toPhoto(imageView: ImageView) {
        when (imageView.id) {
            R.id.photo_rash_0 -> currentPhotoId = 0
            R.id.photo_rash_1 -> currentPhotoId = 1
            R.id.photo_rash_2 -> currentPhotoId = 2
        }
        photoController.madePhoto()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var bitmap: Bitmap? = null
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PhotoController.GALLERY_REQUEST -> {
                    val selectedImage = data!!.data
                    try {
                        val bitmapTemp = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
                        bitmap =compressBitmap(bitmapTemp)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                PhotoController.REQUEST_TAKE_PHOTO -> {
                    val extras = data!!.extras
                    bitmap = extras!!["data"] as Bitmap?
                }
            }
            val finalBitmap = PhotoController.cropToSquare(bitmap)
            if (finalBitmap != null) {
                val actionFunction = object : ActionFunction {
                    override fun action() {
                        viewModelCommon.someChanging()
                        imageView.setImageBitmap(finalBitmap)
                        //viewModel.addBitMapToList(finalBitmap);
                        try {
                            val file = fileFromBitmap(finalBitmap, currentPhotoId)
                            viewModelCommon.putPhotoToMap(currentPhotoId, file!!)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: ClassNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
                val dialog = DialogOnlyOneFunc(
                        "Фото необходимо обрезать до квадрта",
                        "Принять", "Отмена",
                        actionFunction)
                dialog.show(manager, "photo")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PhotoController.REQUEST_TAKE_PHOTO -> {
                val cameraIsTrue = cameraPermission.permissionsResult(this, requestCode, grantResults)
                if (cameraIsTrue) {
                    photoController.getCameraPhoto()
                }
            }
        }
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
                .placeholder(R.drawable.no_photo)
                .into(imageView)
    }

    //*******************************************save temp photo
    //save image
   fun showByPicasso(path: String?, name: String, imageView: ImageView):String? {
        if (path==null){return null}
        Picasso.with(requireContext())
                .load(path)
                .placeholder(R.drawable.no_photo)
                .into(imageView)
        return imageDownload(requireContext(), name, path)
    }
    fun imageDownload(ctx: Context, name: String, pathHttp: String): String? {
        val dir=ctx.getExternalFilesDir("photo")
        if (!dir!!.exists()){
            dir.mkdirs()
        }
        dir.createNewFile()

        viewModelCommon.photoDirectoryInMemory=dir // testing +
        //toastyManager.toastyyyy(viewModelCommon.photoDirectoryInMemory.toString())

        val filePath = dir.absolutePath + "/" + randomUUID() +".png" //testing png+ , Date+
        try {
            Picasso.with(ctx)
                    .load(pathHttp)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(getTarget(filePath))
            return filePath

        }catch (t: Throwable){
            return null
        }
    }

    fun getTarget(filePath: String):Target{
        return object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom) {
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
    private fun compressBitmap(bitmap: Bitmap): Bitmap{
        val halfWidth = bitmap.getWidth() / 7
        val halfHeight = bitmap.getWidth() / 7

        val bmHalf = Bitmap.createScaledBitmap(bitmap, halfWidth,
                halfHeight, false)
        return bmHalf
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun fileFromBitmap(yourBitmap: Bitmap?, count: Int): File? {
        //create a file to write bitmap data
        val f = File(requireContext().cacheDir, "image$count.png")
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

    @Throws(IOException::class, ClassNotFoundException::class)
    fun reportRequest() {

        viewModelCommon.addReport(dateViewModel.getDateUnix())
    }

    fun save(view: View?) {
        if (viewModelCommon.isChanged()) {
            try {
                reportRequest()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        } else Navigation.findNavController(requireView()).popBackStack()
    }
    fun quitSaveLogic(view: View){

        Loger.log("viewModelCommon.isChanged() " + viewModelCommon.isChanged())
        if (viewModelCommon.isChanged()) {
            quitSaveDialog(view){
                Navigation.findNavController(view).popBackStack()
            }
        } else {
            Navigation.findNavController(view).popBackStack()
        }
    }
    fun quitSaveLogic(view: View, navigation: BackNavigation){

        Loger.log("viewModelCommon.isChanged() " + viewModelCommon.isChanged())
        if (viewModelCommon.isChanged()) {
            quitSaveDialog(view){
                navigation.nav()
            }
        } else {
            navigation.nav()
        }
    }

    fun quitSaveDialog(view: View, popBack: () -> Unit) {
        val negative = object : ActionFunction {
            override fun action() {
                popBack()
            }
        }
        val positive = object : ActionFunction {
            override fun action() {
                save(view)
            }
        }
        val navigation = object : NavigationFunction {
            override fun navigate() {
                //empty
            }
        }
        val dialog = DialogTwoFunctionFragment(
                "Сохранить изменения", negative, positive, navigation)
        dialog.show(manager, "dialog")
    }
}