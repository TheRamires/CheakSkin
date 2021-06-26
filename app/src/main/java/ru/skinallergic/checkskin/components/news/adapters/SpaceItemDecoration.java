package ru.skinallergic.checkskin.components.news.adapters;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int position;
    boolean left;
    boolean right;
    public SpaceItemDecoration(){
        position=0;
        left=true;
        right=false;
    }

    public SpaceItemDecoration(int position,boolean left, boolean right){
        this.position=position;
        this.left=left;
        this.right=right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        // вычисление пикселей по DP. Здесь отступ будет *8dp*
        int margin = 5;
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, view.getResources().getDisplayMetrics());
        if(parent.getChildAdapterPosition(view) == position){
            //outRect.top = space;
            if (left){
                outRect.left=space;
            }
            if (right){
                outRect.right=space;
            }
            outRect.bottom = 0;
        }
    }
}