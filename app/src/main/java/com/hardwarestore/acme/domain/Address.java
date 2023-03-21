package com.hardwarestore.acme.domain;

/**
 * A class representing an address.
 */
public class Address {
    String address;
    boolean isSelected;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Address() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
