package ru.skinallergic.checkskin.components.fooddiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.components.fooddiary.data.ProductEntity
import ru.skinallergic.checkskin.databinding.ItemProductBinding

class RecyclerProductAdapter (private var productList: MutableList<ProductEntity>): RecyclerView.Adapter<RecyclerProductAdapter.Item>(){
    lateinit var textChangedListener: OnTextChangedListener
    lateinit var onDeleteListener: OnDeleteListener

    fun addPosition(product: ProductEntity){
        productList.add(product)
    }
    fun removePosition(product_: ProductEntity){
        for (product in productList){
            if (product.id==product_.id)
                productList.removeAt()
        }
    }

    fun getDataList():List<ProductEntity>{
        return productList
    }

    fun setListeners(listener: OnTextChangedListener, onDeleteListener: OnDeleteListener ){
        this.textChangedListener=listener
        this.onDeleteListener=onDeleteListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemProductBinding = ItemProductBinding.inflate(inflater, parent, false)
        val view=binding.root
        return Item(view)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        val item = productList[position]
        holder.name.setText(item.name)
        holder.weight.setText(item.weight)

        holder.name.doAfterTextChanged {
            textChangedListener.textChange(it.toString(),holder.name.id )
        }
        holder.weight.doAfterTextChanged {
            textChangedListener.textChange(it.toString(),holder.weight.id )
        }
        holder.binding.deleteButton.setOnClickListener {
            onDeleteListener.delete(item,position)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
    class Item(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding: ItemProductBinding = DataBindingUtil.bind(itemView)!!
        val name: EditText = binding.name
        val weight: EditText =binding.weight
    }
    interface OnTextChangedListener{
        fun textChange(str: String, id:Int)
    }
    interface OnDeleteListener{
        fun delete(entity: ProductEntity, position: Int)
    }
}