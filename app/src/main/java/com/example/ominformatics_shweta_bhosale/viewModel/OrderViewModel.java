package com.example.ominformatics_shweta_bhosale.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ominformatics_shweta_bhosale.model.OrderRepository;
import com.example.ominformatics_shweta_bhosale.model.Orders;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository repository;
    private LiveData<List<Orders>> allOrders;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderRepository(getApplication());
        allOrders = repository.getAllOrders();
    }

    public LiveData<List<Orders>> getAllOrders() {
        return allOrders;
    }

    public void fetchOrdersFromApi() {
        repository.fetchOrdersFromApi();
    }
}

