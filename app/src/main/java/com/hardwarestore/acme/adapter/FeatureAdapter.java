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
import com.hardwarestore.acme.domain.Feature;

import java.util.List;

/**
 * A RecyclerView adapter for displaying featured items in a list.
 * This adapter takes in a list of featured items and displays them as a list of images.
 * When a featured item image is clicked, it launches the DetailActivity to display the details of that item.
 */

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {
    Context context;
    List<Feature> mFeatureList;
    /**
     * Constructs a new FeatureAdapter.
     * @param context The context of the adapter.
     * @param mFeatureList The list of featured items to display.
     */

    public FeatureAdapter(Context context, List<Feature> mFeatureList) {
        this.context=context;
        this.mFeatureList=mFeatureList;
    }
/** Creates a new ViewHolder for the adapter.
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the new ViewHolder.
     * @return A new ViewHolder for the adapter.
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_feature_item,parent,false);

        return new ViewHolder(view);
    }
/**
     * Binds data to the given ViewHolder.
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the list to bind data from.
     */

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mFetCost.setText("$ "+ mFeatureList.get(position).getPrice());
        holder.mFetName.setText(mFeatureList.get(position).getName());
        Glide.with(context).load(mFeatureList.get(position).getImg_url()).into(holder.mFetImage);
        holder.mFetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("detail",mFeatureList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //method to return the size of the list
        return mFeatureList.size();
    }

    /**
     * A ViewHolder for the adapter.
     * This class holds the views for a single item in the list.
     * @return A new ViewHolder for the adapter.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mFetImage;
        TextView mFetCost;
        TextView mFetName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFetImage=itemView.findViewById(R.id.f_img);
            mFetCost=itemView.findViewById(R.id.f_cost);
            mFetName=itemView.findViewById(R.id.f_name);
        }
    }

}
