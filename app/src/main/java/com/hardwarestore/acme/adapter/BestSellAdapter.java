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
import com.hardwarestore.acme.R;
import com.hardwarestore.acme.domain.BestSell;

import java.util.List;


/**

 Adapter class to populate BestSell items in the RecyclerView.
 */
public class BestSellAdapter extends RecyclerView.Adapter<BestSellAdapter.ViewHolder> {
    Context context;
    List<BestSell> mBestSellList;
    /**
     Constructor to initialize BestSellAdapter.
     @param context The context of the activity.
     @param mBestSellList The list of BestSell items.
     */
    public BestSellAdapter(Context context, List<BestSell> mBestSellList) {
        this.context = context;
        this.mBestSellList = mBestSellList;
    }
    /**
     Creates a ViewHolder object by inflating the item view layout.
     @param parent The parent ViewGroup.
     @param viewType The view type of the new View.
     @return The created ViewHolder object.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_bestsell_item, parent, false);
        return new ViewHolder(view);
    }
    /**
     Binds the data of BestSell item to the ViewHolder.
     @param holder The ViewHolder to bind data to.
     @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mName.setText(mBestSellList.get(position).getName());
        holder.mPrice.setText("$ " + (mBestSellList.get(position).getPrice()));
        Glide.with(context).load(mBestSellList.get(position).getImg_url()).into(holder.mImage);
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail", mBestSellList.get(position));
                context.startActivity(intent);
            }
        });
    }
    /**
     Returns the number of items in the list.
     @return The number of items in the list.
     */
    @Override
    public int getItemCount() {
        return mBestSellList.size();
    }
    /**
     ViewHolder class to hold the views of the item view layout.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        TextView mName;
        TextView mPrice;
        /**
         Constructor to initialize ViewHolder.
         @param itemView The View of the item view layout.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.bs_img);
            mName = itemView.findViewById(R.id.bs_name);
            mPrice = itemView.findViewById(R.id.bs_cost);
        }
    }
}