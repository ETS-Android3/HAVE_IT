package com.example.have_it;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 *This is the activity for adding an address for your selected habit event
 * extends {@link FragmentActivity} implements {@link OnMapReadyCallback}, {@link GoogleApiClient.ConnectionCallbacks}, {@link GoogleApiClient.OnConnectionFailedListener}, {@link LocationListener}
 * @author ruiqingtian
 */
public class PickLocationMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     *This is the current context, of class {@link Context}
     */
    Context context;

    /**
     *This is map fragment used in this map activity, of class {@link SupportMapFragment}
     */
    SupportMapFragment mapFragment;

    /**
     *Reference to the add button, of class {@link Button}
     */
    Button addButton;

    /**
     *Reference to the google map, of class {@link GoogleMap}
     */
    GoogleMap mMap;

    /**
     *Reference to the google map client, of class {@link GoogleApiClient}
     */
    GoogleApiClient mGoogleApiClient;

    /**
     *Reference to the last location on the map, of class {@link Location}
     */
    Location mLastLocation;

    /**
     *Reference to the marker on the map, of class {@link Marker}
     */
    Marker mCurrLocationMarker;

    /**
     *Reference to the request for location, of class {@link LocationRequest}
     */
    LocationRequest mLocationRequest;

    /**
     * Lagitude to store the location as String Variable {@link String}
     */
    String latitude = null;
    /**
     * Longitude to store the location as String Variable {@link String}
     */
    String longitude = null;


    /**
     *This is the method invoked when the activity starts
     * @param savedInstanceState {@link Bundle} used for its super class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        addButton = findViewById(R.id.addLocation_button);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }


    /**
     *This is the method invoked when google map is ready to set up
     * @param googleMap {@link GoogleMap}
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        mMap.clear();
                        addButton.setVisibility(View.INVISIBLE);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("Your Selected Location!"));

                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        addButton.setVisibility(View.VISIBLE);
                        latitude = String.valueOf(marker.getPosition().latitude);
                        longitude = String.valueOf(marker.getPosition().longitude);
                        return false;
                    }
                });

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addButton.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent();
                        intent.putExtra("LAT", latitude);
                        intent.putExtra("LONG", longitude);
                        setResult(RESULT_OK, intent);
//                        Toast.makeText(context,"Latitude: " + latitude +"\nLongitude: "+longitude,Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });


            } else{
                Toast.makeText(context, "Need Permission to access location!!", Toast.LENGTH_SHORT).show();
                Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(gpsOptionsIntent);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     *This is the method invoked when google api client is required to be built and
     * @return
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     *This is the method invoked when a location request is needed for this activity
     * @return
     */
    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {}

    /**
     *This is the method invoked when the location on google map is changed
     * @return
     */
    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



}