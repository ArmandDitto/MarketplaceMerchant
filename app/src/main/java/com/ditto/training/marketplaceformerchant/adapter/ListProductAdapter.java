package com.ditto.training.marketplaceformerchant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ditto.training.marketplaceformerchant.DeskripsiProductActivity;
import com.ditto.training.marketplaceformerchant.R;
import com.ditto.training.marketplaceformerchant.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHolder> {
    Context context;
    List<Product> productList;

    public ListProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public ListProductAdapter(Context context) {
        this.context = context;
    }

    public void addData(ArrayList<Product> productList){
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewku;
        viewku = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk, parent, false);
        return new ViewHolder(viewku);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvNamaProduk.setText(productList.get(position).getProductName());
        holder.tvNamaMerchant.setText(productList.get(position).getMerchant().getMerchantName());

        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl+productList.get(position).getProductImage();
        if(productList.get(position).getProductImage()==null){
            Glide.with(context).load(baseUrl+"images/products/product_image_300x300.png").into(holder.ivProduk);
        }
        else {
            Glide.with(context).load(url).into(holder.ivProduk);
        }

        holder.parentProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deskripsiActivity = new Intent(context, DeskripsiProductActivity.class);
                deskripsiActivity.putExtra("produk", productList.get(position));
                context.startActivity(deskripsiActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProduk;
        public TextView tvNamaProduk, tvNamaMerchant;
        public LinearLayout parentProduk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNamaProduk = itemView.findViewById(R.id.tv_product_name);
            tvNamaMerchant = itemView.findViewById(R.id.tv_merchant_name);
            ivProduk = itemView.findViewById(R.id.iv_product);
            parentProduk = itemView.findViewById(R.id.parent_product);
        }
    }
}
