package com.example.momskitchen;

public class AddressData {
    String addressName, addressPhone, address, addressPincode, Email;

    public AddressData(String addressName, String addressPhone, String address, String addressPincode, String email) {
        this.addressName = addressName;
        this.addressPhone = addressPhone;
        this.address = address;
        this.addressPincode = addressPincode;
        Email = email;
    }

    public AddressData() {}

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressPincode() {
        return addressPincode;
    }

    public void setAddressPincode(String addressPincode) {
        this.addressPincode = addressPincode;
    }
}
