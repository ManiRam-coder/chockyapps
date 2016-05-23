package com.nidea.app.selfiethief;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.nidea.app.selfiethief.receiver.AdminReceiver;
import com.nidea.app.selfiethief.support.Util;

public class StreamActivity extends AppCompatActivity {
	private static final int REQUEST_CODE_ENABLE_ADMIN = 1;
	private GridView gridView;
	private ComponentName mDeviceAdminSample;
	private DevicePolicyManager mDPM;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDeviceAdminSample = new ComponentName(getApplicationContext(), AdminReceiver.class);
		mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		startAdminIntent(this);
		Util.createFolder(getApplicationContext());
		toolBarStyle();
		StreamAdapter adapter = new StreamAdapter(this);
		gridView = ((GridView) findViewById(R.id.grid_view));
		gridView.setAdapter(adapter);
		initiateGridView(adapter);
	}

	private void toolBarStyle(){
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setLogo(R.drawable.ic_launcher);
		getSupportActionBar().setDisplayUseLogoEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}
	private void startAdminIntent(Context context){
		if (!mDPM.isAdminActive(mDeviceAdminSample)) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
			startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void initiateGridView(StreamAdapter adapter){

		String externalStorageDirectoryPath = Environment
				.getExternalStorageDirectory()
				.getAbsolutePath();

		String targetPath = externalStorageDirectoryPath + "/"+getApplicationContext().getResources().getString(R.string.folder_name)+"/";

		//Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
		File targetDirector = new File(targetPath);

		File[] files = targetDirector.listFiles();

		if(files != null){
			Arrays.sort(files, new Comparator<File>(){
				@Override
				public int compare(File f1, File f2)
				{
					return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
				} });

			for (File file : files){
				long modified = file.lastModified();
				Date date = new Date(modified);
				//date.getDate();
				SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM, yyyy");
				SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm:ss");
				//df2.format(date);
				//adapter.add(file.getAbsolutePath(),dateSDF.format(date),timeSDF.format(date),file.getAbsolutePath());
				adapter.add(new StreamItem(this, file.getAbsolutePath(), dateSDF.format(date), timeSDF.format(date)));
			}
		}
		/**
		 * On Click event for Single Gridview Item
		 * */
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intentFull = new Intent(getApplicationContext(), FullImageActivity.class);
				StreamAdapter imageAd = (StreamAdapter) parent.getAdapter();
				//String strLine1 = imageAd.getItem(position).mLine1;

				// passing array index
				intentFull.putExtra("date", imageAd.getItem(position).mLine1);
				intentFull.putExtra("time", imageAd.getItem(position).mLine2);
				intentFull.putExtra("imagepath", imageAd.getItem(position).mImagePath);
				startActivity(intentFull);
			}
		});
	}

	class StreamItem {
		final Bitmap mBitmap;
		final String mLine1;
		final String mLine2;
		final String mImagePath;

		StreamItem(Context c, String imagePath, String line1, String line2) {
			//String externalStorageDirectoryPath = Environment .getExternalStorageDirectory().getAbsolutePath();

			//String folderName = externalStorageDirectoryPath+"/"+getApplicationContext().getResources().getString(R.string.folder_name)+"/"+"2.jpg";
			mBitmap = decodeSampledBitmapFromUri(imagePath, 220, 220);
			//mBitmap = BitmapFactory.decodeResource(c.getResources(), resid);
			mLine1 = line1;
			mLine2 = line2;
			mImagePath = imagePath;
		}

		public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

			Bitmap bm = null;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(path, options); 

			return bm;   
		}

		public int calculateInSampleSize(

				BitmapFactory.Options options, int reqWidth, int reqHeight) {
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {
				if (width > height) {
					inSampleSize = Math.round((float)height / (float)reqHeight);    
				} else {
					inSampleSize = Math.round((float)width / (float)reqWidth);    
				}   
			}

			return inSampleSize;    
		}
	}

	class StreamDrawable extends Drawable {
		private static final boolean USE_VIGNETTE = true;

		private final float mCornerRadius;
		private final RectF mRect = new RectF();
		private final BitmapShader mBitmapShader;
		private final Paint mPaint;
		private final int mMargin;

		StreamDrawable(Bitmap bitmap, float cornerRadius, int margin) {
			mCornerRadius = cornerRadius;

			mBitmapShader = new BitmapShader(bitmap,
					Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setShader(mBitmapShader);

			mMargin = margin;
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			mRect.set(mMargin, mMargin, bounds.width() - mMargin, bounds.height() - mMargin);

			if (USE_VIGNETTE) {
				RadialGradient vignette = new RadialGradient(
						mRect.centerX(), mRect.centerY() * 1.0f / 0.7f, mRect.centerX() * 1.3f,
						new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
						Shader.TileMode.CLAMP);

				Matrix oval = new Matrix();
				oval.setScale(1.0f, 0.7f);
				vignette.setLocalMatrix(oval);

				mPaint.setShader(
						new ComposeShader(mBitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
			}
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mPaint);
		}

		@Override
		public int getOpacity() {
			return PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
			mPaint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			mPaint.setColorFilter(cf);
		}		
	}


	class StreamAdapter extends ArrayAdapter<StreamItem> {
		private static final int CORNER_RADIUS = 24; // dips
		private static final int MARGIN = 12; // dips

		private final int mCornerRadius;
		private final int mMargin;
		private final LayoutInflater mInflater;

		public StreamAdapter(Context context) {
			super(context, 0);

			final float density = context.getResources().getDisplayMetrics().density;
			mCornerRadius = (int) (CORNER_RADIUS * density + 0.5f);
			mMargin = (int) (MARGIN * density + 0.5f);

			mInflater = LayoutInflater.from(getContext());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewGroup view = null;

			if (convertView == null) {
				view = (ViewGroup) mInflater.inflate(R.layout.stream_item, parent, false);
			} else {
				view = (ViewGroup) convertView;
			}

			StreamItem item = getItem(position);

			StreamDrawable d = new StreamDrawable(item.mBitmap, mCornerRadius, mMargin);
			view.setBackground(d);

			((TextView) view.findViewById(R.id.textView1)).setText(item.mLine1);
			((TextView) view.findViewById(R.id.textView2)).setText(item.mLine2);

			int w = item.mBitmap.getWidth();
			int h = item.mBitmap.getHeight();

			float ratio = w / (float) h;

			LayoutParams lp = view.getLayoutParams();
			lp.width = getContext().getResources().getDisplayMetrics().widthPixels/2;
			lp.height = (int) (lp.width / ratio);

			return view;
		}
	}
}
