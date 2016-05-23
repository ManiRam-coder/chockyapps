package com.nidea.app.selfiethief.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.IBinder;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.nidea.app.selfiethief.support.Util;


public class CameraService extends Service 
{

	private SurfaceHolder sHolder; 

	private Camera mCamera;

	private Parameters parameters;
	/** Called when the activity is first created. */
	@Override
	public void onCreate()
	{
		super.onCreate();

	}
	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
		int camIdx = openFrontFacingCameraGingerbread();
		if(camIdx > 0){
			mCamera = Camera.open(camIdx);

			SurfaceView sv = new SurfaceView(getApplicationContext());
			try {
				mCamera.setPreviewDisplay(sv.getHolder());
				setparameters();
				//mCamera.setDisplayOrientation(90);
				mCamera.startPreview();
				//setOrie(camIdx);
				mCamera.takePicture(null, null, mCall);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sHolder = sv.getHolder();
			sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
	}

	@SuppressWarnings("deprecation")
	private void setparameters(){
		parameters = mCamera.getParameters();
		List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
		if(sizeList != null && sizeList.size() > 0){
			Size bestSize = sizeList.get(0);
			for(int i = 1; i < sizeList.size(); i++){
				if((sizeList.get(i).width * sizeList.get(i).height) > (bestSize.width * bestSize.height)){
					bestSize = sizeList.get(i);
				}
			}
			if(bestSize != null){
				parameters.setPreviewSize(bestSize.width, bestSize.height);
				parameters.setPictureSize(bestSize.width, bestSize.height);
			}
		}
		mCamera.setParameters(parameters);
	}
	private void setOrie(int camIdx){
		android.hardware.Camera.CameraInfo info = 
				new android.hardware.Camera.CameraInfo();

		android.hardware.Camera.getCameraInfo(camIdx, info);
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		int rotation = info.orientation; 
		//	int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;

		switch (rotation) {
		case Surface.ROTATION_0: degrees = 0; break;
		case Surface.ROTATION_90: degrees = 90; break;
		case Surface.ROTATION_180: degrees = 180; break;
		case Surface.ROTATION_270: degrees = 270; break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		mCamera.setDisplayOrientation(result);
	}
 
	private int openFrontFacingCameraGingerbread() {
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		int camIdx = 0;
		for (camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				break;
			}
		}

		return camIdx;
	}

	Camera.PictureCallback mCall = new Camera.PictureCallback()
	{

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			String imageFilePath = null;
			FileOutputStream outStream = null;
			try {
				//imageFilePath = "/storage/emulated/0/Image.jpg";
				//imageFilePath = createFolder();
				imageFilePath = Util.createFolder(getApplicationContext());
				imageFilePath = imageFilePath+"/"+Util.createFileName();

				InputStream is = new ByteArrayInputStream(data);
				Bitmap bmp = BitmapFactory.decodeStream(is);
				// Getting width & height of the given image.
				if (bmp != null){
					int w = bmp.getWidth();
					int h = bmp.getHeight();
					// Setting post rotate to 90
					Matrix mtx = new Matrix();
					mtx.postRotate(270);
					// Rotating Bitmap
					Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					rotatedBMP.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] byteArray = stream.toByteArray(); 
					outStream = new FileOutputStream
							(String.format(imageFilePath,System.currentTimeMillis()));
					outStream.write(byteArray);
					outStream.close();
				} else {
					outStream = new FileOutputStream
							(String.format(imageFilePath,System.currentTimeMillis()));
					outStream.write(data);
					outStream.close();           
				}       


			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				mCamera.stopPreview();
				mCamera.release();
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}