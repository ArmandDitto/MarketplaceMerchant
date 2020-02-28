package com.ditto.training.marketplaceformerchant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ditto.training.marketplaceformerchant.R;
import com.ditto.training.marketplaceformerchant.model.Category;

import java.util.ArrayList;

public class CategoriesAdapter extends BaseAdapter {
    ArrayList<Category> categories = new ArrayList<>();

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getCategoryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_dropdown_item, parent,false);
        }
        Category category = getItem(position);
        TextView tvText = convertView.findViewById(R.id.tv_category_item);
        tvText.setText(category.getCategoryName());

        return convertView;
    }

    public void addData(ArrayList<Category> categories){
        this.categories = categories;
    }
}
