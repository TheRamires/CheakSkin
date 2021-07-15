 package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.databinding.FragmentRedactFoodBinding

 class RedactFoodFragment : BaseFoodFragment() {
     lateinit var backStack: ImageButton
     lateinit var saveButton: ImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding=FragmentRedactFoodBinding.inflate(inflater)
        val view= binding.root
        backStack=binding.backBtn
        saveButton=binding.okBtn
        return view
    }

     override fun onStart() {
         super.onStart()

         backStack.setOnClickListener {
             quitSaveLogic({
                 quitSaveCondition()},{
                 popBack(it)},{
                 save()})
         }
     }
     fun save(){

     }
     fun quitSaveCondition(): Boolean {
         return false
     }
}