package ru.skinallergic.checkskin.components.healthdiary.components

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.FrameLayout
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
import ru.skinallergic.checkskin.components.profile.DialogFunctionFragment
import ru.skinallergic.checkskin.components.profile.DialogOnlyOneFunc
import ru.skinallergic.checkskin.components.profile.NavigationFunction
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
                    val selectedImage = data?.data
                    try {
                        val bitmapTemp = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
                        bitmap = BitmapManager.compressBitmap(bitmapTemp)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                PhotoController.REQUEST_TAKE_PHOTO -> {
                    val extras = data?.extras
                    extras?.let { bitmap = extras["data"] as Bitmap? }
                }
            }
            //val finalBitmap = PhotoController.cropToSquare(bitmap)  //testing **
            val finalBitmap = bitmap
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
    fun savePath(path: String?): String?{
        return path?.let {
            imageViewModel.imageDownload(path) { dir ->
                viewModelCommon.photoDirectoryInMemory = dir // testing +
            }
        }
    }
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
        QuitSaveLogic.logic(viewModelCommon.isChanged(), { navigation.nav() }, {
            this.popBackTrue = true;saving()
        }, manager)
    }

    open fun hasImage(view: ImageView): Boolean {
        val drawable = view.drawable
        var hasImage = drawable != null
        if (hasImage && drawable is BitmapDrawable) {
            hasImage = drawable.bitmap != null
        }
        return hasImage
    }

    open fun dialogForDelete(index: Int, imageView: ImageView) {
        val action = object : ActionFunction {
            override fun action() {
                val newPhotoList = viewModelCommon.newMap[viewModelCommon.getNewArea()]!![viewModelCommon.getNewView()]!!.photos
                newPhotoList!![index] = null
                println("new photo list $newPhotoList")
                viewModelCommon.newMap[viewModelCommon.getNewArea()]!![viewModelCommon.getNewView()]!!.photos = newPhotoList
                imageView.setImageDrawable(null)
                viewModelCommon.someChanging() // testing
            }
        }
        val stump = object : NavigationFunction {
            override fun navigate() {

            }
        }
        val message = "Удалить фотографию?"
        val dialog = DialogFunctionFragment(message, action, stump)
        dialog.show(parentFragmentManager, "deleteDialog")
    }
    fun showPhotoDialog(photo: Drawable){
        val dialog= Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.photo_dialog)
        val container=dialog.findViewById<FrameLayout>(R.id.container)
        container.clipToOutline=true
        val imageView=dialog.findViewById<ImageView>(R.id.image_view)
        imageView?.setImageDrawable(photo)
        //val buttonClose=dialog.findViewById<Button>(R.id.button_close)
       // buttonClose?.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }
}