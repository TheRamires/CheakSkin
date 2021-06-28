package ru.skinallergic.checkskin.components.home.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.news.pojo.Datum;
import ru.skinallergic.checkskin.databinding.ItemNewsBinding;

import java.util.List;

import ru.skinallergic.checkskin.components.news.pojo.Datum;

public class RecyclerAdapter_news_UNUSED extends RecyclerView.Adapter<RecyclerAdapter_news_UNUSED.ItemList> {
    private List<Datum> list;

    public RecyclerAdapter_news_UNUSED(List<Datum> list){
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerAdapter_news_UNUSED.ItemList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemNewsBinding binding=ItemNewsBinding.inflate(inflater,parent,false);
        return new RecyclerAdapter_news_UNUSED.ItemList(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_news_UNUSED.ItemList holder, int position) {
        holder.binding.name.setText(list.get(position).getName());
        holder.binding.clickable.setOnClickListener((View v) ->{
            int id=list.get(position).getId();
            Bundle bundle=new Bundle();

            bundle.putInt("idPosition",id);

            // ВКЛЮЧИТЬ КНОПКУ--------------------------------------------------------------------

            Navigation.findNavController(v).navigate(R.id.newsOneFragment,bundle);

            //------------------------------------------------------------------------------------
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemList extends RecyclerView.ViewHolder{
        ItemNewsBinding binding;

        public ItemList(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}