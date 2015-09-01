package pennapps.campicdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

public class PicWordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_word);
        // Load img from intent.
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        String path = (String) extras.get("IMAGE");
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bitmap);
        // Drawable d = Drawable.createFromPath(path);
        // imageView.setImageDrawable(d);
        // Run Tessract to recognize words.
        TessBaseAPI baseAPI = new TessBaseAPI();
        //String DATA_PATH = "/storage/15FC-271E/Android/data/pennapps.campicdemo/files/"; //getDataPath();
        String DATA_PATH = getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                .getAbsolutePath() + "/";
        String lang = "eng";
        Log.i("DATA_PATH", DATA_PATH);
        baseAPI.init(DATA_PATH, lang);
        baseAPI.setImage(bitmap);
        String recognizedText = baseAPI.getUTF8Text();
        baseAPI.end();
        Log.i("REC_WORDS", recognizedText);
    }

    public static boolean isSDCardMounted() {
        boolean isMounted = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            isMounted = true;
        } else if (Environment.MEDIA_BAD_REMOVAL.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_CHECKING.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_NOFS.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_REMOVED.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_UNMOUNTABLE.equals(state)) {
            isMounted = false;
        } else if (Environment.MEDIA_UNMOUNTED.equals(state)) {
            isMounted = false;
        }
        return isMounted;
    }

    public static boolean isDirectoryExists(final String filePath) {
        boolean isDirectoryExists = false;
        File mFilePath = new File(filePath);
        if(mFilePath.exists()) {
            isDirectoryExists = true;
        } else {
            isDirectoryExists = mFilePath.mkdirs();
        }
        return isDirectoryExists;
    }

    public static String getDataPath() {
        String returnedPath = "";
        final String mDirName = "tesseract";
        final String mDataDirName = "tessdata";
        if(isSDCardMounted()) {
            final String mSDCardPath = Environment.getExternalStorageDirectory() + File.separator + mDirName;
            Log.i("SDCardPath", mSDCardPath);
            if(isDirectoryExists(mSDCardPath)) {
                final String mSDCardDataPath = Environment.getExternalStorageDirectory() + File.separator + mDirName +
                        File.separator + mDataDirName;
                Log.i("SDCardDataPath", mSDCardDataPath);
                isDirectoryExists(mSDCardDataPath);
                return mSDCardPath;
            }
        }
        return returnedPath;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pic_word, menu);
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
}
