package com.kiran.customerdata.Adapter;


import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiran.customerdata.Model.CustomerDataModel;

import com.kiran.customerdata.databinding.CustomerLayoutBinding;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    ArrayList<CustomerDataModel> arrayList;
    public onCustomerClickListener customerClickListener;

    public CustomerAdapter(ArrayList<CustomerDataModel> arrayList, com.kiran.customerdata.Adapter.CustomerAdapter.onCustomerClickListener customerClickListener) {
        this.arrayList = arrayList;
        this.customerClickListener = customerClickListener;
    }

    public interface onCustomerClickListener{
        void onDeleteClickListener(int position);
        void onUpdateClickListener(int position);

    }
    

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomerLayoutBinding binding = CustomerLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CustomerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        CustomerDataModel data = arrayList.get(position);
        if (data==null){
            return;
        }
        holder.binding.Name.setText(data.getName());
        holder.binding.Age.setText(String.valueOf(data.getAge()));
        holder.binding.Number.setText(data.getNumber());
        holder.binding.Email.setText(data.getEmail());
        holder.binding.Address.setText(data.getAddress());

        holder.binding.delete.setOnClickListener(view -> {
            customerClickListener.onDeleteClickListener(position);

        });

        holder.binding.update.setOnClickListener(view -> {
            customerClickListener.onUpdateClickListener(position);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        CustomerLayoutBinding binding;
        public CustomerViewHolder(@NonNull CustomerLayoutBinding customerLayoutBinding) {
            super(customerLayoutBinding.getRoot());
            binding = customerLayoutBinding;
        }
    }
}
