package com.example.dogcupid.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dogcupid.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

public class MapFragment extends Fragment {

    private GoogleMap myMap;

    private ExtendedFloatingActionButton discover_BTN_Like;
    private ExtendedFloatingActionButton discover_BTN_pass;
    private DogsFragment dogsFragment;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        findViews(v);
        initViews();

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // When map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions = new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        // Set title of marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        // Remove all marker
                        googleMap.clear();
                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
                myMap = googleMap;
            }
        });
        return v;
    }

    private void findViews(View v) {
        discover_BTN_Like= v.findViewById(R.id.discover_BTN_Like);
        discover_BTN_pass= v.findViewById(R.id.discover_BTN_pass);
    }

    private void initViews() {
        discover_BTN_Like.setOnClickListener(v -> {
            if (dogsFragment != null) {
                dogsFragment.switchCard(true);
            }
        });
        discover_BTN_pass.setOnClickListener(v -> {
            if (dogsFragment != null) {
                dogsFragment.switchCard(false);
            }
        });
    }

    public void setDogsFragment(DogsFragment dogsFragment) {
        this.dogsFragment = dogsFragment;
    }

    public void changeCameraPosition(double latitude, double longitude) {

        if (myMap == null)
            return;

        LatLng latLng = new LatLng(latitude, longitude);
        CameraPosition position = new CameraPosition.Builder()
                .target(latLng)
                .zoom(13)
                .build();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(latitude + " : " + longitude);
        markerOptions.position(latLng);
        myMap.clear();
        myMap.addMarker(markerOptions);
        myMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

    }

}
