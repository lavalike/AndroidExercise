package com.android.exercise.ui.activity.adapter.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.exercise.R;
import com.dimeno.adapter.RecyclerViewHolder;

/**
 * LoadViewHolder
 * Created by wangzhen on 2020/6/10.
 */
public class LoadViewHolder extends RecyclerViewHolder<String> {

    private TextView mTextView;

    public LoadViewHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more_holder_layout, parent, false));
        mTextView = itemView.findViewById(R.id.text);
    }

    @Override
    public void bind() {
        mTextView.setText(mData);
    }
}
