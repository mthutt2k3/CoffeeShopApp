package com.android.coffeeshop.dao;

import com.android.coffeeshop.utils.UserWithRole;

public interface OnUserFetchListener {
    void onSuccess(UserWithRole user);
    void onFailure(Exception e);
}