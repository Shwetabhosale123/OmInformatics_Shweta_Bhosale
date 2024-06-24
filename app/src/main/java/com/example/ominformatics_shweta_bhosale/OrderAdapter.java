package com.example.ominformatics_shweta_bhosale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ominformatics_shweta_bhosale.model.Orders;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Orders> orders = new ArrayList<>();

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Orders currentOrder = orders.get(position);
        holder.textViewOrderId.setText("Order ID: " + currentOrder.getOrderId());
        holder.textViewDeliveryLocation.setText("Location: " + currentOrder.getDeliveryLocation());
        holder.textViewDeliveryCharge.setText("Charge: ₹" + currentOrder.getDeliveryCost());
        holder.textViewDeliveryStatus.setText(currentOrder.isDelivered() ? "Status: Delivered" : "Status: Pending");

        // Additional view settings can be done here
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOrderId;
        private TextView textViewDeliveryLocation;
        private TextView textViewDeliveryCharge;
        private TextView textViewDeliveryStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewDeliveryLocation = itemView.findViewById(R.id.textViewDeliveryLocation);
            textViewDeliveryCharge = itemView.findViewById(R.id.textViewDeliveryCharge);
            textViewDeliveryStatus = itemView.findViewById(R.id.textViewDeliveryStatus);


        }
    }
}
