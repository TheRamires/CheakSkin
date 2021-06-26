package ru.skinallergic.checkskin.components.news.pagedLibrary

import androidx.recyclerview.widget.DiffUtil
import ru.skinallergic.checkskin.components.news.pojo.Datum

class MyDiffUtilCallback_UNUSED: DiffUtil.ItemCallback<Datum>() {
    override fun areItemsTheSame(oldItem: Datum, newItem: Datum): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Datum, newItem: Datum): Boolean {
        return oldItem.name.equals(newItem.name) &&
                oldItem.text.equals(newItem.text) &&
                oldItem.created==newItem.created
    }
}