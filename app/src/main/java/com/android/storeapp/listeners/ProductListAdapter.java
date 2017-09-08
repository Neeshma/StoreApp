package com.android.storeapp.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.storeapp.R;
import com.android.storeapp.model.Product;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Neeshma on 9/7/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductListAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productName.setText(productList.get(position).getProductName());
        if(productList.get(position).getShortDescription()!=null) {
            holder.productDesc.setText(Html.fromHtml(productList.get(position).getShortDescription()));
        }
        if(productList.get(position).getReviewRating()!=null) {
            holder.ratingBar.setRating(Float.parseFloat(productList.get(position).getReviewRating()));
        }
        if(productList.get(position).getPrice()!=null) {
            holder.price.setText(productList.get(position).getPrice());
        }

        Glide.with(context)
                .load(productList.get(position).getProductImage())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productDesc;
        RatingBar ratingBar;
        TextView price;
        ImageView productImage;

        ViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.name);
            productDesc = (TextView) itemView.findViewById(R.id.desc);
            productImage = (ImageView) itemView.findViewById(R.id.img);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}
