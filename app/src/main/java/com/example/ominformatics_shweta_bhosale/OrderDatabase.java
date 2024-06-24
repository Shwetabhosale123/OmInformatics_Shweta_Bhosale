package com.example.ominformatics_shweta_bhosale;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ominformatics_shweta_bhosale.model.Orders;

@Database(entities = {Orders.class}, version = 1, exportSchema = false)
public abstract class OrderDatabase extends RoomDatabase {
    public abstract OrderDao orderDao();

    private static volatile OrderDatabase INSTANCE;

    public static OrderDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (OrderDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    OrderDatabase.class, "order_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

