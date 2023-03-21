package com.hardwarestore.acme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hardwarestore.acme.domain.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    TextView mTotal;
    Button payBtn;
    double amount = 0.0;
    String name = "";
    String img_url = "";
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    List<Items> itemsList;
    TextView mFinlCost;

    /**
     * This method is called when the activity is first created.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amount = getIntent().getDoubleExtra("amount", 0.0);
        img_url = getIntent().getStringExtra("img_url");
        name = getIntent().getStringExtra("name");
        itemsList = (ArrayList<Items>) getIntent().getSerializableExtra("itemsList");
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mTotal = findViewById(R.id.sub_total);
        payBtn = findViewById(R.id.pay_btn);
        mFinlCost = findViewById(R.id.total_amt);

        if (itemsList != null && itemsList.size() > 0) {
            double subtotal = 0.0;
            for (Items item : itemsList) {
                subtotal += item.getPrice();
            }
            String formattedSubtotal = String.format("%.2f", subtotal);
            mTotal.setText("$ " + formattedSubtotal + "");
            mFinlCost.setText("$ " + formattedSubtotal + "");
            amount = subtotal;
        } else {
            amount = getIntent().getDoubleExtra("amount", 0.0);
            img_url = getIntent().getStringExtra("img_url");
            name = getIntent().getStringExtra("name");
            String formattedAmount = String.format("%.2f", amount);
            mTotal.setText("$ " + formattedAmount + "");
            mFinlCost.setText("$ " + formattedAmount + "");
        }

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithSimStripe();
            }
        });
    }

    /**
     * This method is called when the payment is successful.
     * It saves the order information to Firestore and navigates the user to the home activity.
     * If the payment is for multiple items, it loops through the items and saves each item's order information separately.
     * If the payment is for a single item, it saves the order information for that item only.
     * Finally, if the payment is successful and there are items in the cart, it deletes the cart from Firestore.
     *
     * @param s the payment id
     */
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        if (itemsList != null && itemsList.size() > 0) {
            for (Items items : itemsList) {
                Map<String, Object> mMap = new HashMap<>();
                mMap.put("name", items.getName());
                mMap.put("img_url", items.getImg_url());
                mMap.put("payment_id", s);

                mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                        .collection("Orders").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
            mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                    .collection("Cart").document().delete();
        } else {
            Map<String, Object> mMap = new HashMap<>();
            mMap.put("name", name);
            mMap.put("img_url", img_url);
            mMap.put("payment_id", s);

            mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                    .collection("Orders").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }
    }

    /**
     * This method is called when the user clicks the pay button.
     */
    public void payWithSimStripe() {
        Intent intent = new Intent(PaymentActivity.this, CheckoutActivityJava.class);
        intent.putExtra("amount", amount);
        intent.putExtra("name", name);
        intent.putExtra("img_url", img_url);
        startActivity(intent);
    }

    /**
     * This method is called when the payment is unsuccessful.
     *
     * @param i the error code
     * @param s the error message
     */
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
    }


}



