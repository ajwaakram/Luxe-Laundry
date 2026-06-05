 


package com.find.luxelaundary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Model.SubService;
import com.find.luxelaundary.Model.User;
import com.find.luxelaundary.R;

import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.SubServiceViewHolder> {

    private Context context;
    private List<User> subServiceList;
    private OnSubServiceClickListener listener;

    public interface OnSubServiceClickListener {
        void onSubServiceClick(SubService subService);
    }

    public AdminUserAdapter(Context context, List<User> subServiceList) {
        this.context = context;
        this.subServiceList = subServiceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_user_row, parent, false);
        return new SubServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubServiceViewHolder holder, int position) {
        User subService = subServiceList.get(position);
        holder.userName.setText(subService.getName());
        holder.userEmail.setText(subService.getEmail());


    }

    @Override
    public int getItemCount() {
        return subServiceList.size();
    }

    public static class SubServiceViewHolder extends RecyclerView.ViewHolder {
        TextView userName,userEmail;

        public SubServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
        }
    }
}
