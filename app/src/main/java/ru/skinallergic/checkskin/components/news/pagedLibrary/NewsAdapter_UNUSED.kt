package ru.skinallergic.checkskin.components.news.pagedLibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.components.news.pojo.Datum
import ru.skinallergic.checkskin.databinding.ItemNews2Binding

class NewsAdapter_UNUSED(diffUtilCallback: DiffUtil.ItemCallback<Datum>
) : PagedListAdapter<Datum, NewsAdapter_UNUSED.MyViewHolder?>(diffUtilCallback) {
    lateinit var bindingInterface: BindInterface<ItemNews2Binding>;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_news_2, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        bindingInterface.bind(getItem(position), holder.binding!!)
    }

    fun setBinding(binding : BindInterface<ItemNews2Binding>){
        this.bindingInterface=binding
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding= DataBindingUtil.bind<ItemNews2Binding>(itemView)

    }

    interface BindInterface<VM : ViewDataBinding>{
        fun bind(entity: Datum? , binder:VM)
    }
}