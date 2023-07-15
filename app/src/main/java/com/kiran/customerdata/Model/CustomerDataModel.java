package com.kiran.customerdata.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class CustomerDataModel implements Parcelable {
    private String name,email,number,address,id;
    int age;
    private Timestamp joinDate;



    public CustomerDataModel() {
    }

    public CustomerDataModel(String name, String email, String number, String address, int age) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.address = address;
        this.age = age;
    }

    protected CustomerDataModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        number = in.readString();
        address = in.readString();
        id = in.readString();
        age = in.readInt();
        joinDate = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<CustomerDataModel> CREATOR = new Creator<CustomerDataModel>() {
        @Override
        public CustomerDataModel createFromParcel(Parcel in) {
            return new CustomerDataModel(in);
        }

        @Override
        public CustomerDataModel[] newArray(int size) {
            return new CustomerDataModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(number);
        parcel.writeString(address);
        parcel.writeString(id);
        parcel.writeInt(age);
        parcel.writeParcelable(joinDate, i);
    }
}
