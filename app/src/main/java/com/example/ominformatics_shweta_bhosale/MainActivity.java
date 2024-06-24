package com.example.ominformatics_shweta_bhosale;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ominformatics_shweta_bhosale.model.Orders;
import com.example.ominformatics_shweta_bhosale.viewModel.OrderViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OrderViewModel orderViewModel;
    private OrderAdapter adapter;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        adapter = new OrderAdapter();
        recyclerView.setAdapter(adapter);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getAllOrders().observe(this, orders -> {
            adapter.setOrders(orders);
            updateSummary(orders);
        });

        orderViewModel.fetchOrdersFromApi();
    }

    private void checkLocationAndProceed(Orders order) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            float[] results = new float[1];
                            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                                    Double.parseDouble(order.getDeliveryLocation().split(",")[0]),
                                    Double.parseDouble(order.getDeliveryLocation().split(",")[1]), results);
                            if (results[0] <= 50) {
                                // Navigate to delivery page
                                Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
                                intent.putExtra("order", order);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Delivery location is too far!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void updateSummary(List<Orders> orders) {
        int completedDeliveries = 0;
        double totalCollected = 0;
        for (Orders order : orders) {
            if (order.isDelivered()) {
                completedDeliveries++;
                totalCollected += order.getCollectedAmount();
            }
        }

        // Assuming you have TextViews for these in your layout
        TextView txtDeliveriesCompleted = findViewById(R.id.txtDeliveriesCompleted);
        TextView txtCashCollected = findViewById(R.id.txtCashCollected);

        txtDeliveriesCompleted.setText("Deliveries completed: " + completedDeliveries + "/" + orders.size());
        txtCashCollected.setText("Cash collected: ₹" + totalCollected);
    }
}
