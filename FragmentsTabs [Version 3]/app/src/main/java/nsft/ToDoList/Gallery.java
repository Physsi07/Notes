package nsft.ToDoList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    private static final String TAG = "Gallery";
    
    // NOTIFICATION OBJECT //
    NotificationCompat.Builder notification;
    private static final int uniqueID = 12345;

    // BITMAP FOR THE IMAGE THAT WILL BE TAKEN //
    Bitmap bitmap;
    ImageView image;

    // FLOATING BUTTON FOR CAMERA //
    FloatingActionButton fab1;

    ArrayAdapter<String> mAdapter;
    ListView list;
    ArrayList<String> task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        init();
    }

    public void init() {

        // IMAGE //
//        image = findViewById(R.id.image1);

        // FAB FOR CAMERA //
        fab1 = findViewById(R.id.fabForCamera);

        list = findViewById(R.id.listForPhotos);
        task = new ArrayList<String>();

        // NOTIFICATION //
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        // CALLING THE CAMERA WHEN THE FAB BUTTON IS CLICK //
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

                mAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.image, R.id.image1, task);
                list.setAdapter(mAdapter);

                Toast.makeText(Gallery.this, "Camera Open", Toast.LENGTH_SHORT).show();

                Snackbar.make(view, "Camera Loading", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (mAdapter == null) {
            Log.d(TAG, "init: ADAPTER IS NULL");
            mAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.image, R.id.image1, task);
            list.setAdapter(mAdapter);

        }else{
            Log.d(TAG, "init: ADAPTER IS NOT NULL");
            mAdapter.clear();
            mAdapter.addAll(task);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        bitmap = (Bitmap) data.getExtras().get("data");
        if (bitmap == null){
            Log.d(TAG, "onActivityResult: BITMAP IS NULL");
        }
        else {
            Log.d(TAG, "onActivityResult: BITMAP IS NOT NULL");
            image.setImageBitmap(bitmap);
        }

        // CREATING THE LITTLE LOGO //
        notification.setSmallIcon(R.drawable.camera);

        // CREATING THE TICKER FOR THE NOTIFICATION AND THE TIME //
        notification.setTicker("Picture was taken");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Camera");
        notification.setContentText("You have a picture in your side bar");

        // TAKES CARE FOR WHEN EVER THEY CLICKED IT //
        Intent intent = new Intent(getApplicationContext(), Gallery.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        // BUILDS NOTIFICATION AND SENDING IT TO THE DEVICE //
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build()); //BUILDING THE NOTIFICATION AND SENDING IT OUT TO THE SYSTEM //

    }

}
