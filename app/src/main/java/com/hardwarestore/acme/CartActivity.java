package com.hardwarestore.acme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hardwarestore.acme.AddressActivity;
import com.hardwarestore.acme.R;
import com.hardwarestore.acme.adapter.CartItemAdapter;
import com.hardwarestore.acme.domain.Items;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.ItemRemoved {
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    List<Items> itemsList;
    RecyclerView cartRecyclerView;
    CartItemAdapter cartItemAdapter;
    Button buyItemButton;
    TextView totalAmount;

    /**

     This method is called when the activity is created. It initializes the activity by
     setting the layout, retrieving the cart items from Firestore, setting up the RecyclerView
     to display the cart items, and setting the total amount of the cart.
     @param savedInstanceState a Bundle object containing the activity's previously saved state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        itemsList=new ArrayList<>();
        cartRecyclerView =findViewById(R.id.cart_item_container);
        buyItemButton= findViewById(R.id.cart_item_buy_now);
        totalAmount =findViewById(R.id.total_amount);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setHasFixedSize(true);

        buyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CartActivity.this, AddressActivity.class);
                intent.putExtra("itemList", (Serializable) itemsList);
                startActivity(intent);
            }
        });

        cartItemAdapter = new CartItemAdapter(itemsList,this);
        cartRecyclerView.setAdapter(cartItemAdapter);
        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult()!=null){
                                for(DocumentChange doc :task.getResult().getDocumentChanges()){
                                    String documentId = doc.getDocument().getId();
                                    Items item = doc.getDocument().toObject(Items.class);
                                    item.setDocId(documentId);
                                    itemsList.add(item);
                                }
                                calculateAmount(itemsList);
                                cartItemAdapter.notifyDataSetChanged();
                            }

                        }else{
                            Toast.makeText(CartActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /**
     This method calculates the total amount of the cart items.
     @param itemsList a List of Items objects
     */
    private void calculateAmount(List<Items> itemsList) {
        double totalAmountInDouble = 0.0;
        for(Items item:itemsList){
            totalAmountInDouble += item.getPrice();
        }
        String formattedAmount = String.format("%.2f", totalAmountInDouble);
        totalAmount.setText("Total Amount : $"+formattedAmount);
    }

    /**
     This method is called when an item is removed from the cart. It calculates the total amount
     of the cart items.
     @param itemsList a List of Items objects
     */
    @Override
    public void onItemRemoved(List<Items> itemsList) {
        calculateAmount(itemsList);
    }
}