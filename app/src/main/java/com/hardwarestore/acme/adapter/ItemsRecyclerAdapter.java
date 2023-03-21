package com.hardwarestore.acme.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hardwarestore.acme.DetailActivity;
import com.hardwarestore.acme.HomeActivity;
import com.hardwarestore.acme.R;
import com.hardwarestore.acme.domain.Items;

import java.util.List;

/**
 * A RecyclerView adapter for displaying featured items in a list.
 * This adapter takes in a list of featured items and displays them as a list of images.
 * When a featured item image is clicked, it launches the DetailActivity to display the details of that item.
 */

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder> {
    Context applicationContext;
    List<Items> mItemsList;
    /**
     * Constructs a new FeatureAdapter.
     * @param applicationContext The context of the adapter.
     * @param mItemsList The list of featured items to display.
     */

    public ItemsRecyclerAdapter(Context applicationContext, List<Items> mItemsList) {
        this.applicationContext=applicationContext;
        this.mItemsList=mItemsList;
    }

    /** Creates a new ViewHolder for the adapter.
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the new ViewHolder.
     * @return A new ViewHolder for the adapter.
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(applicationContext).inflate(R.layout.single_item_layout,parent,false);
        return new ViewHolder(view);
    }
/**
     * Binds data to the given ViewHolder.
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the list to bind data from.
     */

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mCost.setText("$ "+mItemsList.get(position).getPrice());
        holder.mName.setText(mItemsList.get(position).getName());
        if(!(applicationContext instanceof HomeActivity)){
            Glide.with(applicationContext).load(mItemsList.get(position).getImg_url()).into(holder.mItemImage);

        }else
        {
            holder.mItemImage.setVisibility(View.GONE);
        }

        holder.mItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(applicationContext, DetailActivity.class);
                intent.putExtra("detail",mItemsList.get(position));
                applicationContext.startActivity(intent);
            }
        });

        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(applicationContext, DetailActivity.class);
                intent.putExtra("detail",mItemsList.get(position));
                applicationContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mItemImage;//item image
        private TextView mCost;//item cost
        private TextView mName;//item name

        /**
         * Constructs a new ViewHolder.
         * @param itemView The view of the ViewHolder.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImage=itemView.findViewById(R.id.item_image);
            mCost=itemView.findViewById(R.id.item_cost);
            mName=itemView.findViewById(R.id.item_name);
        }
    }
}