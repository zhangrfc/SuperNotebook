package pennapps.campicdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import magick.ExceptionType;
import magick.ImageInfo;
import magick.Magick;
import magick.MagickImage;
import magick.util.MagickBitmap;
import pennapps.campicdemo.R;

public class CamPaintActivity extends AppCompatActivity{

    DrawingView dv;
    private Paint mPaint;

    public class DrawingView extends View implements SpellCheckerSession.SpellCheckerSessionListener{
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        private Context context;
        private Paint circlePaint;
        private Path circlePath;
        private Paint darkPaint;

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;
        private Bitmap canvasBitmap;
        private Canvas saveImageCanvas;
        private int scaledWidth, scaledHeight;
        private static final float offset = 30;
        private Drawable backgroundDrawable;
        private String pngPath = null;

        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);

            // Paint for transparent layer.
            darkPaint = new Paint();
            darkPaint.setColor(Color.BLACK);
            darkPaint.setAlpha(150);
            darkPaint.setStrokeWidth(60);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

            canvas.drawPath(circlePath, circlePaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

        private void touch_start(float x, float y) {
            // Restore
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            // Save to a new bitmap.
            canvasBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight,
                    Bitmap.Config.ARGB_8888);
            saveImageCanvas = new Canvas();
            saveImageCanvas.setBitmap(canvasBitmap);
            // Draw transparent layer
            Point size = new Point();
            Display display = getWindowManager().getDefaultDisplay();
            display.getSize(size);
            RectF rect = new RectF(0, 0, size.x, size.y);
            mCanvas.drawRect(rect, darkPaint);
            // Reset path
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
                // works fine on device, so they spoke
                mPath.lineTo(mX, mY);
                mCanvas.drawPath(mPath, mPaint);
                //mPath.reset();
                mPath.moveTo(mX, mY);
                // update circle, following finger touch.
                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
                Log.i("touch location", "x:" + mX + ", y: " + mY);
            }
        }

        private void touch_up() {
            // Remove old file
            if (pngPath != null) {
                File toDeleteFile = new File(pngPath);
                boolean success = toDeleteFile.delete();
                if (success)
                    Log.i("DELETE FILE", "success");
                else
                    Log.i("DELETE FILE", "failed");
            }
            // reset brush icon.
            circlePath.reset();
            // Start filtering out chosen words.
            // draw black to start
            saveImageCanvas.drawColor(Color.BLACK);
            // git rid of black area in picked region
            // retrieve picked region
            Paint retrievePaint = new Paint();
            retrievePaint.setAntiAlias(true);
            retrievePaint.setDither(true);
            retrievePaint.setColor(Color.WHITE);
            retrievePaint.setStyle(Paint.Style.STROKE);
            retrievePaint.setStrokeJoin(Paint.Join.ROUND);
            retrievePaint.setStrokeCap(Paint.Cap.ROUND);
            retrievePaint.setStrokeWidth(60);
            retrievePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // remain only src.
            saveImageCanvas.drawPath(mPath, retrievePaint);
            // draw background img through the filter
            retrievePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)); // remain only src.
            // Get filtered background img and resize to displaysize.
            // instead of realsize, which might be larger.
            Point size = new Point();
            Display display = getWindowManager().getDefaultDisplay();
            display.getSize(size);
            // Try hard code size as 1700.
            size.y = 1700;
            Log.i("SIZE",
                    "Size of display w/o bar" + size.x + " " + (size.y - getNavBarHeight()));

            Bitmap backgroundBitmap = resizeBitmap(
                    ((BitmapDrawable) backgroundDrawable).getBitmap(),
                    size);
            saveImageCanvas.drawBitmap(backgroundBitmap,
                    0, 0, retrievePaint); // Offset
            // Display Found Covered area by path with Rectangle.
            RectF rectF = new RectF();
            mPath.computeBounds(rectF, true);
            rectF = getRectWithOffset(rectF, offset);
            mCanvas.drawRect(rectF, circlePaint);
            // crop bitmap
            canvasBitmap = Bitmap.createBitmap(canvasBitmap,
                    (int) rectF.left, (int) rectF.top,
                    (int) (rectF.right - rectF.left),
                    (int) (rectF.bottom - rectF.top));
            // cropped bitmap.
            pngPath = print_png(canvasBitmap);
            String rec_text = recognize_text(canvasBitmap);
            // Reset out path
            mPath.reset();
        }

        public int getNavBarHeight() {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
            return 0;
        }

        public void setBackgroundDrawable(Drawable bitmapDrawable,
                                          boolean custom) {
            backgroundDrawable = bitmapDrawable;
            Point size = new Point();
            Display display = getWindowManager().getDefaultDisplay();
            display.getRealSize(size);
            Drawable backgroundImg = resizeDrawable(bitmapDrawable, size);
            setBackgroundDrawable(backgroundImg);
            int width = size.x;
            int height = size.y;
            scaledWidth = width;
            scaledHeight = height;
            Log.i("setBackGround", String.format("Width and Height set" + scaledHeight));
        }

        private String print_png(Bitmap bitmap) {
            // Save bitmap
            String DATA_PATH = getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                    .getAbsolutePath();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSSZ").format(new Date());
            String imageFileName = "/PNG_" + timeStamp + ".png";
            File file = new File(DATA_PATH + imageFileName);
            // output image
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                return file.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private String recognize_text(Bitmap bitmap) {
            TessBaseAPI baseAPI = new TessBaseAPI();
            String DATA_PATH = getExternalFilesDir(Environment.getDataDirectory()
                    .getAbsolutePath()).getAbsolutePath() + "/";
            String lang = "eng";
            baseAPI.init(DATA_PATH, lang);

            // get clearer bitmap
            bitmap = textcleaner(bitmap);
            print_png(bitmap);

            baseAPI.setImage(bitmap);
            String recognizedText = baseAPI.getUTF8Text();
            Log.i("RECOG_TEXT", recognizedText);
            Toast.makeText(this.context, recognizedText, Toast.LENGTH_LONG).show();
            baseAPI.end();
            //check_spelling(recognizedText);
            return recognizedText;
        }

        private void check_spelling(String string) {
            fetchSuggestionsFor(string);
        }

        private String onGetSpellingCheck(String resultString) {
            Log.i("SPELL CHECK", resultString);
            Toast.makeText(this.context, resultString, Toast.LENGTH_LONG).show();
            return resultString;
        }

        private RectF getRectWithOffset(RectF rectF, float offset) {
            float left = rectF.left - offset;
            float right = rectF.right + offset;
            float top = rectF.top - offset;
            float bottom = rectF.bottom + offset;
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            if (left < 0) left = 0;
            if (top < 0) top = 0;
            if (right > size.x) right = size.x;
            if (bottom > size.y) bottom = size.y;
            RectF result = rectF;
            result.set(left, top, right, bottom);
            return result;
        }

        // SpellCheckerClient
        @Override
        public void onGetSuggestions(SuggestionsInfo[] results) {

        }

        @Override
        public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
            final StringBuffer sb = new StringBuffer("");
            for(SentenceSuggestionsInfo result:results){
                int n = result.getSuggestionsCount();
                for(int i=0; i < n; i++){
                    int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();

                    if((result.getSuggestionsInfoAt(i).getSuggestionsAttributes() &
                            SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO) != SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO )
                        continue;

                    for(int k=0; k < m; k++) {
                        sb.append(result.getSuggestionsInfoAt(i).getSuggestionAt(k))
                                .append("\n");
                    }
                    sb.append("\n");
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onGetSpellingCheck(sb.toString());
                }
            });
        }

        public void fetchSuggestionsFor(String input) {
            TextServicesManager tsm =
                    (TextServicesManager) getSystemService(TEXT_SERVICES_MANAGER_SERVICE);
            SpellCheckerSession session =
                    tsm.newSpellCheckerSession(null, Locale.ENGLISH, this, true);
            session.getSentenceSuggestions(new TextInfo[] {new TextInfo(input)}, 5);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawingView(this);
        // setContentView(R.layout.activity_paint);
        setContentView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(60);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        // Hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // load img
        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        String path = (String) extras.get("IMAGE");
        Drawable backgroundImg = Drawable.createFromPath(path);
        // Resize img
        if (backgroundImg != null) {
            dv.setBackgroundDrawable(backgroundImg, true);
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, Point newSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = newSize.x;
        int newHeight = newSize.y;
        Log.i("RESIZE_BITMAP", String.format("Old size: " + width + ", "
                +height + ", new size: " + newWidth + ", " + newHeight));
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        //return Bitmap.createBitmap(bitmap, 0, 0,
//                width, height, matrix, true);
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }

    private Drawable resizeDrawable(Drawable drawable, Point newSize) {
        if (drawable == null) return null;
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap resizedBitmap = resizeBitmap(bitmap, newSize);
        return new BitmapDrawable(resizedBitmap);
    }

    private String saveMagickImageToFile(MagickImage magickImage, String str) {
        String DATA_PATH = getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                .getAbsolutePath();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSSZ").format(new Date());
        String imageFileName = "/PNG_" + timeStamp + str + ".png";
        File file = new File(DATA_PATH + imageFileName);
        // output image
        try {
            Bitmap bitmap = MagickBitmap.ToBitmap(magickImage);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap textcleaner(Bitmap bitmap) {
        try {
            MagickImage magickImage = MagickBitmap.fromBitmap(bitmap);
            boolean result;
            // convert to grayscale
            magickImage.setGrayscale();
            // enhance stretch, constrast sharpen
            result = magickImage.contrastImage(true);
            // enhance
            magickImage = magickImage.enhanceImage();
            // normalize
            result = magickImage.normalizeImage();
            // sharpen
            magickImage = magickImage.sharpenImage(1, 1);
            return MagickBitmap.ToBitmap(magickImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cam_paint, menu);
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
