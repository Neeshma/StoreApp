package com.android.storeapp.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.android.storeapp.R;
import com.android.storeapp.listeners.ProductPagerAdapter;
import com.android.storeapp.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Neeshma on 9/6/2017.
 */

public class ProductActivity extends Activity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        int currentItem = getIntent().getIntExtra("currentItem",0);
        //This will not work for large amounts of data. Using shared preferences instead. Alternatively we can make the list static
        //or use SQLite database to persist.
        //ArrayList<Product> productList = getIntent().getParcelableArrayListExtra("productList");

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("productList", "");

        Type type = new TypeToken<ArrayList<Product>>(){}.getType();
        ArrayList<Product> productList= gson.fromJson(json, type);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new ProductPagerAdapter(this,productList));
        viewPager.setCurrentItem(currentItem);
    }


}
