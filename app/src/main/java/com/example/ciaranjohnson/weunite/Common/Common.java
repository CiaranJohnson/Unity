package com.example.ciaranjohnson.weunite.Common;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.ciaranjohnson.weunite.Model.Help;
import com.example.ciaranjohnson.weunite.Model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class Common {

    public static User currentUser;

    public static String currentUserEmail;

    public static Help help;

    public static LatLng latLng;


}
