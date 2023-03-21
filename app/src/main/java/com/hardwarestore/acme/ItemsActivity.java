package com.hardwarestore.acme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.hardwarestore.acme.adapter.ItemsRecyclerAdapter;
import com.hardwarestore.acme.domain.Items;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    private FirebaseFirestore mStore;
    List<Items> mItemsList;
    private RecyclerView itemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;
    private Toolbar mToolbar;

    /**
     * This method is called when the Items Activity is created.
     * It sets the layout of the activity and initializes the UI components.
     * It also sets up the search functionality to search for items in Firestore database.
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        String type = getIntent().getStringExtra("type");
        mStore = FirebaseFirestore.getInstance();
        mItemsList = new ArrayList<>();
        mToolbar = findViewById(R.id.item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Items");

        itemRecyclerView = findViewById(R.id.items_recycler);
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(this, mItemsList);
        itemRecyclerView.setAdapter(itemsRecyclerAdapter);
        if (type == null || type.isEmpty()) {
            mStore.collection("All").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            Log.i("TAG", "onComplete: " + doc.toString());
                            Items items = doc.toObject(Items.class);
                            mItemsList.add(items);
                            itemsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        // If the type is not null, then it will query the database for the items of that type.
        if (type != null && type.equalsIgnoreCase("garden")) {
            mStore.collection("All").whereEqualTo("type", "garden").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            Log.i("TAG", "onComplete: " + doc.toString());
                            Items items = doc.toObject(Items.class);
                            mItemsList.add(items);
                            itemsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        // If the type is not null, then it will query the database for the items of that type.
        if (type != null && type.equalsIgnoreCase("tools")) {
            mStore.collection("All").whereEqualTo("type", "tools").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            Log.i("TAG", "onComplete: " + doc.toString());
                            Items items = doc.toObject(Items.class);
                            mItemsList.add(items);
                            itemsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        // If the type is not null, then it will query the database for the items of that type.
        if (type != null && type.equalsIgnoreCase("paint")) {
            mStore.collection("All").whereEqualTo("type", "paint").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            Log.i("TAG", "onComplete: " + doc.toString());
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
     * This method is called when the options menu is created.
     * It sets up the search functionality to search for items in Firestore database.
     *
     * @param menu A Menu object containing the menu items.
     * @return A boolean value indicating whether the menu was created successfully.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_it);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method is called when the user clicks on the search icon.
     * It searches for the item in the Firestore database.
     *
     * @param text A String object containing the text to be searched.
     */
    private void searchItem(String text) {
        if(!text.isEmpty()){
            mItemsList.clear();
            // make the search not case sensitive
            text=text.substring(0,1).toUpperCase()+text.substring(1).toLowerCase();
            mStore.collection("All").whereGreaterThanOrEqualTo("name",text).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && task.getResult()!=null){
                                for(DocumentSnapshot doc:task.getResult().getDocuments()){
                                    Items items=doc.toObject(Items.class);
                                    mItemsList.add(items);
                                    itemsRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

    }
    }


