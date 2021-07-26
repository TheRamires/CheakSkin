package ru.skinallergic.checkskin.components.fooddiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.data.Food
import ru.skinallergic.checkskin.databinding.ItemOneEat2Binding
import ru.skinallergic.checkskin.databinding.ItemOneEatBinding

class FoodPositionAdapter2(override var list: ArrayList<Food>,
                           val bindingInterface: RecyclerCallback<ItemOneEat2Binding,Food>
) : BaseAdapterForDiff<Food>, RecyclerView.Adapter<FoodPositionAdapter2.Item>() {


    override fun addData(myData: Food) {
        TODO("Not yet implemented")
    }

    override fun removeData(myData: Food) {
        TODO("Not yet implemented")
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_one_eat_2, parent, false)
        return Item(view)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        val item = list[position]
        holder.onBind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class Item(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding: ItemOneEat2Binding = DataBindingUtil.bind(itemView)!!

        fun onBind(food: Food){
            bindingInterface?.bind(binding, food)
        }
    }

    interface RecyclerCallback<VM : ItemOneEat2Binding?, Food> {
        fun bind(binder: VM,food: ru.skinallergic.checkskin.components.fooddiary.data.Food)
    }
}