package com.youngonessoft.android.actiondirecte.analysismodule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.youngonessoft.android.actiondirecte.R;
import com.youngonessoft.android.actiondirecte.data.DatabaseContract;
import com.youngonessoft.android.actiondirecte.data.DatabaseHelper;
import com.youngonessoft.android.actiondirecte.data.DatabaseReadWrite;

import static com.youngonessoft.android.actiondirecte.util.TimeUtils.convertDate;

public class MapAnalysisFragment extends Fragment {

    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private SupportMapFragment mapFragment;
    private Marker mSelectedMarker;
    private GoogleMap mMap = null;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_analysis, container, false);

        mContext = getContext();

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;

                    // Hide the zoom controls.
                    mMap.getUiSettings().setZoomControlsEnabled(false);

                    // Add lots of markers to the map.
                    double[] latLngDoubleArray = addMarkersToMap();

                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            LinearLayout info = new LinearLayout(mContext);
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(mContext);
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(mContext);
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });

                    // Set listener for marker click event.  See the bottom of this class for its behavior.
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            // The user has re-tapped on the marker which was already showing an info window.
                            if (marker.equals(mSelectedMarker)) {
                                // The showing info window has already been closed - that's the first thing to happen
                                // when any marker is clicked.
                                // Return true to indicate we have consumed the event and that we do not want the
                                // the default behavior to occur (which is for the camera to move such that the
                                // marker is centered and for the marker's info window to open, if it has one).
                                mSelectedMarker = null;
                                return true;
                            }

                            mSelectedMarker = marker;

                            // Return false to indicate that we have not consumed the event and that we wish
                            // for the default behavior to occur.
                            return false;
                        }
                    });

                    // Set listener for map click event.  See the bottom of this class for its behavior.
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng loaction) {
                            // Any showing info window closes when the map is clicked.
                            // Clear the currently selected marker.
                            mSelectedMarker = null;
                        }
                    });

                    // Override the default content description on the view, for accessibility mode.
                    // Ideally this string would be localized.
                    googleMap.setContentDescription("Map showing climbing ascent locations.");

                    LatLng minLatLng = new LatLng(latLngDoubleArray[0], latLngDoubleArray[1]);
                    LatLng maxLatLng = new LatLng(latLngDoubleArray[2], latLngDoubleArray[3]);
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(minLatLng)
                            .include(maxLatLng)
                            .build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            });
        }

        // R.id.map is a FrameLayout, not a Fragment
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        return rootView;
    }

    private double[] addMarkersToMap() {

        DatabaseHelper handler = new DatabaseHelper(getContext());
        SQLiteDatabase database = handler.getWritableDatabase();
        Cursor cursor = DatabaseReadWrite.GpsClimbLoadData(database);

        int idColumnOutput;
        String outputRouteName;
        String outputLocationName;
        Long outputDate;
        String outputDateString;
        int outputFirstAscent;
        int outputGradeNumber;
        int outputGradeName;
        int outputAscent;
        double outputLatitude;
        double outputLongitude;
        double[] latLngDoubleArray = new double[4];
        latLngDoubleArray[0] = 999; // Min Lat
        latLngDoubleArray[1] = 999; // Min Long
        latLngDoubleArray[2] = -999; // Max Lat
        latLngDoubleArray[3] = -999; // Max Long


        int i = 0;
        while (i < cursor.getCount()) {
            cursor.moveToPosition(i);

            // Get and set route name
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_NAME);
            outputRouteName = cursor.getString(idColumnOutput);

            // Get and set  location name
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_LOCATION);
            outputLocationName = cursor.getString(idColumnOutput);

            // Get date
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_DATE);
            outputDate = cursor.getLong(idColumnOutput);
            outputDateString = convertDate(outputDate, "dd/MM/yyyy");

            // Get whether first ascent or not
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_FIRSTASCENTCODE);
            outputFirstAscent = cursor.getInt(idColumnOutput);

            // Get grade
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_GRADECODE);
            outputGradeNumber = cursor.getInt(idColumnOutput);
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_GRADETYPECODE);
            outputGradeName = cursor.getInt(idColumnOutput);
            String outputGradeText = DatabaseReadWrite.getGradeTypeClimb(outputGradeName, mContext) + " | " + DatabaseReadWrite.getGradeTextClimb(outputGradeNumber, mContext);

            // Get ascent type
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_ASCENTTYPECODE);
            outputAscent = cursor.getInt(idColumnOutput);

            // Get get latitude type
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_GPSLATITUDE);
            outputLatitude = cursor.getDouble(idColumnOutput);

            // Get get longitude type
            idColumnOutput = cursor.getColumnIndex(DatabaseContract.ClimbLogEntry.COLUMN_GPSLONGITUDE);
            outputLongitude = cursor.getDouble(idColumnOutput);

            String outputSnippetString = outputLocationName + " \n" + outputDateString + " \n" + outputGradeText;

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(outputLatitude, outputLongitude))
                    .title(outputRouteName)
                    .snippet(outputSnippetString));

            if (outputLatitude < latLngDoubleArray[0]) {
                latLngDoubleArray[0] = outputLatitude;
            }

            if (outputLongitude < latLngDoubleArray[1]) {
                latLngDoubleArray[1] = outputLongitude;
            }

            if (outputLatitude > latLngDoubleArray[2]) {
                latLngDoubleArray[2] = outputLatitude;
            }

            if (outputLongitude > latLngDoubleArray[3]) {
                latLngDoubleArray[3] = outputLongitude;
            }

            i++;
        }

        cursor.close();
        database.close();
        handler.close();

        return latLngDoubleArray;

    }

}
