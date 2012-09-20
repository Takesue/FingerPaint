package sample.application.fingerpaint;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;


public class FingerPaintActivity extends Activity implements OnTouchListener{
	
	public Canvas	canvas;
	public Paint	paint;
	public Path		path;
	public Bitmap	bitmap;
	public Float	x1, y1;
	public Integer	w, h;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.fingerpaint);
		
		ImageView iv = (ImageView) this.findViewById(R.id.imageView1);
		WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
		
		Display disp = ((WindowManager)this.getSystemService(
				Context.WINDOW_SERVICE)).getDefaultDisplay();
		this.w = disp.getWidth();
		this.h = disp.getHeight();
		this.bitmap = Bitmap.createBitmap(this.w, this.h, Bitmap.Config.ARGB_8888);
		this.paint = new Paint();
		this.path = new Path();
		this.canvas = new Canvas(this.bitmap);
		
		this.paint.setStrokeWidth(5);
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setStrokeJoin(Paint.Join.ROUND);
		this.canvas.drawColor(Color.WHITE);
		iv.setImageBitmap(this.bitmap);
		iv.setOnTouchListener(this);
		
	}


	public boolean onTouch (View v, MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.path.reset();
			this.path.moveTo(x, y);
			this.x1 = x;
			this.y1 = y;
			break;
		case MotionEvent.ACTION_MOVE:
			this.path.quadTo(x1, y1, x, y);
			this.x1 = x;
			this.y1 = y;
			this.canvas.drawPath(this.path, this.paint);
			this.path.reset();
			this.path.moveTo(x, y);
			break;
		case MotionEvent.ACTION_UP:
			if ((x == this.x1) && (y == this.y1)) {
				this.y1 = this.y1 + 1;
			}
			this.path.quadTo(x1, y1, x, y);
			this.canvas.drawPath(this.path, this.paint);
			this.path.reset();
			break;
		}
		
		ImageView iv = (ImageView) this.findViewById(R.id.imageView1);
		iv.setImageBitmap(bitmap);
		
		return true;
	}
	
	
	public void save() {
		
		SharedPreferences prefs = this.getSharedPreferences("FingarPraintPreferences", Context.MODE_PRIVATE);
		int imageNumber = prefs.getInt("imageNumber", 1);
		File file = null;
		
		if (this.externalMediaChecher()) {
			DecimalFormat form = new DecimalFormat("0000");
			String path = Environment.getExternalStorageDirectory() + "/mypaint/";
			File outDir = new File(path);
			
			if (!outDir.exists()) {
				outDir.mkdir();
			}
			
			do {
				file = new File(path + "img" + form.format(imageNumber) + ".pmg");
				imageNumber++;
			}
			while(file.exists());
			
			if (this.writeImage(file)) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("imageNumber", imageNumber);
				editor.commit();
			}
		}
	}
	
	private boolean writeImage(File file) {

		try {
			FileOutputStream fo = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, fo);
			fo.flush();
			fo.close();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	/**
	 * 外部メディアがマウントされているか確認するメソッド
	 * @return
	 */
	private boolean externalMediaChecher() {
		boolean result = false;
		String status = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(status)) {
			result = true;
		}
		return result;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = this.getMenuInflater();
		mi.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case R.id.menu_save:
			this.save();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
}
