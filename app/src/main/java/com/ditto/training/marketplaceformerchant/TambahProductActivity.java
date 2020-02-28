package com.ditto.training.marketplaceformerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ditto.training.marketplaceformerchant.R;
import com.ditto.training.marketplaceformerchant.adapter.CategoriesAdapter;
import com.ditto.training.marketplaceformerchant.model.Category;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class TambahProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etNamaProduk, etHargaProduk, etJumlahProduk, etDeskripsiProduk;
    Button buttonAddProduct, btnAddImage;
    ImageView ivAddImage;
    ArrayList<Category> categories;
    Spinner spinnerCategory;
    CategoriesAdapter categoriesAdapter;
    RequestQueue requestQueueku;
    String productName, productPrice, productQty, productDesc, merchantId, categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_product);

        requestQueueku = Volley.newRequestQueue(getApplicationContext());

        etNamaProduk = findViewById(R.id.et_nama_produk);
        etHargaProduk = findViewById(R.id.et_harga_produk);
        etJumlahProduk = findViewById(R.id.et_jumlah_produk);
        etDeskripsiProduk = findViewById(R.id.et_deskripsi_produk);

        btnAddImage = findViewById(R.id.button_add_image);
        buttonAddProduct = findViewById(R.id.button_add);

        ivAddImage = findViewById(R.id.iv_add_image);

        spinnerCategory = findViewById(R.id.spinner_kategori);
        categories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter();
        spinnerCategory.setAdapter(categoriesAdapter);
        spinnerCategory.setOnItemSelectedListener(this);
        getAllCategories();

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = etNamaProduk.getText().toString();
                productPrice = etHargaProduk.getText().toString();
                productQty = etJumlahProduk.getText().toString();
                productDesc = etDeskripsiProduk.getText().toString();
                merchantId = "1";
                categoryId = "1";
                VolleyLoad();
            }
        });
    }

    private void findView(){

    }

    private void getAllCategories() {
        String url = "http://210.210.154.65:4444/api/categories";

        JsonObjectRequest listCatReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // handle response
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0;i<data.length();i++){
                                Gson gson = new Gson();
                                Category category = gson.fromJson(data.getJSONObject(i).toString(),Category.class);
                                categories.add(category);
                            }

                            categoriesAdapter.addData(categories);
                            categoriesAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),String.valueOf(categoriesAdapter.getCount()),Toast.LENGTH_LONG).show();

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });


        requestQueueku.add(listCatReq);
    }

    private void VolleyLoad(){
        String url = "http://210.210.154.65:4444/api/products/";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response :", response);
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        Log.i("response :", response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Berhasil ditambah", Toast.LENGTH_SHORT).show();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Gagal ditambahkan", Toast.LENGTH_LONG).show();
                }
            }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> data = new Hashtable<String, String>();

                data.put("productName", productName);
                data.put("productPrice", productDesc);
                data.put("productQty", productQty);
                data.put("productDesc", productDesc);
                data.put("categoryId", categoryId);
                data.put("merchantId", merchantId);
                return data;
            }
        };
        {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.categoryId = String.valueOf(categoriesAdapter.getItemId(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
