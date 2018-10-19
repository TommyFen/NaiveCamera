package tommy.naivecamera;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import tommy.naivecamer.Camera1Preview;

public class MainActivity extends AppCompatActivity {

    private Camera1Preview preview;
    private ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preview = findViewById(R.id.preview_camera);
        ivPicture = findViewById(R.id.iv_picture);
    }

    @Override
    protected void onStart() {
        super.onStart();
        preview.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        preview.stop();
    }

    public void takePicture(View view) {
        preview.takePicture(new Camera.PictureCallback() {
            @SuppressLint("CheckResult")
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                GlideApp.with(MainActivity.this)
                        .load(bytes)
                        .centerCrop()
                        .into(ivPicture);
            }
        });
    }
}
