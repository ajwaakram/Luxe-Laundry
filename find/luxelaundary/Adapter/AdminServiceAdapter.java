package com.find.luxelaundary.Adapter;

 
 
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Admin.addSubServices;
import com.find.luxelaundary.Model.Service;
import com.find.luxelaundary.R;
import com.find.luxelaundary.services_user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminServiceAdapter extends RecyclerView.Adapter<AdminServiceAdapter.ViewHolder> {

    private Context context;
    private List<Service> serviceList;

    public AdminServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.services_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.serviceName.setText(service.getServiceName());
        Picasso.get().load(service.getImgUrl()).into(holder.serviceImg);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, addSubServices.class); // Replace with your activity
            intent.putExtra("id", service.getId());
            intent.putExtra("name", service.getServiceName());
            context.startActivity(intent);
        });


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, addSubServices.class);
            intent.putExtra("id", service.getId());
            intent.putExtra("name", service.getServiceName());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Service")
                    .setMessage("Are you sure you want to delete this service?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference("All_Services");

                        // Delete item where id == service.getId()
                        ref.orderByChild("id")
                                .equalTo(service.getId())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                child.getRef().removeValue();
                                            }
                                            Toast.makeText(context, "Service deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Service not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true; // Important: indicates long click was handled
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImg;
        TextView serviceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImg = itemView.findViewById(R.id.serviceImg);
            serviceName = itemView.findViewById(R.id.serviceName);
        }
    }
}