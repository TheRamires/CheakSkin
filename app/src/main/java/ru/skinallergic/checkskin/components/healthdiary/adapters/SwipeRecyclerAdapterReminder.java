package ru.skinallergic.checkskin.components.healthdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.Collections;
import java.util.List;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderEntity;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter;
import ru.skinallergic.checkskin.components.healthdiary.remote.Rash;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaCommonViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogThreeDeleteFunctionFragment;
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment;
import ru.skinallergic.checkskin.components.profile.NavigationFunction;
import ru.skinallergic.checkskin.databinding.ItemAreaSwipeBinding;
import ru.skinallergic.checkskin.databinding.SwipeLayoutBinding;

public class SwipeRecyclerAdapterReminder extends RecyclerSwipeAdapter<SwipeRecyclerAdapterReminder.Item> {
    private final SwipeRecyclerAdapterReminder.RecyclerCallback bindingInterface;
    List<ReminderEntity> list;
    FragmentManager fragmentManager;
    RemindersViewModel viewModel;
    SwipeRecyclerAdapterReminder.OnSwipeItemClickListener onItemClickListener;
    DeleteItemClickListener deleteItemClickListener;
    OffItemClickListener offItemClickListener;

    public SwipeRecyclerAdapterReminder(
                                    RemindersViewModel viewModel,
                                    FragmentManager fragmentManager,
                                    List<ReminderEntity> list,
                                    SwipeRecyclerAdapterReminder.RecyclerCallback bindingInterface){
        this.viewModel=viewModel;
        this.bindingInterface = bindingInterface;
        this.list=list;
        this.fragmentManager=fragmentManager;
    }

    public void setOnItemClickListener(SwipeRecyclerAdapterReminder.OnSwipeItemClickListener listener){
        onItemClickListener=listener;
    }

    public void setDeleteItemClickListener(DeleteItemClickListener deleteItemClickListener){
        this.deleteItemClickListener=deleteItemClickListener;
    }

    public void setOffItemClickListener(OffItemClickListener offItemClickListener){
        this.offItemClickListener=offItemClickListener;
    }

    @NonNull
    @Override
    public SwipeRecyclerAdapterReminder.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        SwipeLayoutBinding binding=SwipeLayoutBinding.inflate(inflater,parent,false);
        return new SwipeRecyclerAdapterReminder.Item(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeRecyclerAdapterReminder.Item holder, int position) {
        ReminderEntity item=list.get(position);
        holder.bindData(item);

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //dari kanan
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.bottom_wraper));

        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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
                /*if (xvel<-300){
                    deleteDialog(holder, item, position, item.getId());
                }*/
            }
        });

        holder.binding.deleteReminder.setOnClickListener((View v)-> {
            deleteDialog(holder, item, position, item.getId());

        });

       /* holder.binding.clickable.setOnClickListener((View v)-> {
            onItemClickListener.onItemClick(v, item.getId());

        });
*/

        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class Item extends RecyclerView.ViewHolder{
        SwipeLayoutBinding binding;
        public SwipeLayout swipeLayout;

        public Item(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
            swipeLayout=binding.swipe;
        }
        public void bindData(ReminderEntity entity){
            bindingInterface.bind(binding,entity);
            binding.executePendingBindings();
        }
    }

    public interface RecyclerCallback{
        void bind(SwipeLayoutBinding binder, ReminderEntity entity);
    }

    public void deleteDialog(SwipeRecyclerAdapterReminder.Item holder, ReminderEntity item, int position, int id){
        ActionFunction positive1= ()-> {
            deleteFunction(holder, item, position, item.getId());
        };
        ActionFunction positive2= ()-> {
            offFunction(holder, item, position, item.getId());
        };
        ActionFunction negative= ()-> {

        };
        NavigationFunction stump=()->{};
        DialogThreeDeleteFunctionFragment dialog=new DialogThreeDeleteFunctionFragment("Удалить",negative,positive1,positive2,stump);
        dialog.show(fragmentManager,"dialog");
    }

    public void deleteFunction(SwipeRecyclerAdapterReminder.Item viewHolder, ReminderEntity entity, int position, int id){
        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
        if (position<list.size()){
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            mItemManger.closeAllItems();
            //viewModel.deletePosition(id);

            deleteItemClickListener.onItemDelete(id);
        }
    }

    public void offFunction(SwipeRecyclerAdapterReminder.Item viewHolder, ReminderEntity entity, int position, int id){
        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
        if (position<list.size()){
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            mItemManger.closeAllItems();
            //viewModel.deletePosition(id);

            offItemClickListener.onItemOff(id);
        }
    }

    public interface OnSwipeItemClickListener{
        void onItemClick(View view, int id);
    }
    public interface DeleteItemClickListener{
        void onItemDelete(int id);
    }
    public interface OffItemClickListener{
        void onItemOff(int id);
    }
}