package ru.skinallergic.checkskin.components.healthdiary.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.databinding.SwipeLayoutBinding;

import java.util.ArrayList;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {
    private Context mContext;
    private ArrayList<EntityReminders> list;

    public SwipeRecyclerViewAdapter(Context context, ArrayList<EntityReminders> objects) {
        this.mContext = context;
        this.list = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        SwipeLayoutBinding binding=SwipeLayoutBinding.inflate(inflater,parent,false);
        return new SwipeRecyclerViewAdapter.SimpleViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final EntityReminders item = list.get(position);

        viewHolder.binding.setEntity(list.get(position));

        viewHolder.binding.swipe.setShowMode(SwipeLayout.ShowMode.PullOut);

        //dari kiri
        viewHolder.binding.swipe.
                addDrag(SwipeLayout.DragEdge.Left, viewHolder.binding.swipe.findViewById(R.id.bottom_wrapper1));

        //dari kanan
        viewHolder.binding.swipe.
                addDrag(SwipeLayout.DragEdge.Right, viewHolder.binding.swipe.findViewById(R.id.bottom_wraper));
        viewHolder.binding.swipe.
                addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        viewHolder.binding.swipe.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("position", position);
                Toast.makeText(mContext, " Click : " + item.getName() + " \n" + item.getDescription(),
                        Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).
                        navigate(R.id.action_remindersFragment3_to_remindersDetailFragment,bundle);
            }
        });

        viewHolder.binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked on Information " +
                        viewHolder.binding.name.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
/*
        //snare green --------------------------------------------------------------------------------------
        viewHolder.Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Clicked on Share " + viewHolder.Name.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //edit blue ---------------------------------------------------------------------------------------
        viewHolder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.Name.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
*/
        viewHolder.binding.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.removeShownLayouts(viewHolder.binding.swipe);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
                mItemManger.closeAllItems();
                Toast.makeText(v.getContext(), "Deleted " + viewHolder.binding.name.getText().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder{
        SwipeLayoutBinding binding;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}
