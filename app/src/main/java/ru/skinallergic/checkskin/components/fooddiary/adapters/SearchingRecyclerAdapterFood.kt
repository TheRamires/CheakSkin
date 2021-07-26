package ru.skinallergic.checkskin.components.fooddiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.data.FoodMealForMain
import ru.skinallergic.checkskin.databinding.ItemFoodSearchingBinding

class SearchingRecyclerAdapterFood(
        val list: ArrayList<FoodMealForMain>,
        val itemListener: OnItemClickListener,
        var bindingInterface: RecyclerCallback? = null
) : RecyclerView.Adapter<SearchingRecyclerAdapterFood.Item>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_food_searching, parent, false)
        return Item(view)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        val entity=list[position]
        holder.bindData(entity)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class Item(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: ItemFoodSearchingBinding = DataBindingUtil.bind(itemView)!!

        fun bindData(entity: FoodMealForMain?) {
            bindingInterface?.bind(binding, entity)
            binding.executePendingBindings()
        }

    }

    interface OnItemClickListener{
        fun onItemClick(view: View?, id: Int)
    }

    interface RecyclerCallback {
        fun bind(binder: ItemFoodSearchingBinding?, entity: FoodMealForMain?)
    }
}