package com.example.consolefindergis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;


public class SellersListActivity extends Activity {
    private DatabaseReference rootReference = FirebaseDatabase.getInstance("https://console-finder-gis-f12bd-default-rtdb.firebaseio.com/").getReference();

    static private String console;
    static private String city;
    static private String indexes;

    MyRecyclerViewAdapter adapter;
    ArrayList<SalesInfo> salesInfo;
    ArrayList<String> updatedIndexes = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_list_layout);
        //TODO get data from firebase
        // set up the RecyclerView


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.city = extras.getString("city");
            this.console = extras.getString("console");
            this.indexes = extras.getString("indexes");
        }


        salesInfo = new ArrayList<>();


        RecyclerView recyclerView = findViewById(R.id.recycleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        //checks indexes so we can connect the chosen console to other related data
        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 0;
                for(DataSnapshot LocationCheck:snapshot.child("Location").getChildren())
                {
                    String locationName = LocationCheck.getValue().toString().trim();
                    if(locationName.equals("תל אביב")|| locationName.equals("תל אביב -יפו"))
                    {
                        locationName = "תל אביב-יפו";
                    }
                    if(locationName.equals(city) && indexes.contains(counter+""))
                    {
                        updatedIndexes.add(counter+ "");
                    }
                    counter++;
                }

                for(String index:updatedIndexes)
                {
                    String title = (String) snapshot.child("Title").child(index).getValue();
                    String description = (String) snapshot.child("Description").child(index).getValue();
                    String sellerName = (String) snapshot.child("Seller Name").child(index).getValue();
                    String price = (String) snapshot.child("Price").child(index).getValue();
                    String sellerPhone = (String) snapshot.child("Seller Phone").child(index).getValue();
                    String city = (String) snapshot.child("Location").child(index).getValue();


                    SalesInfo salesInfoObject = new SalesInfo(title,description,sellerName,price,sellerPhone,city);

                    salesInfo.add(salesInfoObject);
                }
                adapter = new MyRecyclerViewAdapter(getApplicationContext(), salesInfo);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
