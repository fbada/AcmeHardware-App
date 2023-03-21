package com.hardwarestore.acme.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.hardwarestore.acme.R;
import com.hardwarestore.acme.domain.Address;


import java.util.List;


/**
 * Adapter class for a RecyclerView of addresses.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    Context applicationContext;
    List<Address> mAddressList;
    private RadioButton mSelectedRadioButton;
    SelectedAddress selectedAddress;

    /**
     * Constructor for the adapter.
     * @param applicationContext the context of the application
     * @param mAddressList the list of addresses to display
     * @param selectedAddress the interface to handle address selection
     */
    public AddressAdapter(Context applicationContext, List<Address> mAddressList, SelectedAddress selectedAddress) {
        this.applicationContext = applicationContext;
        this.mAddressList = mAddressList;
        this.selectedAddress = selectedAddress;
    }

    /**
     * Inflates the layout for a single address item and returns a new ViewHolder object.
     * @param parent the parent ViewGroup
     * @param viewType the view type
     * @return a new ViewHolder object for a single address item
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.single_address_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data for a single address item to the ViewHolder.
     * @param holder the ViewHolder for a single address item
     * @param position the position of the address item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // Set the address text for the address item
        holder.mAddress.setText(mAddressList.get(position).getAddress());

        // Set an OnClickListener for the radio button to handle address selection
        holder.mRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set all addresses as unselected
                for (Address address : mAddressList) {
                    address.setSelected(false);
                }
                // Set the selected address as selected
                mAddressList.get(position).setSelected(true);

                // Uncheck any previously selected radio button
                if (mSelectedRadioButton != null) {
                    mSelectedRadioButton.setChecked(false);
                }
                // Set the current radio button as selected
                mSelectedRadioButton = (RadioButton) v;
                mSelectedRadioButton.setChecked(true);

                // Notify the SelectedAddress interface that an address has been selected
                selectedAddress.setAddress(mAddressList.get(position).getAddress());
            }
        });
    }

    /**
     * Returns the number of items in the list of addresses.
     * @return the number of items in the list of addresses
     */
    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    /**
     * ViewHolder class for a single address item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mAddress;
        private RadioButton mRadio;

        /**
         * Constructor for the ViewHolder.
         * @param itemView the View for a single address item
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAddress = itemView.findViewById(R.id.address_add);
            mRadio = itemView.findViewById(R.id.select_address);
        }
    }

    /**
     * Interface to handle address selection.
     */
    public interface SelectedAddress {
        /**
         * Method to set the selected address.
         * @param s the selected address as a String
         */
        public void setAddress(String s);
    }
}
