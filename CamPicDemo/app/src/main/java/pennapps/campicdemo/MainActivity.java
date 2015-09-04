package pennapps.campicdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
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


import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_TAKE_PHOTO_PAINT = 2;
    private String mCurrentPhotoPath;
    private String WORD_PIC_PATH;
    //private static final String WORD_PIC_PATH = "/storage/15FC-271E/Android/data/pennapps.campicdemo/files/word.jpg";

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
        WORD_PIC_PATH = getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                .getAbsolutePath() + "/word.jpg";
        Log.i("Dir path", WORD_PIC_PATH);
        // Check path
        // Toast.makeText(this,
                //getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath(),
                //Toast.LENGTH_LONG).show();
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
        File image = null;
        try {
            image = createImageFile();
        } catch (IOException ex) {
            Log.e("exception", ex.toString());
        }
        mCurrentPhotoPath = image.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    // Create an image file
    private File createImageFile() throws IOException {
        // Create a file name to avoid collision
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "/JPEG_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(null); // BitmapFactory kept returning null. Either way failes
        // return File.createTempFile(imageFileName, ".jpg", storageDir);
        return new File(storageDir.getAbsolutePath() + imageFileName);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    protected boolean checkIfImgExists(String path) {
        // Check if image really exist
        File image = new File(mCurrentPhotoPath);
        if (image.exists()) {
            Log.i("image", "file exist.");
            Log.i("image", mCurrentPhotoPath);
            return true;
        } else Log.i("image", "file does not exist. ");
        return false;
        // Pass the path of photo to next activity.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Old request
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            // Check if image really exist
            if (checkIfImgExists(mCurrentPhotoPath))
                openPicWordActivity(mCurrentPhotoPath);
        } else if (requestCode == REQUEST_TAKE_PHOTO_PAINT && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            // Check if image really exist
            if (checkIfImgExists(mCurrentPhotoPath))
                openCamPaintActivity(mCurrentPhotoPath);
        }
    }
    // Test function, enter text selection mode.
    public void pic(View view) {
        openPicWordActivity(mCurrentPhotoPath);
    }

    // Open pre-saved picture for word detection
    public void wordPic(View view) {
        openPicWordActivity(WORD_PIC_PATH);
    }

    public void openPicWordActivity(String imgPath) {
        Intent intent = new Intent(getApplicationContext(), PicWordActivity.class);
        intent.putExtra("IMAGE", imgPath);
        startActivity(intent);
    }

    public void openCamPaintActivity(String imgPath) {
        Intent intent = new Intent(getApplicationContext(), CamPaintActivity.class);
        intent.putExtra("IMAGE", imgPath);
        startActivity(intent);
    }

    public void wordPick(View view) {
        Intent intent = new Intent(getApplicationContext(), PickWordActivity.class);
        intent.putExtra("IMAGE", WORD_PIC_PATH);
        startActivity(intent);
    }

    public void paintDemo(View view) {
        Intent intent = new Intent(getApplicationContext(), PaintActivity.class);
        intent.putExtra("IMAGE", WORD_PIC_PATH);
        startActivity(intent);
    }

    public void camPaintDemo(View view) {
        // No need for camera permission. wired
        File image = null;
        try {
            image = createImageFile();
        } catch (IOException ex) {
            Log.e("exception", ex.toString());
        }
        mCurrentPhotoPath = image.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_PAINT);
    }

    // Test Tesseract
    public void tess() {
        TessBaseAPI baseApi = new TessBaseAPI();
        String DATA_PATH = null;
        String lang = null;
        Bitmap bitmap = null;
        baseApi.init(DATA_PATH, lang);
        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();
        Toast.makeText(this, recognizedText, Toast.LENGTH_LONG).show();
    }
}
