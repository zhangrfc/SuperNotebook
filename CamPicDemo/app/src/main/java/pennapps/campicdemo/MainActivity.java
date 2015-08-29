package pennapps.campicdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if device support camera
        Context context = this;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // yes
            Log.i("camera", "This device has camera");
        } else {
            // no
            Log.i("camera", "This device has no camera");
        }
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Opens camera and take picture.
    // Then enter text selection mode.
    public void camera(View view) {
        // No need for camera permission. wired
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            // Add a imageview to layout for image displaying.
            // LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
            // ImageView mImageView = new ImageView(this);
            // mImageView.setImageBitmap(bp);
            // layout.addView(mImageView);
            Intent intent = new Intent(getApplicationContext(), PicWordActivity.class);
            intent.putExtra("IMAGE", bp);
            startActivity(intent);
        }

    }
    // Test function, enter text selection mode.
    public void pic(View view) {

    }
}
