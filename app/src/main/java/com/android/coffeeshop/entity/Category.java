package com.android.coffeeshop.entity;

import com.google.firebase.firestore.PropertyName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @PropertyName("categoryId")
    private int categoryId;

    @PropertyName("categoryName")
    @NonNull
    private String categoryName;
}