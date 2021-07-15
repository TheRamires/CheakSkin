package ru.skinallergic.checkskin.components.fooddiary.components

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import ru.skinallergic.checkskin.databinding.FragmentAddFood2Binding

class AddFoodFragment : BaseFoodFragment() {
    lateinit var backStack: ImageButton
    lateinit var saveButton: ImageView
    lateinit var spinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentAddFood2Binding.inflate(inflater)
        val view= binding.root
        binding.apply {
            backStack=backBtn
            saveButton=okBtn
            spinner=eatTime
            fragment=this@AddFoodFragment
        }

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
        spinner.setAdapter(listOf("Выберите прием пищи","Завтрак","Обед","Ланч","Ужин"))

        subscribeDate()
    }

    fun save(){

    }
    fun quitSaveCondition(): Boolean {
        return false
    }
}