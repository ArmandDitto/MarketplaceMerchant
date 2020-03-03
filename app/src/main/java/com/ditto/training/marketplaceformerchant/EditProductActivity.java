package com.ditto.training.marketplaceformerchant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ditto.training.marketplaceformerchant.adapter.CategoriesAdapter;
import com.ditto.training.marketplaceformerchant.model.Category;
import com.ditto.training.marketplaceformerchant.model.Product;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {

    EditText etNamaProduk, etHargaProduk, etJumlahProduk, etDeskripsiProduk;
    Button buttonAddProduct, btnAddImage;
    ImageView ivAddImage;
    ArrayList<Category> categories;
    Spinner spinnerCategory;
    CategoriesAdapter categoriesAdapter;
    private int PICK_IMAGE_REQUEST = 1;
    RequestQueue requestQueueku;
    Product product = null;

    String productName, productPrice, productQty, productDesc, merchantId, categoryId;
    String productImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        requestQueueku = Volley.newRequestQueue(getApplicationContext());

        etNamaProduk = findViewById(R.id.et_nama_produk);
        etHargaProduk = findViewById(R.id.et_harga_produk);
        etJumlahProduk = findViewById(R.id.et_jumlah_produk);
        etDeskripsiProduk = findViewById(R.id.et_deskripsi_produk);

        btnAddImage = findViewById(R.id.button_add_image);
        buttonAddProduct = findViewById(R.id.button_add);

        ivAddImage = findViewById(R.id.iv_add_image);

        spinnerCategory = findViewById(R.id.spinner_kategori);
        /*categories = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter();
        spinnerCategory.setAdapter(categoriesAdapter);
        spinnerCategory.setOnItemSelectedListener(this);*/
        //getAllCategories();

        previousDataLoad();

        ivAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = etNamaProduk.getText().toString();
                productDesc = etDeskripsiProduk.getText().toString();
                productPrice = etHargaProduk.getText().toString();
                productQty = etJumlahProduk.getText().toString();
                merchantId = "1";

                if(productImage == null){ // jika kosong,
                    productImage = null;     // isi dengan null
                }

                volleyLoad();

                Intent daftarProduk = new Intent(getApplicationContext(), DaftarProductActivity.class);
                startActivity(daftarProduk);
            }
        });
    }

    private void getAllCategories() {
    }

    private void volleyLoad() {
        final StringRequest stringRequest = new StringRequest(Request.Method.PUT, "http://210.210.154.65:4444/api/product/"+product.getProductId()+"/update",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response :", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i("response :", response);

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Product Berhasil Diubah",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data = new Hashtable<String, String>();

                data.put("productName", productName);
                data.put("productPrice", productPrice);
                data.put("productQty", productQty);
                data.put("productImage", productImage);
                data.put("productDesc", productDesc);
                data.put("categoryId", categoryId);
                data.put("merchantId", merchantId);
                return data;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> data = new Hashtable<String, String>();

                data.put("Content-type","application/x-www-form-urlencoded");
                return data;
            }
        };
        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void previousDataLoad() {
        String url = "http://210.210.154.65:4444/api/product/"+product.getProductSlug();

        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = response.getJSONObject("data");
                            product = gson.fromJson(jsonObject.toString(), Product.class);

                            etNamaProduk.setText(product.getProductName());
                            etDeskripsiProduk.setText(product.getProductDesc());
                            etJumlahProduk.setText(String.valueOf(product.getProductQty()));
                            etHargaProduk.setText(String.valueOf(product.getProductPrice()));
                            Glide.with(getApplicationContext()).load(product.getProductImage()).into(ivAddImage);
                            //merchantId.setText(String.valueOf(product.getMerchant().getMerchantId()));
                            //categoryId.setText(String.valueOf(product.getCategory().getCategoryId()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProductActivity.this, "Data tidak terbaca", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(req);
    }

    private void showFileChooser() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                productImage = getStringImage(bitmap); // call getStringImage() method below this code
                Log.d("image",productImage);

                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .override(ivAddImage.getWidth())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivAddImage);
                System.out.println("image : "+productImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
