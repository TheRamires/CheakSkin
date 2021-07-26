package ru.skinallergic.checkskin.components.fooddiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.databinding.ItemOneRedactBinding

class RecyclerProductRedactAdapter <T: ProductEntity> : RecyclerView.Adapter<RecyclerProductRedactAdapter.Item>(), BaseAdapterForDiff<T> {
    override var list: ArrayList<T> = arrayListOf()
    lateinit var deletingFun : (product: T)->Unit
    lateinit var binder : (item: Item, entity: T)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_one_redact, parent, false)
        return Item(view)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        val item= list[position]
        binder(holder, item)
    }

    override fun addData(myData: T){
        this.list.add(myData)
        notifyItemInserted(list.indexOf(myData))
    }

    override fun removeData(myData: T){
        val index=list.indexOf(myData)
        list.removeAt(index)
        notifyItemRemoved(index)
    }

    fun getData(): List<T>{
        return list
    }

    fun bind(binder: (item: Item, entity: T) -> Unit){
        this.binder=binder
    }

    fun setDeletingFunction(function: (entity: T) -> Unit){
        deletingFun=function
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemOneRedactBinding = DataBindingUtil.bind(itemView)!!

    }
}