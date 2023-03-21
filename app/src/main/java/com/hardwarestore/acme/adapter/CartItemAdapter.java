package com.hardwarestore.acme.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hardwarestore.acme.R;
import com.hardwarestore.acme.domain.Items;

import java.util.List;
/**

 A RecyclerView adapter for displaying items in a shopping cart.

 This adapter takes in a list of items to display and an interface for handling item removal.

 It uses Firebase to delete items from the user's cart when the "remove" button is clicked.
 */
public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    List<Items> itemsList; // The list of items to display
    FirebaseFirestore firebaseFirestore; // Firebase Firestore instance

    FirebaseAuth firebaseAuth;// Firebase Authentication instance
    ItemRemoved itemRemoved;// Interface for handling item removal

    /**

     Constructs a new CartItemAdapter.
     @param itemsList The list of items to display in the adapter.
     @param itemRemoved The interface for handling item removal.
     */
    public CartItemAdapter(List<Items> itemsList, ItemRemoved itemRemoved){
        this.itemsList = itemsList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        this.itemRemoved= itemRemoved;
    }

    /**
     Creates a new ViewHolder for the adapter.
     @param parent The parent ViewGroup.
     @param viewType The view type of the new ViewHolder.
     @return A new ViewHolder for the adapter.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item,parent,false);
        return new ViewHolder(view);
    }

    /**

     Binds data to the given ViewHolder.
     @param holder The ViewHolder to bind data to.
     @param position The position of the item in the list to bind data from.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cartName.setText(itemsList.get(position).getName());
        holder.cartPrice.setText("$ "+itemsList.get(position).getPrice());
        Glide.with(holder.itemView.getContext()).load(itemsList.get(position).getImg_url()).into(holder.cartImage);
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                        .collection("Cart").document(itemsList.get(position).getDocId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        // Remove the item from the list and notify the adapter that the
                        // data has changed
                        itemsList.remove(itemsList.get(position));
                        notifyDataSetChanged();
                        itemRemoved.onItemRemoved(itemsList);
                        Toast.makeText(holder.itemView.getContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(holder.itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    }
                });
            }
        });
    }

    /**

     Gets the number of items in the list.
     @return The number of items in the list.
     */
    @Override
    public int getItemCount() {
        return itemsList.size();
    }
    /**
     A ViewHolder for the CartItemAdapter.
     This ViewHolder contains views for displaying an item in the shopping cart.
     */

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage;
        TextView cartName;
        TextView cartPrice;
        TextView removeItem;

        /**
         Constructs a new ViewHolder.
         @param itemView The view for the ViewHolder.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cart_image);//item's image
            cartName = itemView.findViewById(R.id.cart_name);//item's name
            cartPrice = itemView.findViewById(R.id.cart_price);//item's price
            removeItem = itemView.findViewById(R.id.remove_item);//remove item button
        }
    }
    /**
     An interface for handling item removal.
     This interface contains a method for handling item removal.
     */
    public interface ItemRemoved{
        public void onItemRemoved(List<Items> itemsList);

    }
}
