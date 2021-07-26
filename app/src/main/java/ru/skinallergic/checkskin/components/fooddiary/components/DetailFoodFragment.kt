package ru.skinallergic.checkskin.components.fooddiary.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.adapters.FoodPositionAdapter
import ru.skinallergic.checkskin.components.fooddiary.adapters.FoodPositionAdapter2
import ru.skinallergic.checkskin.components.fooddiary.data.Food
import ru.skinallergic.checkskin.components.fooddiary.data.FoodMealForMain
import ru.skinallergic.checkskin.components.fooddiary.viewModels.FoodDiaryViewModel
import ru.skinallergic.checkskin.databinding.FragmentDetailFoodBinding
import ru.skinallergic.checkskin.databinding.ItemOneEat2Binding
import ru.skinallergic.checkskin.databinding.ItemOneEatBinding

const val BUNDLE_ID_OF_REMIND = "idOfRemind"
class DetailFoodFragment : BaseFoodFragment() {
    lateinit var backStack: ImageButton
    lateinit var redactButton: ImageView
    var idPostion: Int?= null
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FoodPositionAdapter2
    lateinit var binding: FragmentDetailFoodBinding
    val foodDiaryViewModel by lazy { ViewModelProvider(requireActivity(), viewModelFactory).get(FoodDiaryViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idPostion=arguments?.getInt(BUNDLE_ID_OF_REMIND)
        Loger.log("idPostion $idPostion")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentDetailFoodBinding.inflate(inflater)
        binding.apply {
            backStack=backBtn
            redactButton=redactBtn
            fragment=this@DetailFoodFragment
            recyclerView=recycler
        }

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        backStack.setOnClickListener { popBack(it) }
        redactButton.setOnClickListener {
            val bundle = Bundle()
            idPostion?.let { it1 -> bundle.putInt(BUNDLE_ID_OF_REMIND, it1) }
            navFunction(it, R.id.action_detailFoodFragment_to_redactFoodFragment,bundle )
        }
        subscribeDate()

        idPostion?.let {
            val list: List<FoodMealForMain?>? =foodDiaryViewModel.foodDiaryList.value
            list?.let {
                binding.title.text= it[0]?.meal.toString()
                for (foodEntity in list){
                    if (foodEntity?.id == idPostion){
                        displayPosition(foodEntity)
                        break
                    }
                }
            }
        }
    }
    fun displayPosition(foodEntity: FoodMealForMain?){
        foodEntity?.let {
            adapter= FoodPositionAdapter2(it.list as ArrayList<Food>, object : FoodPositionAdapter2.RecyclerCallback<ItemOneEat2Binding, Food>{
                override fun bind(binder: ItemOneEat2Binding, food: Food) {
                    binder.food=food
                }
            })
            recyclerView.adapter=adapter
        }
    }
    fun navFunction(view: View, fragmentId: Int, bundle: Bundle){
        Navigation.findNavController(view).navigate(fragmentId, bundle)
    }
}