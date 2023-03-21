package com.hardwarestore.acme.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hardwarestore.acme.ItemsActivity;
import com.hardwarestore.acme.R;
import com.hardwarestore.acme.domain.Category;

import java.util.List;

/**

 A RecyclerView adapter for displaying categories in a list.
 This adapter takes in a list of categories and displays them as a list of images.
 When a category image is clicked, it launches the ItemsActivity to display the items in that category.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private List<Category> mCategoryList;

    /**
     Constructs a new CategoryAdapter.
     @param context The context of the adapter.
     @param mCategoryList The list of categories to display.
     */
    public CategoryAdapter(Context context, List<Category> mCategoryList) {
        this.context=context;
        this.mCategoryList=mCategoryList;
    }
    /**
     Creates a new ViewHolder for the adapter.
     @param parent The parent ViewGroup.
     @param viewType The view type of the new ViewHolder.
     @return A new ViewHolder for the adapter.
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_category_item,parent,false);
        return new ViewHolder(view);
    }
    /**
     Binds data to the given ViewHolder.
     @param holder The ViewHolder to bind data to.
     @param position The position of the item in the list to bind data from.
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(mCategoryList.get(position).getImg_url()).into(holder.mTypeImg);
       //load category using glide
        holder.mTypeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch items activity
                Intent intent=new Intent(context, ItemsActivity.class);
                intent.putExtra("type",mCategoryList.get(position).getType());
                context.startActivity(intent);
            }
        });
    }

/**
     Gets the number of items in the list.
     @return The number of items in the list.
     */

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    /**
     A ViewHolder for the CategoryAdapter.
     @return A ViewHolder for the CategoryAdapter.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mTypeImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTypeImg=itemView.findViewById(R.id.category_img);
        }
    }
}
