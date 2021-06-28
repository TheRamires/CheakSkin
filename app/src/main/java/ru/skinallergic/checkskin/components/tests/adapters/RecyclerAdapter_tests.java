package ru.skinallergic.checkskin.components.tests.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.tests.data.EntityTest;
import ru.skinallergic.checkskin.databinding.ItemTests2Binding;

import java.util.List;

public class RecyclerAdapter_tests extends RecyclerView.Adapter<RecyclerAdapter_tests.Item> {
    private List<EntityTest> list;
    public Binder binder;

    public RecyclerAdapter_tests(List<EntityTest> list){
        this.list=list;
    }

    @NonNull
    @Override
    public RecyclerAdapter_tests.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemTests2Binding binding=ItemTests2Binding.inflate(inflater,parent,false);
        return new RecyclerAdapter_tests.Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_tests.Item holder, int position) {
        holder.onBind();
        /*
        EntityTest entity=list.get(position);
        int image=entity.getImage();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image);
        holder.binding.testImage.setImageBitmap(bitmap);
        holder.binding.setEntity(entity);
        holder.binding.cardView.setOnClickListener((View v)-> {
            Bundle bundle=new Bundle();
            bundle.putLong("idPosition", list.get(position).getId());
            Navigation.findNavController(v).navigate(R.id.testsOneFragment,bundle);
        });
        */

    }
    public void setBinder(Binder binder){
        this.binder=binder;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder{
        ItemTests2Binding binding;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
        public void onBind(){
            binder.bind(binding, list.get(getPosition()));
        }
    }
    public interface Binder{
        public void bind(ItemTests2Binding binding, EntityTest entity);
    }
}
