package com.android.storeapp.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.storeapp.listeners.ProductListAdapter;
import com.android.storeapp.listeners.ProductLoader;
import com.android.storeapp.listeners.RecyclerTouchListener;
import com.android.storeapp.listeners.RecyclerViewScrollListener;
import com.android.storeapp.model.Product;
import com.android.storeapp.model.ResponseObj;
import com.android.storeapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<ResponseObj>{

    private RecyclerViewScrollListener scrollListener;
    private ProgressBar progressBar;
    private ArrayList<Product> productList = new ArrayList<Product>();
    private RecyclerView.Adapter adapter;
    private int currentPage = 0;
    private int previousTotalItemCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            //This will not work for large amounts of data. Using shared preferences instead. Alternatively we can make the list static
            //or use SQLite database to persist.
            //productList = savedInstanceState.getParcelableArrayList("productList");

            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("productList", "");

            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
            productList= gson.fromJson(json, type);

            currentPage = savedInstanceState.getInt("currentPage");
            previousTotalItemCount = savedInstanceState.getInt("previousTotalItemCount");
            Bundle args = new Bundle();
            args.putString("url","https://walmartlabs-test.appspot.com/_ah/api/walmart/v1/walmartproducts/c86bbded-3988-463f-94a5-6443ed7cec34/"+currentPage+"/10");
            getLoaderManager().initLoader(0, args, this);
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        adapter = new ProductListAdapter(productList,this);
        rv.setAdapter(adapter);

        scrollListener = new RecyclerViewScrollListener(linearLayoutManager,currentPage,previousTotalItemCount) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                Bundle args = new Bundle();
                args.putString("url","https://walmartlabs-test.appspot.com/_ah/api/walmart/v1/walmartproducts/c86bbded-3988-463f-94a5-6443ed7cec34/"+page+"/10");
                restartLoad(args);
            }
        };
        // Adds the scroll listener to RecyclerView
        rv.addOnScrollListener(scrollListener);

        rv.addOnItemTouchListener(
                new RecyclerTouchListener(this, new RecyclerTouchListener.ItemClickListener() {
                    @Override public void onItemClicked(View view, int position) {
                        Intent intent = new Intent(MainActivity.this,ProductActivity.class);
                        intent.putExtra("currentItem",position);
                        //This will not work for large amounts of data. Using shared preferences instead. Alternatively we can make the list static
                        //or use SQLite database to persist.
                        //intent.putParcelableArrayListExtra("productList", productList);
                        SharedPreferences appSharedPrefs = PreferenceManager
                                .getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(productList);
                        prefsEditor.putString("productList", json);
                        prefsEditor.commit();
                        startActivity(intent);
                    }
                })
        );

        if(savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putString("url", "https://walmartlabs-test.appspot.com/_ah/api/walmart/v1/walmartproducts/c86bbded-3988-463f-94a5-6443ed7cec34/0/10");
            getLoaderManager().initLoader(0, args, this).forceLoad();
        }

    }

    public void restartLoad(Bundle args){
        progressBar.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(0,args,this).forceLoad();
    }

    @Override public Loader<ResponseObj> onCreateLoader(int id, Bundle args) {
        return new ProductLoader(this,args.getString("url"));
    }

    @Override public void onLoadFinished(Loader<ResponseObj> loader, ResponseObj data) {
        if(data.getStatus() == 200 && data.getProducts()!=null && (productList.size() < (data.getPageNumber() + 10))){
            productList.addAll(data.getProducts());
            adapter.notifyItemRangeChanged(data.getPageNumber(),data.getProducts().size());
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override public void onLoaderReset(Loader<ResponseObj> loader) {


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //This will not work for large amounts of data. Using shared preferences instead. Alternatively we can make the list static
        //or use SQLite database to persist.
        //outState.putParcelableArrayList("productList",productList);
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(productList);
        prefsEditor.putString("productList", json);
        prefsEditor.commit();
        outState.putInt("currentPage",productList.size()-10);
        outState.putInt("previousTotalItemCount",productList.size());
    }
}
