package com.android.coffeeshop.dao;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public interface BaseDao {
    // Chạy Task trong luồng nền, trả về kết quả
    default <T> T executeFirestoreTask(Task<T> task) {
        CompletableFuture<T> future = CompletableFuture.supplyAsync(() -> {
            try {
                return Tasks.await(task);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }, Executors.newFixedThreadPool(2));
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Chạy Task không trả về giá trị
    default void executeFirestoreTaskVoid(Task<Void> task) {
        CompletableFuture.runAsync(() -> {
            try {
                Tasks.await(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, Executors.newFixedThreadPool(2));
    }
}