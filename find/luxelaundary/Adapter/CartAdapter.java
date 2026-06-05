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

import com.find.luxelaundary.Model.Cart;
import com.find.luxelaundary.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.name.setText(cart.getName());
        holder.qty.setText("Qty: " + cart.getQuantity());
        holder.price.setText("PKR " + cart.getPrice());
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Remove Item")
                    .setMessage("Are you sure you want to remove this item from the cart?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        String cartId = cart.getId(); // Assuming ID is set in model

                        FirebaseDatabase.getInstance().getReference("All_Cart")
                                .child(cartId)
                                .removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
                                    cartList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, cartList.size());

                                    // 👇 Call a callback to notify total update
                                    if (onItemDeletedListener != null) {
                                        onItemDeletedListener.onItemDeleted();
                                    }

                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
    // Interface to notify total price update on deletion
    public interface OnItemDeletedListener {
        void onItemDeleted();
    }

    private OnItemDeletedListener onItemDeletedListener;

    // Setter for the listener
    public void setOnItemDeletedListener(OnItemDeletedListener listener) {
        this.onItemDeletedListener = listener;
    }


    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, qty, price;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            qty = itemView.findViewById(R.id.qty);
            price = itemView.findViewById(R.id.price);
        }
    }
}
