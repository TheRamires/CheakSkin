package ru.skinallergic.checkskin.components.fooddiary.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity

class ProductFoodDiffUtilCallback(private val oldList: List<ProductEntity>, private val newList: List<ProductEntity>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct: ProductEntity = oldList[oldItemPosition]
        val newProduct: ProductEntity = newList[newItemPosition]
        return oldProduct.id == newProduct.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct: ProductEntity = oldList[oldItemPosition]
        val newProduct: ProductEntity = newList[newItemPosition]
        return (oldProduct.name == newProduct.name && oldProduct.weight == newProduct.weight)
    }
}