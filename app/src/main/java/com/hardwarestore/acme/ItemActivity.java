package com.hardwarestore.acme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

/**
 * ItemActivity displays the details of an item (Feature, BestSell, or Items).
 * Users can add the item to their cart or purchase it.
 */

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

    }
}
