package com.example.consolefindergis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenu extends AppCompatActivity {

    private DatabaseReference consoleReference = FirebaseDatabase.getInstance("https://console-finder-gis-f12bd-default-rtdb.firebaseio.com/").getReference("Console");
    private DatabaseReference locationReference = FirebaseDatabase.getInstance("https://console-finder-gis-f12bd-default-rtdb.firebaseio.com/").getReference("Location");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button button = findViewById(R.id.search_btn_map);

// Values of string array from the database
        Spinner dropdown = findViewById(R.id.dropdown);
        String[] items = new String[]{"PS VITA", "PSP", "Wii", "XBox", "XBox 360", "XBox ONE", "נינטנדו", "פלייסטיישן", "פלייסטיישן 2", "פלייסטיישן 3", "פלייסטיישן 4" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
//TODO change input city to fetch from database

        ArrayList<String> indexes = new ArrayList();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dropdownChoice = dropdown.getSelectedItem().toString();

                //checks indexes so we can connect the chosen console to other related data
                consoleReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int counter = 0;
                        for(DataSnapshot console:snapshot.getChildren())
                        {
                            String consoleName = console.getValue().toString().trim();
                            if(consoleName.equals(dropdownChoice))
                            {
                                indexes.add(counter+ "");
                            }
                            counter++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //setting HashSet to draw the cities
                //setting HashMap to count the cities so we can add the numbers on markers
                HashSet<String> locationsToDraw = new HashSet<>();
                List<String> locationList = Collections.<String>emptyList();
                //HashMap<String, Integer> locationCounter = new HashMap<String, Integer>();
                //locationCounter.put("תל אביב-יפו", 7);
                //locationCounter.get("תל אביב-יפו");

                locationReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int counter = 0;

                        for(DataSnapshot location:snapshot.getChildren())
                        {

                            if(indexes.contains(counter+""))
                            {
                                String name = location.getValue(String.class);

                                name.trim();
                                if(name.equals("תל אביב")|| name.equals("תל אביב -יפו"))
                                {
                                    name = "תל אביב-יפו";
                                }
                                locationsToDraw.add(name);

                            }
                            counter++;
                        }

                        System.out.println(locationsToDraw);
                        Intent intent = new Intent(MainMenu.this,MapsActivity.class);
                        ArrayList<String> conversionToList = new ArrayList<>();
                        ArrayList<String> conversionIndexesKeyToList = new ArrayList<>();
                        conversionIndexesKeyToList.addAll(indexes);
                        conversionToList.addAll(locationsToDraw);
                        intent.putExtra("city",conversionToList.toString());
                        intent.putExtra("console",dropdown.getSelectedItem().toString());
                        intent.putExtra("indexes",conversionIndexesKeyToList.toString());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

    }
}