package com.ditto.training.marketplaceformerchant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ditto.training.marketplaceformerchant.adapter.ListProductAdapter;
import com.ditto.training.marketplaceformerchant.model.Product;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class DeskripsiProductActivity extends AppCompatActivity {
    public TextView tvNamaDesc, tvMerchantDesc, tvJumlahDesc, tvMiniNameDesc, tvMiniCategoryDesc;
    public ImageView ivProduk;
    public Button btnDeleteProduct, btnEditProduct;
    Product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_product);

        product = getIntent().getParcelableExtra("produk");
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

        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new  AlertDialog.Builder(DeskripsiProductActivity.this);
                alert.setTitle("Hapus Data");
                alert.setMessage("Kamu yakin ingin menghapus ini ?");
                alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct();
                        Intent listProduk = new Intent(DeskripsiProductActivity.this, ListProductAdapter.class);
                        startActivity(listProduk);
                    }
                });

                alert.show();
            }
        });

        btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProduct = new Intent(DeskripsiProductActivity.this, EditProductActivity.class);
                startActivity(editProduct);
            }
        });
    }

    private void deleteProduct() {
        String url = "http://210.210.154.65:4444/api/product/"+product.getProductId()+"/delete";
        RequestQueue con = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DeskripsiProductActivity.this, "Data telah dihapus", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeskripsiProductActivity.this, "Data tidak terhapus", Toast.LENGTH_SHORT).show();
                    }
                });
        con.add(req);
    }

    public void findView(){
        ivProduk = findViewById(R.id.iv_deskripsi_produk);
        tvNamaDesc = findViewById(R.id.tv_deskripsi_nama_produk);
        tvMerchantDesc = findViewById(R.id.tv_deskripsi_nama_merchant);
        tvJumlahDesc = findViewById(R.id.tv_deskripsi_jumlah_produk);
        tvMiniNameDesc = findViewById(R.id.tv_deskripsi_mini_nama_produk);
        tvMiniCategoryDesc = findViewById(R.id.tv_deskripsi_mini_kategori_produk);
        btnDeleteProduct = findViewById(R.id.button_delete_product);
        btnEditProduct = findViewById(R.id.button_edit_product);
    }


}
