package com.ditto.training.marketplaceformerchant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ditto.training.marketplaceformerchant.model.AccessToken;
import com.ditto.training.marketplaceformerchant.model.RegisterErrorRespone;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    String customerFirstName, customerLastName, customerEmail, customerPassword, customerConfirmPassword, customerMerchant;
    EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etMerchant;
    CheckBox cbBeMerchant;
    Button btnRegister;
    TextView tvLoginActivity;
    LinearLayout parentRegisterMerchant;
    RequestQueue requestQueue;
    AccessToken accessToken;

    final String FIRST_NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String EMAIL = "email";
    final String PASSWORD = "password";
    final String CONPASSWORD = "confirm_password";
    final String IS_MERCHANT = "is_merchant";
    final String MERCHANT_NAME = "merchant_name";

    int isMerchant = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findView();

        requestQueue = Volley.newRequestQueue(this);

        tvLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);*/
            }
        });

        cbBeMerchant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    parentRegisterMerchant.setVisibility(View.VISIBLE);
                    isMerchant = 1;
                }
                else {
                    parentRegisterMerchant.setVisibility(View.INVISIBLE);
                    isMerchant = 0;
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    postDataRegister();
                    Toast.makeText(RegisterActivity.this, "Berhasil ?", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Isi Semua Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findView() {
        etFirstName = findViewById(R.id.et_register_first_name);
        etLastName = findViewById(R.id.et_register_last_name);
        etEmail = findViewById(R.id.et_register_email);
        etPassword = findViewById(R.id.et_register_pasword);
        etConfirmPassword = findViewById(R.id.et_register_confirm_password);
        etMerchant = findViewById(R.id.et_register_merchant_name);
        cbBeMerchant = findViewById(R.id.cb_register_as_merchant);
        btnRegister = findViewById(R.id.button_register);
        tvLoginActivity = findViewById(R.id.tv_login_activity);
        parentRegisterMerchant = findViewById(R.id.parent_register_merchant);
    }


    private void postDataRegister(){
        customerFirstName = etFirstName.getText().toString();
        customerLastName = etLastName.getText().toString();
        customerEmail = etEmail.getText().toString();
        customerPassword = etPassword.getText().toString();
        customerConfirmPassword = etConfirmPassword.getText().toString();
        isMerchant = 1;
        customerMerchant = etMerchant.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/signup";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        accessToken = new Gson().fromJson(response, AccessToken.class);
                        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).saveToken(accessToken);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        String body = "";
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject res = new JSONObject(body);
                            RegisterErrorRespone errorRespone = new Gson().fromJson(res.getJSONObject("error").toString(),RegisterErrorRespone.class);
                            if(errorRespone.getEmailError().size()>0){
                                if(errorRespone.getEmailError().get(0)!=null){
                                    etEmail.setError(errorRespone.getEmailError().get(0));
                                }
                            }
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();

                headers.put("Accept","application/json");
                headers.put("Content-Type","application/x-www-form-urlencoded");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();

                params.put(FIRST_NAME, customerFirstName);
                params.put(LAST_NAME, customerLastName);
                params.put(EMAIL, customerEmail);
                params.put(PASSWORD, customerPassword);
                params.put(CONPASSWORD, customerConfirmPassword);
                if(isMerchant==1){
                    params.put(IS_MERCHANT, String.valueOf(isMerchant));
                    params.put(MERCHANT_NAME, customerMerchant);
                }
                return params;
            }
        };

        VolleyService.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private boolean isValidInput(){
        boolean isValid = true;

        if(etFirstName.getText().toString().isEmpty()){
            etFirstName.setError("First Name is Require");
            isValid = false;
        }

        if(etLastName.getText().toString().isEmpty()){
            etLastName.setError("Last Name is Require");
            isValid = false;
        }

        if(etEmail.getText().toString().isEmpty()){
            etEmail.setError("Email is Require");
            isValid = false;
        }
        else if(!etEmail.getText().toString().contains("@")){
            etEmail.setError("Input valid Email");
            isValid = false;
        }

        if(etPassword.getText().toString().isEmpty()){
            etPassword.setError("Password is Require");
            isValid = false;
        }
        else if(etPassword.getText().toString().length()<8){
            etPassword.setError("Password must be contain at least 8 characters");
            isValid = false;
        }

        if(etConfirmPassword.getText().toString().isEmpty()){
            etConfirmPassword.setError("Confirm Password is Require");
            isValid = false;
        }
        else if(!(etConfirmPassword.getText().toString().equals(etPassword.getText().toString()))){
            etConfirmPassword.setError("Password did not match");
            isValid = false;
        }

        if(isMerchant==1){
            if(etMerchant.getText().toString().isEmpty()){
                etMerchant.setError("Merchant Name is Required");
                isValid = false;
            }
        }

        return isValid;
    }
}
