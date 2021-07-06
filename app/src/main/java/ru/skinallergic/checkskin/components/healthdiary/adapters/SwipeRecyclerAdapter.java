package ru.skinallergic.checkskin.components.healthdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ru.skinallergic.checkskin.Adapters.MyRecyclerAdapter;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.databinding.SwipeLayoutBinding;

public class SwipeRecyclerAdapter extends
        RecyclerView.Adapter<SwipeRecyclerAdapter.Item>
        implements SimpleItemTouchHelperCallback.ItemTouchHelperAdapter
{
    List<EntityReminders> list;
    public SwipeRecyclerAdapter.OnItemClickListener itemListener;

    public SwipeRecyclerAdapter( List<EntityReminders> list){
        this.list=list;
    }

    public void setOnItemClickListener(SwipeRecyclerAdapter.OnItemClickListener listener){
        this.itemListener=listener;
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        System.out.println("onItemMove");
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                System.out.println("1. i = "+i);
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                System.out.println("2. i = "+i);
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout,parent,false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        EntityReminders entity=list.get(position);
        holder.binding.setEntity(entity);
        holder.viewForeground.setOnClickListener((View view)->{
            itemListener.onItemClick(view,entity.getId());});


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder{
        SwipeLayoutBinding binding;
        public RelativeLayout viewBackground;
        public FrameLayout viewForeground;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
            assert binding != null;
            viewBackground=binding.viewBackground;
            viewForeground=binding.viewForeground;
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int id);
    }
}