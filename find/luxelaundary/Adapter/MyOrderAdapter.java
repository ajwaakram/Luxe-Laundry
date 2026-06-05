package com.find.luxelaundary.Adapter;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Model.Order;
import com.find.luxelaundary.OrderDetailActivity;
import com.find.luxelaundary.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder> {

    private List<Order> orders = new ArrayList<>();
    String role;

    public void setOrders(List<Order> orders,String role) {
        this.orders = orders;
        this.role = role;
        notifyDataSetChanged();
    }

    public void filterByStatus(String status) {
        if (status.equalsIgnoreCase("All")) {
            // show all
            notifyDataSetChanged();
            return;
        }

        List<Order> filteredList = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStatus() != null && o.getStatus().equalsIgnoreCase(status)) {
                filteredList.add(o);
            }
        }
        orders = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_row, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText("Order ID: 101" + holder.getAdapterPosition());
        holder.tvDate.setText("Date: " + order.getDate());
        holder.tvStatus.setText(order.getStatus());
        holder.tvTotalBill.setText("Total: " + order.getTotalBill());
        holder.tvAddress.setText("Address: " + order.getAddress());
        holder.tvPhone.setText("Phone: " + order.getPhoneNumber());

        // Change status color
        switch (order.getStatus()) {
            case "Pending":
                holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "Completed":
                holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "Canceled":
                holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
                break;
        }
        if(role.matches("0")){
            holder.actions.setVisibility(INVISIBLE);
        }
        else{
            holder.actions.setVisibility(VISIBLE);
        }
        holder.actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_update_status, null);
                builder.setView(dialogView);

                RadioGroup statusGroup = dialogView.findViewById(R.id.statusGroup);
                Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
                Button btnCancel = dialogView.findViewById(R.id.btnCancel);

                AlertDialog dialog = builder.create();

                // Pre-select current status
                switch (order.getStatus()) {
                    case "Pending":
                        statusGroup.check(R.id.radioPending);
                        break;
                    case "Completed":
                        statusGroup.check(R.id.radioCompleted);
                        break;
                    case "Canceled":
                        statusGroup.check(R.id.radioCanceled);
                        break;
                }

                btnUpdate.setOnClickListener(view -> {
                    int selectedId = statusGroup.getCheckedRadioButtonId();
                    String newStatus = "";
                    if (selectedId == R.id.radioPending) {
                        newStatus = "Pending";
                    } else if (selectedId == R.id.radioCompleted) {
                        newStatus = "Completed";
                    } else if (selectedId == R.id.radioCanceled) {
                        newStatus = "Canceled";
                    }

                    // ✅ Update Firebase based on Order ID
                    FirebaseDatabase.getInstance().getReference("All_Order")
                            .child(order.getOrderId()) // assumes orderId is used as the key
                            .child("status")
                            .setValue(newStatus)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Status updated!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                });

                btnCancel.setOnClickListener(view -> dialog.dismiss());

                dialog.show();
            }
        });

        // Set click listener to navigate to detail screen
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderData", order); // passing order as Serializable
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView actions,tvOrderId, tvDate, tvStatus, tvTotalBill, tvAddress, tvPhone;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            actions = itemView.findViewById(R.id.actions);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotalBill = itemView.findViewById(R.id.tvTotalBill);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }
}
