package com.ditto.training.marketplaceformerchant.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ditto.training.marketplaceformerchant.R;
import com.ditto.training.marketplaceformerchant.RegisterActivity;
import com.ditto.training.marketplaceformerchant.TokenManager;
import com.ditto.training.marketplaceformerchant.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextView tvRegisterActivity;
    EditText etEmail, etPassword;
    String email, password;
    Button btnLogin;

    final String EMAIL = "email";
    final String PASSWORD = "password";

    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findView();

        tvRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    postDataRegister();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Isi Semua Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findView() {
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.button_login);
        tvRegisterActivity = findViewById(R.id.tv_register_activity);
    }

    private void postDataRegister(){
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        String url = "http://210.210.154.65:4444/api/auth/login";
        StringRequest loginReg = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        accessToken = new Gson().fromJson(response, AccessToken.class);
                        TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).saveToken(accessToken);
                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
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
                            Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new Hashtable<>();

                headers.put("Accept","application/json");
                headers.put("Content-Type","application/x-www-form-urlencoded");

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new Hashtable<>();

                params.put(EMAIL, email);
                params.put(PASSWORD, password);

                return params;
            }
        };

        VolleyService.getInstance(getApplicationContext()).addToRequestQueue(loginReg);
    }

    private boolean isValidInput(){
        boolean isValid = true;

        if(etEmail.getText().toString().isEmpty()){
            etEmail.setError("Email is Require");
            isValid = false;
        }

        else if(etPassword.getText().toString().isEmpty()){
            etPassword.setError("Password is Require");
            isValid = false;
        }

        else{
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            isValid = true;
        }

        return isValid;
    }
}
