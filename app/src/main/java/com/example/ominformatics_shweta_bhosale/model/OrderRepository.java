package com.example.ominformatics_shweta_bhosale.model;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ominformatics_shweta_bhosale.OrderDao;
import com.example.ominformatics_shweta_bhosale.OrderDatabase;
import com.example.ominformatics_shweta_bhosale.serviceapi.OrderApiService;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderRepository {
    private OrderApiService apiService;
    private OrderDao orderDao;
    private LiveData<List<Orders>> allOrders;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public OrderRepository(Application application) {
        OrderDatabase db = OrderDatabase.getDatabase(application);
        orderDao = db.orderDao();
        allOrders = orderDao.getAllOrders();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ominfo.in/test_app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(OrderApiService.class);
    }

    public LiveData<List<Orders>> getAllOrders() {
        return allOrders;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchOrdersFromApi() {
        apiService.getOrders().enqueue(new Callback<List<Orderlist>>() {
            @Override
            public void onResponse(Call<List<Orderlist>> call, Response<List<Orderlist>> response) {
                if (response.isSuccessful()) {
                    List<Orders> orders = convertToOrders(response.body());
                    new InsertOrdersAsyncTask(orderDao).execute(orders);
                } else {
                    errorMessage.postValue("Failed to fetch orders: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Orderlist>> call, Throwable t) {
                errorMessage.postValue("Failed to fetch orders: " + t.getMessage());
                Log.e("OrderRepository", "Error fetching orders", t);
            }
        });
    }

    private List<Orders> convertToOrders(List<Orderlist> orderlist) {

        return null;
    }

    private static class InsertOrdersAsyncTask extends AsyncTask<List<Orders>, Void, Void> {
        private OrderDao orderDao;

        InsertOrdersAsyncTask(OrderDao orderDao) {
            this.orderDao = orderDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Orders>... lists) {
            orderDao.insertAll(lists[0]);
            return null;
        }
    }
}
