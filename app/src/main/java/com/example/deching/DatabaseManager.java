package com.example.deching;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class DatabaseManager {
    public RequestQueue queue;

    public DatabaseManager(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }
}
