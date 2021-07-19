package ru.skinallergic.checkskin.components.fooddiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.databinding.ItemProductBinding

class RecyclerProductAdapter : RecyclerView.Adapter<RecyclerProductAdapter.Item>(), BaseAdapterForDiff<ProductEntity> {
    override var list: ArrayList<ProductEntity> = arrayListOf()
    lateinit var deletingFun : (product: ProductEntity)->Unit
    lateinit var binder : (item: Item, entity: ProductEntity)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false)
        return Item(view)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        val item= list[position]
        binder(holder, item)
    }

    override fun addData( myData: ProductEntity){
        this.list.add(myData)
        notifyItemInserted(list.indexOf(myData))
    }

    override fun removeData( myData: ProductEntity){
        val index=list.indexOf(myData)
        list.removeAt(index)
        notifyItemRemoved(index)
    }

    fun getData(): List<ProductEntity>{
        return list
    }

    fun bind(binder : (item: Item, entity: ProductEntity)->Unit){
        this.binder=binder
    }

    fun setDeletingFunction(function: (entity: ProductEntity)->Unit){
        deletingFun=function
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemProductBinding = DataBindingUtil.bind(itemView)!!
        val deleteBtnVisible = ObservableField(false)
        var name: TextView =binding.name
        var weight: TextView =binding.weight
        val delete: ImageButton =binding.deleteButton
        init {
            binding.buttonVisible=this
        }
    }
}