package ru.skinallergic.checkskin.components.home.adapters;

import androidx.databinding.ViewDataBinding;

public interface RecyclerCallback<VM extends ViewDataBinding, T>{
    public void bind(VM binder, T entity);
}
