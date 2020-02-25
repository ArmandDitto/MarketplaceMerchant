package com.ditto.training.marketplaceformerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ditto.training.marketplaceformerchant.model.Product;

public class DeskripsiProductActivity extends AppCompatActivity {
    public TextView tvNamaDesc, tvMerchantDesc, tvJumlahDesc, tvMiniNameDesc, tvMiniCategoryDesc;
    public ImageView ivProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_product);

        Product product = getIntent().getParcelableExtra("produk");
        findView();

        String namaProduk = product.getProductName();
        int jumlahProduk = product.getProductQty();
        String merchantProduk = product.getMerchant().getMerchantName();
        String kategoriProduk = product.getCategory().getCategoryName();
        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl+product.getProductImage();

        tvNamaDesc.setText(namaProduk);
        tvMerchantDesc.setText(merchantProduk);
        tvJumlahDesc.setText(String.valueOf(jumlahProduk));
        tvMiniNameDesc.setText(namaProduk);
        tvMiniCategoryDesc.setText(kategoriProduk);
        Glide.with(this).load(url).into(ivProduk);
    }

    public void findView(){
        ivProduk = findViewById(R.id.iv_deskripsi_produk);
        tvNamaDesc = findViewById(R.id.tv_deskripsi_nama_produk);
        tvMerchantDesc = findViewById(R.id.tv_deskripsi_nama_merchant);
        tvJumlahDesc = findViewById(R.id.tv_deskripsi_jumlah_produk);
        tvMiniNameDesc = findViewById(R.id.tv_deskripsi_mini_nama_produk);
        tvMiniCategoryDesc = findViewById(R.id.tv_deskripsi_mini_kategori_produk);
    }
}
