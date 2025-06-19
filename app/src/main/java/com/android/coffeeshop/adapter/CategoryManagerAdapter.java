package com.android.coffeeshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.android.coffeeshop.R;
import com.android.coffeeshop.activity.CategoryListManagerActivity;
import com.android.coffeeshop.entity.Category;
import com.android.coffeeshop.utils.UserWithRole;
import com.android.coffeeshop.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagerAdapter extends RecyclerView.Adapter<CategoryManagerAdapter.MyViewHolder> {
    private static List<Category> categories;
    private int userId;
    private UserViewModel userViewModel;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static List<Category> getListCategory() {
        return categories;
    }

    public static void setCategoryList(List<Category> categories) {
        CategoryManagerAdapter.categories = categories;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Button deleteButton;
        CoordinatorLayout coordinatorLayout;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.categoryName);
            deleteButton = (Button) view.findViewById(R.id.deleteCategory);
            coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.categoryBottomSheetLayout);
        }

    }

    public CategoryManagerAdapter(List<Category> categories, int userId) {
        this.categories = categories;
        this.userId = userId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from((parent.getContext())).inflate(R.layout.category_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category category = categories.get(position);

        // Initialize userViewModel using ViewModelProvider
        userViewModel = new ViewModelProvider((ViewModelStoreOwner) holder.itemView.getContext()).get(UserViewModel.class);
        UserWithRole userWithRole = userViewModel.getUserWithRoleById(userId);


        holder.name.setText(category.getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(category);
                }
            }
        });

        // Call delete method from Activity
        holder.deleteButton.setOnClickListener(v -> {
            if (holder.itemView.getContext() instanceof CategoryListManagerActivity) {
                ((CategoryListManagerActivity) holder.itemView.getContext()).showDeleteDialog(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();

    }

    public static List<Category> filterData(Category clickedCategory) {
        List<Category> filteredCategories = new ArrayList<>();
        for (Category category : categories) {
            if (category.getCategoryId() == clickedCategory.getCategoryId()) {
                filteredCategories.add(category);
            }
        }
        return filteredCategories;
    }

    public static void filter(String text) {
        List<Category> filteredList = new ArrayList<>();
        for (Category category : categories) {
            if (category.getCategoryName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(category);
            }
        }
        filterList(filteredList);
    }

    public static void filterList(List<Category> filteredList) {
        categories = filteredList;
    }
}
