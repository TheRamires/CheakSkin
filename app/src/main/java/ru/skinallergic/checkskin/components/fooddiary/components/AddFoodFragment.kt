package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.adapters.DiffUtilFromMySelf
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerProductAdapter
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerProductAdapter_UNUSED
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.components.fooddiary.view_models.AddingFoodViewModel
import ru.skinallergic.checkskin.components.fooddiary.view_models.add
import ru.skinallergic.checkskin.components.fooddiary.view_models.delete
import ru.skinallergic.checkskin.databinding.FragmentAddFood2Binding
import java.util.*

class AddFoodFragment : BaseFoodFragment(){
    val eatingList=listOf("Выберите прием пищи", "Завтрак", "Обед", "Ланч", "Ужин")
    lateinit var backStack: ImageButton
    lateinit var saveButton: ImageView
    lateinit var spinner: Spinner
    lateinit var addButton: Button
    lateinit var binding: FragmentAddFood2Binding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter :RecyclerProductAdapter
    lateinit var thisView: View
    val viewModel by lazy {ViewModelProvider(this, viewModelFactory).get(AddingFoodViewModel::class.java)}

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
        viewModel.productList.observe(viewLifecycleOwner, {newData->
            val diffUtil = DiffUtilFromMySelf<ProductEntity>(adapter.getData(), newData)
            diffUtil.calculate(adapter)
        })

        saveButton.setOnClickListener {
            viewModel.save(adapter.getData())
        }

        backStack.setOnClickListener {
            quitSaveLogic({
                viewModel.quitSaveCondition() }, {
                popBack(it) }, {
                viewModel.backSave(adapter.getData())})
        }

        viewModel.isBackSaved.observe(viewLifecycleOwner,{ isSaved->
            if (isSaved){popBack(thisView)}
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
        viewModel.productList.add(position)
    }

    fun delete(position: ProductEntity){
        viewModel.productList.delete(position)
    }

    fun getData():List<ProductEntity>{
        return adapter.getData()
    }
}