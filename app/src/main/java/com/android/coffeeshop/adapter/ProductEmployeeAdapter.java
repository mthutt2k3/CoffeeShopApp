package com.android.coffeeshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.Product;
import com.android.coffeeshop.utils.FormartCurrency;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductEmployeeAdapter extends RecyclerView.Adapter<ProductEmployeeAdapter.MyViewHolder> {
    private static List<Product> products;
    Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ProductEmployeeAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from((parent.getContext())).inflate(R.layout.product_card_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = products.get(position);
        holder.name.setText(product.getProductName());
        holder.price.setText(FormartCurrency.formatVNCurrency(product.getProductPrice()));

        Picasso.get().load(product.getProductImage()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView price;
        public ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            image = itemView.findViewById(R.id.imageProduct);
            price = itemView.findViewById(R.id.textViewPrice);
        }
    }

    public static List<Product> getListProduct() {
        return products;
    }

    public static void setProductList(List<Product> listProduct) {
        ProductEmployeeAdapter.products = listProduct;
    }

    public static void filter(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : products) {
            if (product.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(product);
            }
        }
        filterList(filteredList);
    }

    public static void filterCategory(int id) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategoryId() == id) {
                filteredList.add(product);
            }
        }
        filterList(filteredList);
    }

    public static void filterList(List<Product> filteredList) {
        products = filteredList;
    }
}
