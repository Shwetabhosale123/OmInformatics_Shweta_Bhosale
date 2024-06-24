package com.example.ominformatics_shweta_bhosale.serviceapi;

import com.example.ominformatics_shweta_bhosale.model.Orderlist;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface OrderApiService {
    @GET("orderlist.php")
    Call<List<Orderlist>> getOrders();
}


