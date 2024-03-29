package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.adapters.DiffUtilFromMySelf
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerProductAdapter
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.components.fooddiary.viewModels.AddingFoodViewModel
import ru.skinallergic.checkskin.components.fooddiary.viewModels.MealViewModel
import ru.skinallergic.checkskin.components.fooddiary.viewModels.add
import ru.skinallergic.checkskin.components.fooddiary.viewModels.delete
import ru.skinallergic.checkskin.databinding.FragmentAddFood2Binding
import java.util.*

class AddFoodFragment : BaseFoodFragment(){
    val eatingList=listOf("Выберите прием пищи", "Завтрак", "Обед", "Перекус", "Ужин")
    lateinit var backStack: ImageButton
    lateinit var saveButton: ImageButton
    lateinit var spinner: Spinner
    lateinit var addButton: Button
    lateinit var binding: FragmentAddFood2Binding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter :RecyclerProductAdapter
    lateinit var thisView: View
    val viewModel by lazy { ViewModelProvider(this, viewModelFactory).get(AddingFoodViewModel::class.java) }
    val mealViewModel by lazy { ViewModelProvider(this,viewModelFactory).get(MealViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter= RecyclerProductAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAddFood2Binding.inflate(inflater)
        thisView= binding.root
        binding.apply {
            backStack=backBtn
            saveButton=okBtn
            spinner=eatTime
            addButton=buttonAdd
            fragment=this@AddFoodFragment
            recyclerView=recycler
            baseViewModel=mealViewModel
        }
        recyclerView.adapter=adapter

        spinner.setAdapter(eatingList)
        subscribeDate()
        return thisView
    }

    override fun onStart() {
        super.onStart()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position!=0){
                    println(eatingList[position])
                    when(position){
                        1->mealViewModel.meal=0 //перекодирование для сервера
                        2->mealViewModel.meal=1
                        3->mealViewModel.meal=3
                        4->mealViewModel.meal=2
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        addButton.setOnClickListener {
            if (viewModel.conditionOfAdding(getData())){
                add(ProductEntity())
            } else {toastyManager.toastyyyy("Пожалуйста заполните предыдущую позицию")}
        }

        adapter.apply{
            setDeletingFunction { position-> delete(position) }
            bind {holder,entity->
                holder.name.text= entity.name
                holder.weight.text= entity.weight
                holder.delete.setOnClickListener { deletingFun(entity) }
                holder.name.doAfterTextChanged { entity.name=it.toString(); deleteButtonVisibility(holder, entity) }
                holder.weight.doAfterTextChanged { entity.weight=it.toString() }
            }
        }

        add(ProductEntity())
        mealViewModel.productList.observe(viewLifecycleOwner, {newData->
            val diffUtil = DiffUtilFromMySelf<ProductEntity>(adapter.getData(), newData)
            diffUtil.calculate(adapter)
        })

        saveButton.setOnClickListener {
            //viewModel.save(adapter.getData())
            Loger.log("setOnClickListener")
            save()
        }

        backStack.setOnClickListener {
            Loger.log("mealViewModel.quitSaveCondition() ${mealViewModel.quitSaveCondition()}")
            quitSaveLogic({
                mealViewModel.quitSaveCondition() }, {
                popBack(it) }, {
                //viewModel.backSave(adapter.getData())
                save(mealViewModel.isBackSaved)
                })
        }

        /*viewModel.isBackSaved.observe(viewLifecycleOwner,{ isSaved->
            if (isSaved){popBack(thisView)}
        })*/

        mealViewModel.isBackSaved.observe(viewLifecycleOwner,{ isSaved->
            if (isSaved){
                mealViewModel.isBackSaved.value=false
                popBack(thisView) // view ??????????????????????????????????
            }
        })
    }

    fun deleteButtonVisibility(holder: RecyclerProductAdapter.Item, entity: ProductEntity){
        if (viewModel.conditionOfDelete(entity)){
            holder.deleteBtnVisible.set(true)
        } else holder.deleteBtnVisible.set(false)
    }

    fun quitSaveCondition(): Boolean {
        return false
    }

    fun add(position: ProductEntity){
        mealViewModel.productList.add(position)
    }

    fun delete(position: ProductEntity){
        mealViewModel.productList.delete(position)
    }

    fun getData():List<ProductEntity>{
        return adapter.getData()
    }

    fun save(backSaved: MutableLiveData<Boolean>?=null){
        Loger.log("save")
        val list: List<ProductEntity> =adapter.getData()
        mealViewModel.productList.value
        if (mealViewModel.saveCondition()){
            mealViewModel.addMealsAndConvert(
                    dateViewModel.dateUnix,
                    mealViewModel.meal!!,
                    list,
                    backSaved
            )
        }
    }
}