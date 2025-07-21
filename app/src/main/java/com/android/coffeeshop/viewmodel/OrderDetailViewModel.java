package com.android.coffeeshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.coffeeshop.entity.OrderDetail;
import com.android.coffeeshop.repository.OrderDetailRepository;

import java.util.List;

public class OrderDetailViewModel extends AndroidViewModel {
    private OrderDetailRepository orderDetailRepository;

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        orderDetailRepository = new OrderDetailRepository(application);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        return orderDetailRepository.getOrderDetailByOrderId(orderId);
    }
}
