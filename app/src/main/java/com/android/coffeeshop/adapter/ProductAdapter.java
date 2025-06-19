package com.android.coffeeshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.entity.Product;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnDeleteClickListener onDeleteClickListener;
    private OnEditClickListener onEditClickListener;

    public ProductAdapter(Context context, List<Product> productList, OnDeleteClickListener onDeleteClickListener, OnEditClickListener onEditClickListener) {
        this.context = context;
        this.productList = productList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onEditClickListener = onEditClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getProductName());
       // Format price -->  VND
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(product.getProductPrice());
        if (formattedPrice.endsWith(".0")) {
            formattedPrice = formattedPrice.substring(0, formattedPrice.length() - 2);
        }
        holder.tvProductPrice.setText(formattedPrice);
        if (product.getProductImage() != null && !product.getProductImage().isEmpty()) {
            File imgFile = new File(product.getProductImage());
            if (imgFile.exists()) {
                Picasso.get()
                        .load(imgFile)
                        .placeholder(R.drawable.img_avatar)
                        .error(R.drawable.img_error)
                        .into(holder.ivProductImage);
            } else {
                holder.ivProductImage.setImageResource(R.drawable.img_error);
            }
        } else {
            holder.ivProductImage.setImageResource(R.drawable.img_error);
        }

        holder.tvProductName.setOnClickListener(v -> {
            onEditClickListener.onEditClick(product.getProductId(), product.getProductName(),
                    product.getProductPrice(), product.getProductImage(), product.getCategoryId(),
                    product.getStockQuantity(), product.getProductRecipes(), product.isStatus());
        });
        holder.tvAction.setOnClickListener(v -> {
            onDeleteClickListener.onDeleteClick(product.getProductId(), product.getProductName());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName, tvProductPrice;
        ImageView ivProductImage;
        Button tvAction;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvAction = itemView.findViewById(R.id.tvAction);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int productId, String productName);
    }

    public interface OnEditClickListener {
        void onEditClick(int productId, String productName, double productPrice, String productImage,
                         int categoryId, int stockQuantity, String productRecipes, boolean status);
    }
}
