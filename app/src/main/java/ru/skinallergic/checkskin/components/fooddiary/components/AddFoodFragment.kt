package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.data.ProductPosition
import ru.skinallergic.checkskin.components.fooddiary.view_models.AddingFoodViewModel
import ru.skinallergic.checkskin.databinding.FragmentAddFood2Binding
import java.util.*

class AddFoodFragment : BaseFoodFragment() {
    lateinit var backStack: ImageButton
    lateinit var saveButton: ImageView
    lateinit var spinner: Spinner
    lateinit var addButton: Button
    lateinit var binding: FragmentAddFood2Binding
    lateinit var containerLayout: ViewGroup
    val viewModel by lazy {ViewModelProvider(this, viewModelFactory).get(AddingFoodViewModel::class.java)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAddFood2Binding.inflate(inflater)
        val view= binding.root
        binding.apply {
            backStack=backBtn
            saveButton=okBtn
            spinner=eatTime
            addButton=buttonAdd
            fragment=this@AddFoodFragment
        }

        spinner.setAdapter(listOf("Выберите прием пищи","Завтрак","Обед","Ланч","Ужин"))

        containerLayout=binding.container
        if (viewModel.viewList.value ==null || viewModel.viewList.value!!.isEmpty()){
            addView(containerLayout)
        }

        subscribeDate()
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
        addButton.setOnClickListener {
            addView(containerLayout)
        }
    }

    fun save(){

    }
    fun quitSaveCondition(): Boolean {
        return false
    }

    fun addView(container : ViewGroup){
        val productPosition : View = LayoutInflater.from(container.context).inflate(R.layout.item_product,container)
        productPosition.id = viewModel.newId()


        //Loger.log("size "+container.size)
        buttonAddCorrectParams(binding.linear,RelativeLayout.BELOW)
        subscribeEditTextName(productPosition.id, productPosition.findViewById(R.id.name))
    }

    fun buttonAddCorrectParams(hook: ViewGroup, doing: Int){ //doing = Relative.Bellow
        val margin =80
        val param :RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        param.addRule(doing, hook.id)
        param.topMargin=margin
        param.marginEnd=margin
        param.marginStart=margin
        binding.buttonAdd.layoutParams=param
    }
    fun subscribeEditTextName(id: Int, editText: EditText){
        editText.doAfterTextChanged { name->
            viewModel.addNameToMap(id,name.toString())
            Loger.log(viewModel.positionList)
        }
    }
}