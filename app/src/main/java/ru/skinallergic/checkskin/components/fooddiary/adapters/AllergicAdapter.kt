package ru.skinallergic.checkskin.components.fooddiary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.Loger
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicEntity
import ru.skinallergic.checkskin.components.fooddiary.data.AllergicWriter
import ru.skinallergic.checkskin.databinding.ItemAllergicBinding

class AllergicAdapter( list_: ArrayList<AllergicWriter>,
) : RecyclerView.Adapter<AllergicAdapter.Item>(), BaseAdapterForDiff<AllergicWriter> {
    lateinit var binder : (item: Item, entity: AllergicWriter)->Unit
    override var list: ArrayList<AllergicWriter> =list_

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_allergic,parent,false)
        return Item(view)
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        val item=list[position]
        holder.name.text=item.name
        binder(holder, item)
    }
    fun bind(binder: (item: Item, entity: AllergicWriter) -> Unit){
        this.binder=binder
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun addData(myData: AllergicWriter){
        this.list.add(myData)
        notifyItemInserted(list.indexOf(myData))
    }

    override fun removeData(myData: AllergicWriter){
        val index=list.indexOf(myData)
        list.removeAt(index)
        notifyItemRemoved(index)
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding : ItemAllergicBinding = DataBindingUtil.bind(itemView)!!
        val name: TextView =binding.name
        val delete: ImageButton=binding.delete
    }
    fun setData(newList: ArrayList<AllergicWriter>){
        Loger.log("-------------------------------------setData")
        list=newList
    }
    fun convertToAllergicWriter(pagedList: PagedList<AllergicEntity?>): ArrayList<AllergicWriter>{
        Loger.log("-------------------------------------convertToAllergicWriter")
        val allergicWriterList= arrayListOf<AllergicWriter>()
        for (allergicEntity in pagedList){
            allergicWriterList.add(
                    AllergicWriter(id = allergicEntity!!.id, name = allergicEntity.name.toString())
            )
        }
        Loger.log("-------------------------------------allergicWriterList $allergicWriterList")
        return allergicWriterList
    }
}