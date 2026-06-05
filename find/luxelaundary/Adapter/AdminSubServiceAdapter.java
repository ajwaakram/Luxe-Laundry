 
package com.find.luxelaundary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Model.SubService;
import com.find.luxelaundary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdminSubServiceAdapter extends RecyclerView.Adapter<AdminSubServiceAdapter.SubServiceViewHolder> {

    private Context context;
    private List<SubService> subServiceList;

    public interface OnSubServiceClickListener {
        void onSubServiceClick(SubService subService);
    }

    public AdminSubServiceAdapter(Context context, List<SubService> subServiceList) {
        this.context = context;
        this.subServiceList = subServiceList;
    }

    @NonNull
    @Override
    public SubServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_service_row, parent, false);
        return new SubServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubServiceViewHolder holder, int position) {
        SubService subService = subServiceList.get(position);
        holder.name.setText(subService.getName());

        holder.name.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Service")
                    .setMessage("Are you sure you want to delete this Sub service?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference("All_Sub_Services");

                        ref.orderByChild("id")
                                .equalTo(subService.getId())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                child.getRef().removeValue();
                                            }

                                            // Remove from local list and notify adapter
                                            subServiceList.remove(holder.getAdapterPosition());
                                            notifyItemRemoved(holder.getAdapterPosition());
                                            notifyItemRangeChanged(holder.getAdapterPosition(), subServiceList.size());

                                            Toast.makeText(context, "Sub Service deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Sub Service not found", Toast.LENGTH_SHORT).show();
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

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return subServiceList.size();
    }

    public static class SubServiceViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public SubServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.subServiceName);
        }
    }
}
