package nsft.ToDoList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.content.*;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    // VARIABLES FROM XML //
    private ImageView iv;
    private TextView  tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // INITIALIZING XML FILES //
        iv = findViewById(R.id.iv_logo);
        tv = findViewById(R.id.tv_text);

        // ANIMATION INITIALIZATION //
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.transition);
        iv.startAnimation(anim);
        tv.startAnimation(anim);

        final Intent intent = new Intent(this, MainActivity.class);

        // CREATING A TIMER THAT FOR THE ANIMATION DURATION //
        Thread timer = new Thread() {
            @Override
            public void run() {
                try{
                    sleep(10000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}