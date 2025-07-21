package com.android.coffeeshop.dao;

import android.content.Context;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public abstract class FirestoreDatabase {
    private static FirestoreDatabase INSTANCE = null;
    protected final FirebaseFirestore db;

    protected FirestoreDatabase(Context context) {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Kích hoạt offline
                .build();
        db.setFirestoreSettings(settings);
    }

    public static synchronized FirestoreDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FirestoreDatabaseImpl(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public abstract CategoryDao categoryDao();
    public abstract OrderDao orderDao();
    public abstract UserDao userDao();
    public abstract RoleDao roleDao();
    public abstract ProductDao productDao();
    public abstract OrderDetailDao orderDetailDao();
    public abstract ScheduleDao scheduleDao();
}

class FirestoreDatabaseImpl extends FirestoreDatabase {
    private final CategoryDao categoryDao;
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final ProductDao productDao;
    private final OrderDetailDao orderDetailDao;
    private final ScheduleDao scheduleDao;

    FirestoreDatabaseImpl(Context context) {
        super(context);
        this.categoryDao = new FirestoreCategoryDao(db);
        this.orderDao = new FirestoreOrderDao(db);
        this.userDao = new FirestoreUserDao(db);
        this.roleDao = new FirestoreRoleDao(db);
        this.productDao = new FirestoreProductDao(db);
        this.orderDetailDao = new FirestoreOrderDetailDao(db);
        this.scheduleDao = new FirestoreScheduleDao(db);
    }

    @Override
    public CategoryDao categoryDao() { return categoryDao; }
    @Override
    public OrderDao orderDao() { return orderDao; }
    @Override
    public UserDao userDao() { return userDao; }
    @Override
    public RoleDao roleDao() { return roleDao; }
    @Override
    public ProductDao productDao() { return productDao; }
    @Override
    public OrderDetailDao orderDetailDao() { return orderDetailDao; }
    @Override
    public ScheduleDao scheduleDao() { return scheduleDao; }
}