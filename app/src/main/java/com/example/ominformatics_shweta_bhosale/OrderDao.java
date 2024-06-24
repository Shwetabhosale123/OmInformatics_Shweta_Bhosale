package com.example.ominformatics_shweta_bhosale;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ominformatics_shweta_bhosale.model.Orders;

import java.util.List;

@Dao
public interface OrderDao {

    @Query("SELECT * FROM orders")
    LiveData<List<Orders>> getAllOrders();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Orders> orders);

    @Update
    void updateOrder(Orders order);
}
