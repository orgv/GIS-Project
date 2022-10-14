package com.example.consolefindergis;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.consolefindergis.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker marker;
    static private String city;
    static private String console;
    static private String indexes;
    static private SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findCoordinates();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSearchParamsFromExtras();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private DatabaseReference reference = FirebaseDatabase.getInstance("https://console-finder-gis-f12bd-default-rtdb.firebaseio.com/").getReference("features");

    public void findCoordinates()
    {
               reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot city:snapshot.getChildren())
                {
                    PolygonOptions rectOption = new PolygonOptions();
                    String name = city.child("properties").child("name").getValue(String.class);
                    if(name == null)
                    {
                        name = "";
                    }
                    List<String> myList = Arrays.asList(MapsActivity.city.replace("[","").replace("]","").split(","));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        myList.replaceAll(String::trim);
                    }
                    if(myList.contains(name) && name != "") {
                        for(DataSnapshot coordinates: city.child("geometry").child("coordinates").child("0").child("0").getChildren())
                        {
                            rectOption.add(new LatLng(coordinates.child("1").getValue(Double.class),coordinates.child("0").getValue(Double.class)));
                        }
                        rectOption.fillColor(0x55FF00FF).strokeColor(Color.MAGENTA);
                        Polygon polygon = mMap.addPolygon(rectOption);
                        LatLng locationFirstCoordinates = new LatLng(city.child("geometry").child("coordinates").child("0").child("0").child("0").child("1").getValue(Double.class),city.child("geometry").child("coordinates").child("0").child("0").child("0").child("0").getValue(Double.class));
                        mMap.addMarker(new MarkerOptions().position(locationFirstCoordinates).title(name));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng initialCamera = new LatLng(32.0337564, 34.739131);
        //mMap.addMarker(new MarkerOptions().position(initialCamera).title("Marker in Tel-Aviv"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(initialCamera));
        mMap.setMinZoomPreference(8.5f);
        mMap.setMaxZoomPreference(14.0f);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerName = marker.getTitle();
                Intent intent = new Intent(MapsActivity.this,SellersListActivity.class);
                intent.putExtra("city", markerName);
                intent.putExtra("console",MapsActivity.console);
                intent.putExtra("indexes",MapsActivity.indexes);
                startActivity(intent);
                //Toast.makeText(MapsActivity.this, "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        /*
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            //TODO set the first coordinate of the city, also get data from firebase accordingly
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Intent intent = new Intent(MapsActivity.this,SellersListActivity.class);
                intent.putExtra("city", "rehovot");
                startActivity(intent);
            }
        });
*/
    }

    private void setSearchParamsFromExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.city = extras.getString("city");
            this.console = extras.getString("console");
            this.indexes = extras.getString("indexes");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);

    }
}