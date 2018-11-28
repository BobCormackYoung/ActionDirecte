package com.youngonessoft.android.actiondirecte.analysismodule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.youngonessoft.android.actiondirecte.R;

public class MapAnalysisFragment extends Fragment {

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    Context mContext;
    TextView longitudeValueGPS, latitudeValueGPS;
    Double longitudeGPS, latitudeGPS;

    private static final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_REQUEST = 1337;
    LocationManager locationManager;
    LocationListener locationListener;
    String locationProvider;

    //@SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_analysis, container, false);

        mContext = getActivity().getApplicationContext();
        longitudeValueGPS = rootView.findViewById(R.id.longitudeValueGPS);
        latitudeValueGPS = rootView.findViewById(R.id.latitudeValueGPS);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the location provider.
                Log.i("GPS Update", "onLocationChanged");
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("GPS Update", "onStatusChanged");
            }

            public void onProviderEnabled(String provider) {
                Log.i("GPS Update", "onProviderEnabled");
            }

            public void onProviderDisabled(String provider) {
                Log.i("GPS Update", "onProviderDisabled");
            }
        };

        //String locationProvider = LocationManager.NETWORK_PROVIDER;
        locationProvider = LocationManager.GPS_PROVIDER;

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        } else {
            doLocationThing();
        }

        //Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        //locationManager.removeUpdates(locationListener);

        return rootView;
    }

    private void makeUseOfNewLocation(Location location) {

        longitudeGPS = location.getLongitude();
        latitudeGPS = location.getLatitude();

        Log.i("GPS Update", "Long: " + longitudeGPS + " | Lat: " + latitudeGPS);

        longitudeValueGPS.setText(longitudeGPS + "");
        latitudeValueGPS.setText(latitudeGPS + "");
        Toast.makeText(mContext, "GPS Provider update", Toast.LENGTH_SHORT).show();

    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doLocationThing();
        }

    }

    @SuppressLint("MissingPermission")
    private void doLocationThing() {
        Log.i("GPS Update", "Do GPS Thing");
        //locationManager.requestLocationUpdates(locationProvider, 1, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
}
