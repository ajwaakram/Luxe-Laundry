package com.find.luxelaundary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Model.SubService;
import com.find.luxelaundary.R;

import java.util.List;

public class SubServiceAdapter extends RecyclerView.Adapter<SubServiceAdapter.SubServiceViewHolder> {

    private Context context;
    private List<SubService> subServiceList;
    private OnSubServiceClickListener listener;

    public interface OnSubServiceClickListener {
        void onSubServiceClick(SubService subService);
    }

    public SubServiceAdapter(Context context, List<SubService> subServiceList, OnSubServiceClickListener listener) {
        this.context = context;
        this.subServiceList = subServiceList;
        this.listener = listener;
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

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSubServiceClick(subService);
            }
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
