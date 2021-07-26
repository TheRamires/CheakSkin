 package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.adapters.DiffUtilFromMySelf
import ru.skinallergic.checkskin.components.fooddiary.adapters.RecyclerProductRedactAdapter
import ru.skinallergic.checkskin.components.fooddiary.data.Food
import ru.skinallergic.checkskin.components.fooddiary.data.FoodEntity
import ru.skinallergic.checkskin.components.fooddiary.data.FoodMealForMain
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.components.fooddiary.viewModels.*
import ru.skinallergic.checkskin.components.fooddiary.viewModels_unused.RedactViewModel
import ru.skinallergic.checkskin.databinding.FragmentRedactFoodBinding

 class RedactFoodFragment : BaseFoodFragment() {
     lateinit var thisView: View

     var saved : Int? =null
     var deleted : Int? =null
     var redacted : Int? =null
     var mustSaved : Int? =null
     var mustDeleted : Int? =null
     var mustRedacted : Int? =null
     var backSaving : Boolean =false

     lateinit var backStack: ImageButton
     lateinit var saveButton: ImageView
     lateinit var recyclerView: RecyclerView
     lateinit var adapter : RecyclerProductRedactAdapter<ProductEntity>
     var currentMeal: Int?=null
     var idPostion: Int?= null
     var fromSearching =false
     val foodDiaryViewModel by lazy { ViewModelProvider(requireActivity(), viewModelFactory).get(FoodDiaryViewModel::class.java) }
     val mealViewModel by lazy { ViewModelProvider(this,viewModelFactory).get(MealViewModel::class.java) }
     val redactViewModel by lazy { ViewModelProvider(this,viewModelFactory).get(RedactFoodViewModel::class.java) }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         idPostion=arguments?.getInt(BUNDLE_ID_OF_REMIND)
         fromSearching = arguments?.getBoolean(FROM_SEARCHING) == true
         Loger.log("idPostion $idPostion \n fromSearching$fromSearching")
         getStartData()
         clearInnerValues()
     }

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding=FragmentRedactFoodBinding.inflate(inflater)
        thisView= binding.root
        backStack=binding.backBtn
        saveButton=binding.okBtn
        recyclerView=binding.recycler

        adapter= RecyclerProductRedactAdapter<ProductEntity>().apply {
            setDeletingFunction {  position-> delete(position)  }
            bind { item, entity ->
                item.binding.apply {
                    food=entity
                    delete.setOnClickListener { delete(entity) }
                    nameEditText.doAfterTextChanged { redactViewModel.newList.redactName(entity, it.toString()) }
                    weightEditText.doAfterTextChanged { redactViewModel.newList.redactWeight(entity,it.toString())}
                }
            }
        }
         recyclerView.adapter=adapter
         binding.addBtn.setOnClickListener { add(ProductEntity()) }

         return thisView
    }

     override fun onStart() {
         super.onStart()

         backStack.setOnClickListener {
             back(it)
             /*quitSaveLogic({
                 quitSaveCondition()},{
                 popBack(it)},{
                 save()})*/
         }
         with(mealViewModel){
             productList.observe(viewLifecycleOwner, {newData->
                 redactViewModel.newList=newData
                 displayPosition(newData)
             })
             isSaved.observe(viewLifecycleOwner,{
                 if (it){
                     isSaved.value=false
                     saved= saved?.plus(1)
                     popBackConditions()
                 }
             })
             isDeleted.observe(viewLifecycleOwner,{
                 if (it){
                     isDeleted.value=false
                     deleted= deleted?.plus(1)
                     popBackConditions()
                 }
             })
             isRedacted.observe(viewLifecycleOwner,{
                 if (it){
                     isRedacted.value=false
                     redacted= redacted?.plus(1)
                     popBackConditions()
                 }
             })

         }


         saveButton.setOnClickListener {
             save()
             println("oldList ${redactViewModel.oldList}")
             println("newList ${redactViewModel.newList}")
             println("addList ${redactViewModel.getAddList()}")
             println("deleteList ${redactViewModel.getDeleteList()}")
             println("redactList ${redactViewModel.getRedactList()}")
         }

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
         val addList=redactViewModel.getAddList()
         mustSaved=addList.size
         val deleteList=redactViewModel.getDeleteList()
         mustDeleted=deleteList.size
         val redactList=redactViewModel.getRedactList()
         mustRedacted=redactList.size
         if (addList.isNotEmpty()){
             println("go addList")
             currentMeal?.let { mealViewModel.addMealsAndConvert(dateViewModel.dateUnix, it,addList) }
         }
         if (deleteList.isNotEmpty()){
             println("go deleteList")
             mealViewModel.deleteMeal(deleteList)
         }
         if (redactList.isNotEmpty()){
             println("go redactList")
             currentMeal?.let { mealViewModel.redactMealAndConvert(dateViewModel.dateUnix, it,redactList) }
         }
     }

     fun back(view: View){
         quitSaveLogic({
             redactViewModel.saveCondition() }, {
             popBack(view) }, {
             //viewModel.backSave(adapter.getData())
             save()
         })
     }

     fun getStartData(){
         idPostion?.let {
             var list: List<FoodMealForMain?>?=null
             if (fromSearching){
                 list=foodDiaryViewModel.searchingDiaryList.value
             } else {
                 list =foodDiaryViewModel.foodDiaryList.value
             }
             list?.let {
                 for (foodEntity in list){
                     if (foodEntity?.id == idPostion){
                         with(mealViewModel){
                             if (foodEntity != null) {
                                 println("☺☺☺777777")
                                 redactViewModel.oldList= FoodConverter.convertToProductPosition(foodEntity.list)
                                 currentMeal=foodEntity.meal
                                 productList.value = FoodConverter.convertToProductPosition(foodEntity.list)
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

         fun convertToProductPosition(foodList: List<FoodEntity>): ArrayList<ProductEntity> {
             val finalList= arrayListOf<ProductEntity>()
             for (foodEntity in foodList){
                 finalList.add(ProductEntity(
                         id =foodEntity.id,
                         name = foodEntity.food?.name!!,
                         weight = foodEntity.food.weight.toString(),
                         allergic = foodEntity.food.is_allergen))
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

     private fun popBackConditions(){
         if(saved!! >= mustSaved!! && deleted!!>=mustDeleted!! && redacted!!>=mustRedacted!! ){

             popBack(thisView)
             /*if (backSaving){
                 popBack(thisView)
             } else {
                 val list = redactViewModel.newList
                 clearInnerValues()
             }*/
         }
     }

     private fun clearInnerValues(){
         saved=0
         deleted=0
         redacted=0
         backSaving=false
     }
     override fun popBack(view: View){
         Navigation.findNavController(view).popBackStack(R.id.navigation_foodDiary,false)
     }
}