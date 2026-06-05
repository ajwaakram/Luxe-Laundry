 
package com.find.luxelaundary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.find.luxelaundary.Model.FeedbackModel;
import com.find.luxelaundary.Model.SubService;
import com.find.luxelaundary.Model.User;
import com.find.luxelaundary.R;

import java.util.List;

public class Admin_FeedbackAdapter extends RecyclerView.Adapter<Admin_FeedbackAdapter.SubServiceViewHolder> {

    private Context context;
    //
    private List<FeedbackModel> subServiceList;
    private OnSubServiceClickListener listener;

    public interface OnSubServiceClickListener {
        void onSubServiceClick(SubService subService);
    }

    public Admin_FeedbackAdapter(Context context, List<FeedbackModel> subServiceList) {
        this.context = context;
        this.subServiceList = subServiceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_feedback_row, parent, false);
        return new SubServiceViewHolder(view);
    }

    @Override
    //list m s data fetch kr k  design
    public void onBindViewHolder(@NonNull SubServiceViewHolder holder, int position) {
        FeedbackModel subService = subServiceList.get(position);
        holder.userName.setText("By: "+subService.getUserId());
        holder.userRating.setText(subService.getRating());
        holder.userReview.setText(subService.getReview());
    }

    @Override
    public int getItemCount() {
        return subServiceList.size();
    }

    public static class SubServiceViewHolder extends RecyclerView.ViewHolder {
        TextView userName,userRating,userReview;

        public SubServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userRating = itemView.findViewById(R.id.userRating);
            userReview = itemView.findViewById(R.id.userReview);
        }
    }
}
