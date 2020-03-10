package com.ditto.training.marketplaceformerchant;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyService {
    private static VolleyService INSTANCE;
    private static RequestQueue requestQueue;
    private static Context context;

    public VolleyService(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized VolleyService getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE = new VolleyService(context);
        }
        return INSTANCE;
    }


    public static RequestQueue getRequestQueue() {
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request req){
        getRequestQueue().add(req);
    }
}
