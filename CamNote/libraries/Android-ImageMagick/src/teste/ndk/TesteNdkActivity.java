package teste.ndk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import fakeawt.Rectangle;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.util.MagickBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TesteNdkActivity extends Activity {
	
	EditText et; Button load;
	ImageView iv;
	Bitmap bmp;
	String formats[] = {"x","xc","xbm","xpm","xwd","yuv"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		et = (EditText) findViewById(R.id.et);
		load = (Button) findViewById(R.id.load);
		iv = (ImageView) findViewById(R.id.iv);
		
		
        load.setOnClickListener(new View.OnClickListener() {			
			@SuppressLint("SdCardPath")
			@Override
			public void onClick(View arg0) {	    
				for(int in=0;in<formats.length;in++){
        
        try {
        	ImageInfo i = new ImageInfo("/sdcard/small.jpg");     	
        	
        	
			MagickImage image = new MagickImage(i);
			
			
				String fn = "/mnt/shared/Emulator_folder/"+"small"+"."+formats[in];
				image.setFileName(fn);
	            ImageInfo info = new ImageInfo(fn);
	            info.setMagick(formats[in]);
	            image.writeImage(info);
	            byte blob[] = image.imageToBlob(info);
	            FileOutputStream fos = new FileOutputStream(fn);
	            fos.write(blob);
	            fos.close();
			
			
			
			
			/**int newHeight = (int) ((640/(float)m.getWidth()) * m.getHeight());
			m = m.scaleImage(640, newHeight);
			m = m.cropImage(new Rectangle((640-480)/2, 0, 480, 480));
			m = m.charcoalImage(0.5, 0.5);
			
			image.setImageFormat("webp");
			String fn = Environment.getExternalStorageDirectory() + "/test.webp";
			image.setFileName(fn);
			ImageInfo info = new ImageInfo(fn);
			info.setMagick("webp");
			//image.writeImage(info);
			byte blob[] = image.imageToBlob(info);
			FileOutputStream fos = new FileOutputStream(fn);
			fos.write(blob);
			fos.close();
			
			iv.setImageBitmap(MagickBitmap.ToBitmap(image));
			
			image = MagickBitmap.fromBitmap(bmp);**/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
				}
        
			}
		});
    }
}