package ru.skinallergic.checkskin.components.healthdiary.components

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.healthdiary.*
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaCommonViewModel
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ImageViewModel
import ru.skinallergic.checkskin.components.profile.ActionFunction
import ru.skinallergic.checkskin.components.profile.DialogOnlyOneFunc
import ru.skinallergic.checkskin.handlers.ToastyManager
import ru.skinallergic.checkskin.view_models.DateViewModel
import java.io.File
import java.io.IOException
import java.util.regex.Pattern

abstract class BaseAreaFragment : Fragment() {
    lateinit var viewModelCommon: AffectedAreaCommonViewModel
    lateinit var dateViewModel: DateViewModel
    lateinit var imageViewModel: ImageViewModel

    lateinit var photoController: PhotoController
    lateinit var cameraPermission: CameraPermission
    lateinit var manager: FragmentManager
    lateinit var toastyManager: ToastyManager

    lateinit var imageView: ImageView
    var currentPhotoId: Int = 0

    var popBackTrue=false

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
                        bitmap = BitmapManager.compressBitmap(bitmapTemp)
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
                            val file = BitmapManager.SaveFileFromBitmap(finalBitmap, currentPhotoId, context!!)

                            Loger.log("file put  $file", "image")
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
                    .placeholder(R.drawable.no_photo)
                    .into(imageView)
        } else Picasso.with(requireContext()).load(file).into(imageView)
    }

    //*******************************************save temp photo
    //save image
    fun showByPicassoWithSave(path: String?, imageView: ImageView): String? {
        if (path == null) {
            return null
        }
        Picasso.with(requireContext())
                .load(path)
                .placeholder(R.drawable.no_photo)
                .into(imageView)
        return imageViewModel.imageDownload(path){ dir->
            viewModelCommon.photoDirectoryInMemory = dir // testing +
        }
    }
/*
    fun imageDownload(ctx: Context, pathHttp: String): String? {
        val dir = ctx.getExternalFilesDir("photo")
        if (!dir!!.exists()) {
            dir.mkdirs()
        }
        dir.createNewFile()

        viewModelCommon.photoDirectoryInMemory = dir // testing +
        //toastyManager.toastyyyy(viewModelCommon.photoDirectoryInMemory.toString())

        val filePath = dir.absolutePath + "/" + randomUUID() + ".png" //testing png+ , Date+
        try {
            Picasso.with(ctx)
                    .load(pathHttp)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(getTarget(filePath))
            return filePath

        } catch (t: Throwable) {
            return null
        }
    }*/
/*
    fun getTarget(filePath: String): Target {
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
    */
    fun save(view: View) {
        saving()
    }
    fun saving() {
        if (viewModelCommon.isChanged()) {
            try {
                viewModelCommon.addReport(dateViewModel.getDateUnix())
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        } else Navigation.findNavController(requireView()).popBackStack()
    }
    fun quitSaveLogic(navigation: BackNavigation){
        QuitSaveLogic.logic(viewModelCommon.isChanged(), { navigation.nav() }, { this.popBackTrue =true ;saving() }, manager)
    }
}