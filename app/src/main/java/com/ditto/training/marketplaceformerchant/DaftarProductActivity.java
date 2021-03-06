package com.ditto.training.marketplaceformerchant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ditto.training.marketplaceformerchant.adapter.ListProductAdapter;
import com.ditto.training.marketplaceformerchant.model.AccessToken;
import com.ditto.training.marketplaceformerchant.model.ListProduct;
import com.ditto.training.marketplaceformerchant.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class DaftarProductActivity extends AppCompatActivity {
    RecyclerView rvProduct;
    ListProductAdapter listProductAdapter;
    ArrayList<Product> data;
    RequestQueue requestQueue;
    FloatingActionButton floatingActionButton;
    Intent addProductIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_produk);

        data = new ArrayList<>();
        rvProduct = findViewById(R.id.rv_list_produk);
        listProductAdapter = new ListProductAdapter(this, data);

        rvProduct.setAdapter(listProductAdapter);
        rvProduct.setLayoutManager(new GridLayoutManager(this, 2));

        LoadVolley();

        floatingActionButton = findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductIntent = new Intent(getApplicationContext(), TambahProductActivity.class);
                startActivity(addProductIntent);
            }
        });
    }

    private void LoadVolley() {
        String url = "http://210.210.154.65:4444/api/merchant/products";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gsonData = new Gson();
                        ListProduct listProduct = gsonData.fromJson(response.toString(),ListProduct.class);
                        listProductAdapter.addData(listProduct.getListProduct());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String bodyRes = "";
                        try{
                            bodyRes = new String(error.networkResponse.data,"UTF-8");
                            System.out.println(bodyRes);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE)).getToken().getTokenType() +" "+ TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE)).getToken().getAccessToken());
                return headers;
            }
        };

        VolleyService.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
