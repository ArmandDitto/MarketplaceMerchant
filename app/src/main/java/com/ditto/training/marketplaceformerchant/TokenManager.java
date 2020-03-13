package com.ditto.training.marketplaceformerchant;

import android.content.SharedPreferences;

import com.ditto.training.marketplaceformerchant.model.AccessToken;

public class TokenManager {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    private static TokenManager INSTANCE;

    public TokenManager(SharedPreferences preferences) {
        this.sharedPreferences = preferences;
        this.editor = sharedPreferences.edit();
    }

    public static synchronized TokenManager getInstance(SharedPreferences preferences){
        if(INSTANCE == null){
            INSTANCE  = new TokenManager(preferences);
        }

        return INSTANCE;
    }

    public void saveToken(AccessToken accessToken){
        editor.putString("TOKEN_TYPE", accessToken.getTokenType()).commit();
        editor.putString("ACCESS_TOKEN", accessToken.getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN", accessToken.getRefreshToken()).commit();
    }

    public void deleteToken(){
        editor.remove("TOKEN_TYPE").commit();
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();
    }

    public AccessToken getToken(){
        AccessToken accessToken = new AccessToken();
        accessToken.setTokenType(sharedPreferences.getString("TOKEN_TYPE", null));
        accessToken.setAccessToken(sharedPreferences.getString("ACCESS_TOKEN", null));
        accessToken.setRefreshToken(sharedPreferences.getString("REFRESH_TOKEN", null));
        return accessToken;
    }
}
