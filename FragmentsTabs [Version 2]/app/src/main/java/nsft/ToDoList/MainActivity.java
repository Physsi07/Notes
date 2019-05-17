package nsft.ToDoList;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

import static nsft.ToDoList.R.array.Colors_For_SideBar;

// TO DO //
// USER NOTIFICATION
// SETTING AND
// USER PREFERENCES
// NOTIFIES ME WHEN I GET TO A SPECIFIC ADDRESS
// CHAPTER 9
// SETTINGS FOR THE USER CONTROL
// CREATE AN ALERT DIALOG SAYING "ARE YOU SURE YOU WANT TO DELETE"

public class MainActivity extends AppCompatActivity implements Tab1.OnFragmentInteractionListener,
        Tab2.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    // MY VARIABLES //
    Toolbar toolbar;
    TabLayout tablayout;
    TabItem personal;
    TabItem business;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    // A STRING VARIABLE //
    private String selecteditem;

    // MY SPINNER FOR THE USER SETTING //A
    Spinner spinner1;

    // FLOATING ACTION BUTTONS/
    FloatingActionButton fab1;
    FloatingActionButton fab2;

    // NOTIFICATION OBJECT //
    NotificationCompat.Builder notification;
    private static final int uniqueID = 12345;

    // BITMAP FOR THE IMAGE THAT WILL BE TAKEN //
    Bitmap bitmap;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isServicesOK()) {

            init();
        }

    }

    public void init() {
        // MY TOOLBAR -> GETTING ITS ID, SETTING THE TITLE AND THE SUBTITLE OF IT //
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("To Do List");
        toolbar.setSubtitle("Welcome");
        setSupportActionBar(toolbar);

        // MY TABS //
        // WHAT YOU PUT IN THE TEXT WILL BE DISPLAY IN THE TAB //
        tablayout = findViewById(R.id.tapLayout);
        personal = findViewById(R.id.personalTab);
        business = findViewById(R.id.businessTab);

        // MY SPINNERS //
        spinner1 = findViewById(R.id.toolbar_spinner);

        // MY VIEW PAGER //
        viewPager = (ViewPager) findViewById(R.id.pager);

        // FLOATING BUTTON FOR CAMERA AND MAP //
        fab1 = findViewById(R.id.fabForCamera);
        fab2 = findViewById(R.id.fabForMap);

        // IMAGE //
        image = findViewById(R.id.image);

        // NOTIFICATION //
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        // CALLING THE CAMERA WHEN THE FAB BUTTON IS CLICK //
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

                Snackbar.make(view, "Camera Loading", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);

                Snackbar.make(view, "Map Loading", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selecteditem = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "THE STRING: " + selecteditem);


                // GETTING THE COLOR THAT IS STORE IN THE SHARE PREFERENCE IF THE COLOR IS NOT THE DEFAULT† //
                if (getColorForToolbar() != getResources().getColor(R.color.colorPrimaryDark)) {
                    // COLOR THE TOOLBAR AND THE STATUS BAR //
                    toolbar.setBackgroundColor(getColorForToolbar());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getColorForToolbar());
                    }
                }

                if (getColorForTabLayout() != getResources().getColor(R.color.colorForTabs)) {
                    // COLOR THE TABS //
                    tablayout.setBackgroundColor(getColorForTabLayout());
                }

                if (selecteditem.equals("Default")) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    tablayout.setBackgroundColor(getResources().getColor(R.color.colorForTabs));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    saveColorForToolbar(getResources().getColor(R.color.colorPrimaryDark));
                    saveColorForTabLayout(getResources().getColor(R.color.colorForTabs));

                } else if (selecteditem.equals("Red")) {
                    // TOOLBAR AND TAB LAYOUT //
                    toolbar.setBackgroundColor(getResources().getColor(R.color.Red));
                    tablayout.setBackgroundColor(getResources().getColor(R.color.Red2));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.Red));
                    }
                    saveColorForToolbar(getResources().getColor(R.color.Red));
                    saveColorForTabLayout(getResources().getColor(R.color.Red2));

                } else if (selecteditem.equals("Green")) {
                    // TOOLBAR AND TAB LAYOUT//
                    toolbar.setBackgroundColor(getResources().getColor(R.color.Green));
                    tablayout.setBackgroundColor(getResources().getColor(R.color.Green2));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.Green));
                    }
                    saveColorForToolbar(getResources().getColor(R.color.Green));
                    saveColorForTabLayout(getResources().getColor(R.color.Green2));

                } else if (selecteditem.equals("Purple")) {
                    // TOOLBAR AND TAB LAYOUT //
                    toolbar.setBackgroundColor(getResources().getColor(R.color.Purple));
                    tablayout.setBackgroundColor(getResources().getColor(R.color.Purple2));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.Purple));
                    }
                    saveColorForToolbar(getResources().getColor(R.color.Purple));
                    saveColorForTabLayout(getResources().getColor(R.color.Purple2));
                }

                if (selecteditem == "Colors") {
                    Toast.makeText(getApplicationContext(), selecteditem, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* OBJECT OF THE PAGE ADAPTER CLASS */
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tablayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // SINKING THE VIEW PAGER WITH THE TABS //
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        // ADAPTERS //
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.Colors_For_SideBar,
                android.R.layout.simple_spinner_item);

        // SETTING THE ADAPTER //
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        spinner1.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        bitmap = (Bitmap) data.getExtras().get("data");
        image.setImageBitmap(bitmap);

        // CREATING THE LITTLE LOGO //
        notification.setSmallIcon(R.drawable.camera);

        // CREATING THE TICKER FOR THE NOTIFICATION AND THE TIME //
        notification.setTicker("Picture was taken");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Camera");
        notification.setContentText("You have a picture in your side bar");

        // TAKES CARE FOR WHEN EVER THEY CLICKED IT //
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        // BUILDS NOTIFICATION AND SENDING IT TO THE DEVICE //
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build()); //BUILBING THE NOTIFICATION AND SENDING IT OUT TO THE SYSTEM //

        saveImage(image);;
    }

    public void saveImage(ImageView img){
        Log.d(TAG, "saveImage: SAVING IMAGE TO THE SHARE PREFERENCES");
        SharedPreferences sharedPreferences = getSharedPreferences("image", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        //mEditor.put
    }

    public void saveColorForToolbar(int color) {
        Log.d(TAG, "saveColor: " + color);
        SharedPreferences sharedPreferences = getSharedPreferences("ColorForToolbar", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putInt("ColorForToolbar", color);

        // SAVE THE CHANGES INTO SHARE PREFERENCE //
        mEditor.apply();
    }

    public void saveColorForTabLayout(int color) {
        Log.d(TAG, "saveColorForTabLayout: " + color);
        SharedPreferences sharedPreferences = getSharedPreferences("ColorForTabLayout", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putInt("ColorForTabLayout", color);

        // SAVE THE CHANGES INTO SHARE PREFERENCE //
        mEditor.apply();
    }

    public int getColorForToolbar() {
        Log.d(TAG, "getColorForToolbar: " + selecteditem);
        SharedPreferences sharedPreferences = getSharedPreferences("ColorForToolbar", MODE_PRIVATE);
        int selectedColor = sharedPreferences.getInt("ColorForToolbar", getResources().getColor(R.color.colorPrimaryDark));
        return selectedColor;
    }

    public int getColorForTabLayout() {
        Log.d(TAG, "getColorForTabLayout: " + selecteditem);
        SharedPreferences sharedPreferences = getSharedPreferences("ColorForTabLayout", MODE_PRIVATE);
        int selectedColor = sharedPreferences.getInt("ColorForTabLayout", getResources().getColor(R.color.colorForTabs));
        return selectedColor;
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: CHECKING GOOGLE SERVICES VERSION");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            // EVERYTHING IS FINE WITH THE USER VERSION AND THEY CAN MAKE A MAP REQUEST //
            Log.d(TAG, "isServicesOK: GOOGLE PLAY SERVICES IS WORKING");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // AN ERROR OCCURED BUT WE CAN RESOLVE IT //
            Log.d(TAG, "isServicesOK: AN ERROR OCCURRED BUT WE CAN FIX IT");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "CAN'T REQUEST FOR MAP", Toast.LENGTH_LONG).show();
        }

        return false;
    }

}