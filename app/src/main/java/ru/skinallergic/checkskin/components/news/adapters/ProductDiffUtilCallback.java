package ru.skinallergic.checkskin.components.news.adapters;

import androidx.recyclerview.widget.DiffUtil;

import ru.skinallergic.checkskin.components.news.pojo.Datum;

import java.util.List;

public class ProductDiffUtilCallback extends DiffUtil.Callback {

    private final List<Datum> oldList;
    private final List<Datum> newList;

    public ProductDiffUtilCallback(List<Datum> oldList, List<Datum> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Datum oldNews = oldList.get(oldItemPosition);
        Datum newNews = newList.get(newItemPosition);
        if (oldList==null){
            return false;
        }else return oldNews.getId() == newNews.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Datum oldNews = oldList.get(oldItemPosition);
        Datum newNews = newList.get(newItemPosition);
        if (oldNews==null) {
            return false;
        }else return oldNews.getName().equals(newNews.getName())
                && oldNews.getCreated().equals(newNews.getCreated())
                && oldNews.getText().equals(newNews.getText())
                && oldNews.getCover().equals(newNews.getCover());
    }
}
