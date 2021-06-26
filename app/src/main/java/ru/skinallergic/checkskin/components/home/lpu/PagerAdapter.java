package ru.skinallergic.checkskin.components.home.lpu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {
    private int counter = 0;

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LPUListFrag();
            case 1:
                return new MapFragment();
        }

        return new LPUListFrag();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}