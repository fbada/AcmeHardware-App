package com.hardwarestore.acme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CheckoutActivityJava extends AppCompatActivity {

    Double amountDouble = null;
    String name = "";
    String img_url = "";
    private FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    /**
     * This method is called when the activity is first created.
     *
     * @param savedInstanceState The saved instance state.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_java);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        amountDouble = getIntent().getDoubleExtra("amount", 0.0);
        img_url = getIntent().getStringExtra("img_url");
        name = getIntent().getStringExtra("name");

        // Hook up the pay button to simulate a payment
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            simulatePayment();
        });
    }


    /**
     * Simulates a payment for the current order.
     * Saves the order information to Firestore and deletes the items from the user's cart.
     * Starts a new HomeActivity.
     */
    private void simulatePayment() {
// Simulate a payment
        Toast.makeText(this, "Simulating payment...", Toast.LENGTH_LONG).show();
// Save the order information to Firestore
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("name", name);
        mMap.put("img_url", img_url);
        mMap.put("payment_id", "fake_payment_id");
        mStore.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("Orders").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            deleteCart();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            displayAlert("Error", Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
    }

    /**
     * Deletes the items from the user's cart.
     */
    private void deleteCart() {
        mStore.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        document.getReference().delete();
                    }
                    Toast.makeText(CheckoutActivityJava.this, "Cart deleted and reset successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    displayAlert("Error", Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

    /**
     * Displays an alert dialog with the given title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message of the alert dialog.
     */

    private void displayAlert(@NonNull String title, @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(title).setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }
}
