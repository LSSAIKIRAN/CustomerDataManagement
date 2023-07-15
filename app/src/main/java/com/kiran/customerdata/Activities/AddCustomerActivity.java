package com.kiran.customerdata.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kiran.customerdata.Model.CustomerDataModel;
import com.kiran.customerdata.databinding.ActivityAddCustomerBinding;


public class AddCustomerActivity extends AppCompatActivity {

    private ActivityAddCustomerBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private CustomerDataModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUp();

        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.save.setOnClickListener(view -> {
            if (data == null) {
                addData();
            } else {
                updateData();
                finish();
            }
        });
    }

    private void setUp() {
        Intent intent = getIntent();
        data = intent.getParcelableExtra("data");
        updateUI(data);
    }

    private void updateUI(CustomerDataModel data) {
        if (data != null) {
            binding.name.setText(data.getName());
            binding.email.setText(data.getEmail());
            binding.age.setText(String.valueOf(data.getAge()));
            binding.number.setText(data.getNumber());
            binding.address.setText(data.getAddress());
        }
    }

    private CustomerDataModel getCustomerData() {
        String name = binding.name.getText().toString();
        String email = binding.email.getText().toString();
        String age = binding.age.getText().toString();
        String number = binding.number.getText().toString();
        String address = binding.address.getText().toString();

        if (name.isEmpty() || email.isEmpty() || number.length() != 10 || address.isEmpty()) {
            binding.name.setError("Enter your name");
            binding.email.setError("Enter your email");
            binding.number.setError("Enter your number");
            binding.address.setError("Enter your address");
            return null;
        }

        CustomerDataModel data = new CustomerDataModel();
        data.setName(name);
        data.setNumber(number);
        data.setEmail(email);
        data.setAddress(address);
        data.setJoinDate(Timestamp.now());

        if (age.isEmpty()) {
            data.setAge(0);
        } else {
            data.setAge(Integer.parseInt(age));
        }
        return data;
    }

    private void updateData() {
        CustomerDataModel customerData = getCustomerData();
        if (customerData != null) {
            firebaseFirestore.collection("customer")
                    .document(data.getId())
                    .set(customerData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddCustomerActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "onFailure: " + e.getLocalizedMessage());
                            Toast.makeText(AddCustomerActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addData() {
        CustomerDataModel data = getCustomerData();
        if (data != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
            firebaseFirestore.collection("customer")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddCustomerActivity.this, "Customer added", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            binding.progressBar.setVisibility(View.GONE);
                            Log.e("TAG", "onFailure: " + e.getLocalizedMessage());
                            Toast.makeText(AddCustomerActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
