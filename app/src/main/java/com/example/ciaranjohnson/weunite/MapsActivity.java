package com.example.ciaranjohnson.weunite;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ciaranjohnson.weunite.Common.Common;
import com.example.ciaranjohnson.weunite.Model.Help;
import com.example.ciaranjohnson.weunite.Model.Offers;
import com.example.ciaranjohnson.weunite.Model.User;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Button btnProfile, btnNotifications, btnOptions;

    LocationManager locationManager;

    public static String TAG = "MapsActivity";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;
    private User user;
    private Common common;

    private Dialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mDialog = new Dialog(MapsActivity.this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null) {
            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        } else {

            //Potentially delete this part as we might not need the common part.
            firebaseDatabase = FirebaseDatabase.getInstance();
            mRef = firebaseDatabase.getReference("User");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.child(mFirebaseAuth.getCurrentUser().getUid()).getValue(User.class);
                    common = new Common();
                    common.currentUser = user;
                    common.help = new Help();

                    Log.d(TAG, user.getName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnNotifications = (Button) findViewById(R.id.buttonNotifications);
        btnOptions = (Button) findViewById(R.id.buttonOptions);
        btnProfile = (Button) findViewById(R.id.buttonProfile);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Log.i("Network", "onMapReady: Location Manger is okay");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //get latitude
                    double latitude = location.getLatitude();
                    //get longitude
                    double longitude = location.getLongitude();
                    //instatiate the LatLng class
                    LatLng latLng = new LatLng(latitude, longitude);

                    Common.latLng = latLng;

                    //instantiate the class, Geocoder
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
//                        String string = addressList.get(0).getLocality();
//                        string += addressList.get(0).getCountryName();
                        LatLng os = new LatLng(55.940260, -3.177613);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(mFirebaseUser.getDisplayName()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.i("GPS", "onMapReady: Location Manger is okay");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    Log.d("GPS", "onLocationChanged");

                    Common.latLng = latLng;
                    //instantiate the class, Geocoder
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
//                        String string = addressList.get(0).getLocality();
//                        string += ", " + addressList.get(0).getCountryName();
                        Log.d("GPS", "inside try");
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person_pin_blue_24dp);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(mFirebaseUser.getDisplayName())
                                .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_person_pin_blue_24dp)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_help = database.getReference("Help");

        table_help.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(final DataSnapshot contact:dataSnapshot.getChildren()){
                    for(final DataSnapshot c: contact.getChildren()) {

                        //Plan is to store username in the help section meaning there is no need to open user info.
                        // when clicked bring up a display help activity.
                        final Help help = c.getValue(Help.class);


                        LatLng latLng = new LatLng(help.getLatitude(), help.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(help.getDisplayName()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.2f));

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(final Marker marker) {

                                mDialog.setContentView(R.layout.custom_popup);
                                Button helpOutBtn = (Button) mDialog.findViewById(R.id.helpOutBtn);
                                TextView txtTitle = (TextView) mDialog.findViewById(R.id.helpTitle);
                                TextView txtName = (TextView) mDialog.findViewById(R.id.helpName);
                                TextView txtDescription = (TextView) mDialog.findViewById(R.id.helpDescription);


                                txtName.setText(help.getDisplayName());
                                txtDescription.setText(help.getDescription());

                                helpOutBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        int offerCount = help.getOfferCounter();
                                        help.setOfferCounter(offerCount+1);
                                        table_help.child(contact.getKey()).child(c.getKey()).setValue(help);


                                        FirebaseDatabase fbOffers = FirebaseDatabase.getInstance();
                                        final DatabaseReference table_offers = fbOffers.getReference("Offers");
                                        final Offers offer = new Offers(mFirebaseUser.getUid());

                                        table_offers.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                table_offers.child(contact.getKey()).child(c.getKey()).child(Integer.toString(help.getOfferCounter())).setValue(offer);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });
                                mDialog.show();
                                return false;
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void closeWindow(View view){
        mDialog.dismiss();
    }


    public void goToProfile(View view){
        Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void goToOptions(View view){
        Intent intent = new Intent(MapsActivity.this, OptionsActivity.class);
        startActivity(intent);
    }

    public void goToNotifications(View view){
        Intent intent = new Intent(MapsActivity.this, NotificationsActivity.class);
        startActivity(intent);
    }

    /**
     *
     *  This method is used to create the marker for the users current position.
     *  Future improvements can change marker background and vectorDrawable.
     *  Make the Marker bigger.
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_person_pin_blue_24dp);
        background.setBounds(0, 0, background.getIntrinsicWidth(),background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
