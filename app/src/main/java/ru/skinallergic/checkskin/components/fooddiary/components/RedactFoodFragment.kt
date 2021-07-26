 package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.components.fooddiary.adapters.DiffUtilFromMySelf
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerProductRedactAdapter
import ru.skinallergic.checkskin.components.fooddiary.data.Food
import ru.skinallergic.checkskin.components.fooddiary.data.FoodMealForMain
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.components.fooddiary.viewModels.FoodDiaryViewModel
import ru.skinallergic.checkskin.components.fooddiary.viewModels.MealViewModel
import ru.skinallergic.checkskin.components.fooddiary.viewModels.add
import ru.skinallergic.checkskin.components.fooddiary.viewModels.delete
import ru.skinallergic.checkskin.databinding.FragmentRedactFoodBinding

 class RedactFoodFragment : BaseFoodFragment() {
     lateinit var backStack: ImageButton
     lateinit var saveButton: ImageView
     lateinit var recyclerView: RecyclerView
     lateinit var adapter : RecyclerProductRedactAdapter<ProductEntity>
     var idPostion: Int?= null
     val foodDiaryViewModel by lazy { ViewModelProvider(requireActivity(), viewModelFactory).get(FoodDiaryViewModel::class.java) }
     val mealViewModel by lazy { ViewModelProvider(this,viewModelFactory).get(MealViewModel::class.java) }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         idPostion=arguments?.getInt(BUNDLE_ID_OF_REMIND)
         Loger.log("idPostion $idPostion")
         getStartData()
     }

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding=FragmentRedactFoodBinding.inflate(inflater)
        val view= binding.root
        backStack=binding.backBtn
        saveButton=binding.okBtn
        recyclerView=binding.recycler

        adapter= RecyclerProductRedactAdapter<ProductEntity>().apply {
            setDeletingFunction {  position-> delete(position)  }
            bind { item, entity ->
                item.binding.apply {
                    food=entity
                    delete.setOnClickListener { delete(entity) }
                }
            }
        }
         recyclerView.adapter=adapter
         binding.addBtn.setOnClickListener { add(ProductEntity()) }

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
/*
         idPostion?.let {
             val list: List<FoodMealForMain?>? =foodDiaryViewModel.foodDiaryList.value
             list?.let {
                 for (foodEntity in list){
                     if (foodEntity?.id == idPostion){
                         displayPosition(foodEntity)
                         break
                     }
                 }
             }
         }
*/
         mealViewModel.productList.observe(viewLifecycleOwner, {newData->
             displayPosition(newData)
         })
     }

     fun quitSaveCondition(): Boolean {
         return false
     }
     fun displayPosition(newData: List<ProductEntity>){

             val diffUtil = DiffUtilFromMySelf<ProductEntity>(adapter.getData(), newData)
             diffUtil.calculate(adapter)
     }
     fun add(position: ProductEntity){
         println("add click")
         with(mealViewModel){
             productList.add(position)
         }
     }
     fun delete(position: ProductEntity){
         with(mealViewModel){
             productList.delete(position)
         }
     }
     fun save(){

     }
     fun getStartData(){
         idPostion?.let {
             val list: List<FoodMealForMain?>? =foodDiaryViewModel.foodDiaryList.value
             list?.let {
                 for (foodEntity in list){
                     if (foodEntity?.id == idPostion){
                         with(mealViewModel){
                             if (foodEntity != null) {
                                 productList.value=FoodConverter.convertToProductPosition(foodEntity.list)
                             }
                         }
                         break
                     }
                 }
             }
         }
     }

     object FoodConverter{
         fun convertToProductPosition(food :Food): ProductEntity{
             return ProductEntity(name = food.name!!, weight = food.weight.toString(), allergic = food.is_allergen)
         }

         fun convertToProductPosition(foodList: List<Food>): ArrayList<ProductEntity> {
             val finalList= arrayListOf<ProductEntity>()
             for (food in foodList){
                 finalList.add(ProductEntity(name = food.name!!, weight = food.weight.toString(), allergic = food.is_allergen))
             }
             return finalList
         }
         fun convertToFood(oldList: List<ProductEntity>):List<Food>{
             var newList  = mutableListOf<Food>()
             if (oldList != null) {
                 for (product in oldList){
                     product.weight?.toInt()?.let { weight->
                         Food(name =product.name, weight =weight, is_allergen = product.allergic) }?.let {food->
                         newList.add(food) }
                 }
             }
             return newList
         }
     }
}