package com.kiran.customerdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kiran.customerdata.Activities.AddCustomerActivity;
import com.kiran.customerdata.Adapter.CustomerAdapter;
import com.kiran.customerdata.Model.CustomerDataModel;
import com.kiran.customerdata.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private CustomerAdapter adapter;
    private ArrayList<CustomerDataModel> arrayList;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        binding.imageButton.setOnClickListener(view -> {
            startActivity(new Intent(this, AddCustomerActivity.class));
        });


    }
    private void init(){
        arrayList = new ArrayList<>();
        adapter = new CustomerAdapter(arrayList, customerClickListener);
        firebaseFirestore = FirebaseFirestore.getInstance();
        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.RecyclerView.setAdapter(adapter);
        binding.RecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                adapter.notifyDataSetChanged();
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        binding.swipeRefresh.setRefreshing(true);
        firebaseFirestore.collection("customer").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        binding.swipeRefresh.setRefreshing(false);
                        for (int i=0; i<queryDocumentSnapshots.size(); i++){
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);
                            CustomerDataModel data = documentSnapshot.toObject(CustomerDataModel.class);
                            assert data != null;
                            data.setId(documentSnapshot.getId());
                            arrayList.add(data);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: "+e.getLocalizedMessage() );
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    CustomerAdapter.onCustomerClickListener customerClickListener = new CustomerAdapter.onCustomerClickListener() {
        @Override
        public void onDeleteClickListener(int position) {
            CustomerDataModel data = arrayList.get(position);
            firebaseFirestore.collection("customer").document(data.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onSuccess(Void unused) {
                    arrayList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "onFailure: "+e.getLocalizedMessage());
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onUpdateClickListener(int position) {
            CustomerDataModel data = arrayList.get(position);
            Intent intent = new Intent(MainActivity.this, AddCustomerActivity.class);
            intent.putExtra("data",data);
            startActivity(intent);
        }
    };

}