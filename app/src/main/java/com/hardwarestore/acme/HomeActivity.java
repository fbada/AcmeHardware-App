package com.hardwarestore.acme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hardwarestore.acme.adapter.ItemsRecyclerAdapter;
import com.hardwarestore.acme.domain.Items;
import com.hardwarestore.acme.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Fragment homeFragment;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private EditText mSearchtext;
    private FirebaseFirestore mStore;
    private List<Items> mItemsList;
    private RecyclerView mItemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;


    /**
     * This method is called when the Home Activity is created.
     * It sets the layout of the activity and initializes the UI components.
     * It also sets up the search functionality to search for items in Firestore database.
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeFragment = new HomeFragment();
        loadFragment(homeFragment);
        mAuth = FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        mSearchtext = findViewById(R.id.search_text);
        mStore = FirebaseFirestore.getInstance();
        mItemsList = new ArrayList<>();
        mItemRecyclerView = findViewById(R.id.search_recycler);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(this, mItemsList);
        mItemRecyclerView.setAdapter(itemsRecyclerAdapter);
        /**
         Sets up a TextWatcher on the search text field to detect when the user inputs text.
         When the text is changed, it clears the mItemsList and searches for items in Firestore
         that match the search text. It then adds the search results to the mItemsList and
         updates the recycler view.
         */
        mSearchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    mItemsList.clear();
                    itemsRecyclerAdapter.notifyDataSetChanged();
                } else {
                    searchItem(s.toString());
                }
            }
        });
    }

    /**
     * Searches for items in the Firestore database that match the search text.
     * It first clears the mItemsList and then queries the Firestore database for items that have
     * a name greater than or equal to the search text. The search is not case sensitive.
     * The search results are then added to the mItemsList and the recycler view is updated.
     *
     * @param text A String containing the search text.
     */
    private void searchItem(String text) {
        if (!text.isEmpty()) {
            mItemsList.clear();
// make the search not case sensitive
            text = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
            mStore.collection("All").whereGreaterThanOrEqualTo("name", text).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    Items items = doc.toObject(Items.class);
                                    mItemsList.add(items);
                                    itemsRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
    }

    /**
     * Loads the Home Fragment into the home_container FrameLayout.
     *
     * @param fragment A Fragment object containing the Home Fragment.
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Creates the options menu for the Home Activity.
     *
     * @param menu A Menu object containing the options menu.
     * @return A boolean value indicating whether the menu was created successfully.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handles the click events for the options menu items.
     *
     * @param item A MenuItem object containing the menu item that was clicked.
     * @return A boolean value indicating whether the click event was handled successfully.
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_btn) {
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.cart) {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        }
        return true;
    }
}