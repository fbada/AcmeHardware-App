package com.hardwarestore.acme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity class for adding a new address to the user's account.
 */
public class AddAddressActivity extends AppCompatActivity {
    // EditText fields for entering the new address information
    private EditText mName;
    private EditText mCity;
    private EditText mAddress;
    private EditText mCode;
    private EditText mNumber;

    // Button for adding the new address
    private Button mAddAddressbtn;

    // Firestore instance for accessing the database
    private FirebaseFirestore mStore;

    // Firebase Authentication instance for checking the user's login status
    private FirebaseAuth mAuth;

    // Toolbar for navigating back to the previous activity
    private Toolbar mToolbar;

    /**
     * Called when the activity is being created.
     * Initializes views and listeners for adding a new address to the user's account.
     *
     * @param savedInstanceState Bundle object containing saved state information
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        // Find views in the layout and assign them to member variables
        mName = findViewById(R.id.ad_name);
        mCity = findViewById(R.id.ad_city);
        mAddress = findViewById(R.id.ad_address);
        mCode = findViewById(R.id.ad_code);
        mNumber = findViewById(R.id.ad_phone);
        mAddAddressbtn = findViewById(R.id.ad_add_address);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add a click listener to the "Add Address" button
        mAddAddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the entered address information
                String name = mName.getText().toString();
                String city = mCity.getText().toString();
                String address = mAddress.getText().toString();
                String code = mCode.getText().toString();
                String number = mNumber.getText().toString();
                String final_address = "";

                // Construct the final address string by concatenating the city, address, and postal code fields
                if (!city.isEmpty()) {
                    final_address += city + ", ";
                }
                if (!address.isEmpty()) {
                    final_address += address + ", ";
                }
                if (!code.isEmpty()) {
                    final_address += code + ", ";
                }

                // Store the final address string in a HashMap
                Map<String, String> mMap = new HashMap<>();
                mMap.put("address", final_address);

                // Add the new address to the user's account in the Firestore database
                mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                        .collection("Address").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddAddressActivity.this, "Address Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });

            }
        });
    }
}
