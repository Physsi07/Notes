package nsft.ToDoList;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.google.android.gms.location.*;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    // VARS //
    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // WIDGETS //
    private EditText mSearchText;
    private ImageView mGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSearchText = findViewById(R.id.inputSearch);
        mGPS = findViewById(R.id.ic_gps);

        getLocationPermission();


        // trying to use the google places in here
//        GeoDataClient geoDataClient = Places.getGeoDataClient(this, null);
    }

    // INIT METHOD //;
    public void init() {
        Log.d(TAG, "init: Initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER ){

                    // EXECUTE OUR METHOD FOR SEARCHING //
                    geoLocate();
                }
                return false;
            }
        });

        mGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: CLICKED GPS ICON");
                getDeviceLocation();
            }
        });

        hideKeyBoard();
    }

    // GETTING MY LOCATION IN THE MAP WITH GEOLOCATE //
    private void geoLocate() {
        Log.d(TAG, "geoLocate: geoLoacting");

        // GETTING THE SEARCH STRING //
        String searchingString  = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);

        // CREATING A LIST OF RESULT FROM SEARCH BAR //
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchingString, 1);

        }catch (IOException e){
            Log.d(TAG, "geoLocate: IOException");
        }

        // CHEKING IF WE HAVE SOME RESULT IN THE LIST THAT WAS GOTTEN FROM THE SEARCH BAR //
        if(list.size() > 0){
            Address address = list.get(0); // FIRST POSITION OF THE LIST //

            Log.d(TAG, "geoLocate: FOUND A LOCATION" + address.toString());
//            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            // MOVING THE CAMERA TO THE NEW LATITUDE AND LONGITUDE //
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }

    }

    // INITIALIZING THE MAP //
    private void initMap() {
        Log.d(TAG, "initMap: INITIALIZING THE MAP");
        // GET SUPPORT MAP FRAGMENT AND GET REQUEST FOR MAP WHEMN THE MAP IS READY TO BE USE //
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "MAP IS READY", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onMapReady: MAPS IS READY");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            // DISABLES THE BUTTON FOR RELOCATING ME //
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        // IIIALIZING //
        init();

    }

    public void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: GETTING THE CURRENT DEVICE LOCATION");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted) {

                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener(){

                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: FOUND LOCATION");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                            Log.d(TAG, "onComplete: MY LOCATION IS " + currentLocation.getLatitude() + " AND " +  currentLocation.getLongitude());

                        }else{
                            Log.d(TAG, "onComplete: CURRENT LOCATION IS NULL");
                            Toast.makeText(MapsActivity.this, "CURRENT LOCATION NOT FOUND", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch(SecurityException e){
            Log.d(TAG, "getDeviceLocation:  SECURITYEXCEPTION" + e.getMessage());

        }
    }

    // METHOD THAT WE WILL USE TO MOVE THE CAMERA TO THE LOCATION WHERE THE DEVICE IS //
    public void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: MOVING THE CAMERA TO LATITUDE: " + latLng.latitude + " , LONGITUDE TO : " + latLng.longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        // DROPING A PING ON THE NEW LATITUDE AND LONGITUDE THAT WAS FOUND THE THS LIST GOTTEN FROM THE SEARCH BARR //
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);

        mMap.addMarker(options);

        hideKeyBoard();
    }

    public void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: GETTING LOCATION PERMISSION");
        String[] permission = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };

        // CHECKING IF THE PERMISSION ARE GRANTED IF THERE NOT THEN IT RUNS THE REQUEST FOR PERMISSION BUT IF THEY ARE THEN INITIALIZE THE MAP//
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: CALLED");
        mLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0) {
                    for(int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            Log.d(TAG, "onRequestPermissionsResult: PERMISSION FAILED");
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: PERMISSION GRANTED");
                    mLocationPermissionGranted = true;
                    // INITIALIZE OUR MAP //
                    initMap();
                }
        }
    }

    // HIDDING THE KEYBOARD //
    public void hideKeyBoard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}