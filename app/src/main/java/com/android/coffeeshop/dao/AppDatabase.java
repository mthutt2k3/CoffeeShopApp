package com.android.coffeeshop.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.android.coffeeshop.entity.Category;
import com.android.coffeeshop.entity.Order;
import com.android.coffeeshop.entity.OrderDetail;
import com.android.coffeeshop.entity.Product;
import com.android.coffeeshop.entity.Role;
import com.android.coffeeshop.entity.Schedule;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.entity.UserRole;
import com.android.coffeeshop.utils.Converters;

@Database(entities = {User.class, Role.class, UserRole.class, Product.class, Order.class,
        Category.class, OrderDetail.class, Schedule.class}, version = 19, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE = null;

    public abstract UserDao userDao();
    public abstract RoleDao roleDao();
    public abstract UserRoleDao userRoleDao();
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract CategoryDao categoryDao();
    public abstract OrderDetailDao orderDetailDao();
    public abstract ScheduleDao scheduleDao();
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "coffee_shop")
//                    .fallbackToDestructiveMigration()
                    .createFromAsset("coffee_shop_schema.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
