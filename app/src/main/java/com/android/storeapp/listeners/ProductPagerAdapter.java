package com.android.storeapp.listeners;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.storeapp.model.Product;
import com.android.storeapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Neeshma on 9/7/2017.
 */

public class ProductPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Product> itemList;

    public ProductPagerAdapter(Context context, List<Product> itemList) {
        mContext = context;
        this.itemList = itemList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Product item = itemList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.product_pager_layout, collection, false);
        ImageView imgView = (ImageView) layout.findViewById(R.id.productImg);
        Glide.with(mContext)
                .load(itemList.get(position).getProductImage())
                .into(imgView);
        imgView.setScaleType(ImageView.ScaleType.CENTER);
        TextView txtName = (TextView) layout.findViewById(R.id.productName);
        txtName.setText(itemList.get(position).getProductName());
        TextView txtDesc = (TextView) layout.findViewById(R.id.longDesc);
        if(itemList.get(position).getLongDescription()!=null) {
            txtDesc.setText(Html.fromHtml(itemList.get(position).getLongDescription()));
        }
        RatingBar ratingBar = (RatingBar) layout.findViewById(R.id.rating);
        if(itemList.get(position).getReviewRating()!=null) {
            ratingBar.setRating(Float.parseFloat(itemList.get(position).getReviewRating()));
        }
        TextView reviewCnt = (TextView) layout.findViewById(R.id.reviewCnt);
        if(itemList.get(position).getReviewCount()!=null) {
            reviewCnt.setText("("+itemList.get(position).getReviewCount()+")");
        }
        TextView price = (TextView) layout.findViewById(R.id.price);
        if(itemList.get(position).getPrice()!=null) {
            price.setText(itemList.get(position).getPrice());
        }
        ImageView inStock = (ImageView) layout.findViewById(R.id.inStockImg);
        if(itemList.get(position).isInStock()){
            inStock.setVisibility(View.VISIBLE);
        }else{
            inStock.setVisibility(View.GONE);
        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
