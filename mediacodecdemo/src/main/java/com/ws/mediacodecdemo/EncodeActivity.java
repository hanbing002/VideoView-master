package com.ws.mediacodecdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;


@SuppressWarnings("deprecation")
public class EncodeActivity extends Activity implements SurfaceHolder.Callback, PreviewCallback, TextureView.SurfaceTextureListener
{
	public static final String TAG = "EncodeActivity";

	AvcEncoder avcCodec;

    public Camera m_camera = null;
    private SurfaceView m_prevewview = null;
    //private TextureView textureView = null;
	private Context mContext = null;
	private byte[][] mPreviewBuffer = null;

    SurfaceHolder m_surfaceHolder;
    int width = 1920;	//1280;
    int height = 1080;	//720;
    int framerate = 30;	//15;
    int bitrate = 2048000;	//125000;
    //int bitRate = camera.getFpsRange()[1] * currentSize.width * currentSize.height / 15;
     
    byte[] h264 = new byte[width*height*3/2];

//    private FileOutputStream file = null;
//    private String filename = "camera.h264";
    private int byteOffset = 0;
    private long lastTime = 0;
	private int mLastFrameCount = 0;
	private int mFrameCount = 0;
	private long mLastTime = 0;

//	private String mImagePath;
	private int cameraId;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		mImagePath = Environment.getExternalStorageDirectory().getPath() + "/avcCodec/";
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectAll()   // or .detectAll() for all detectable problems
        .penaltyLog()
        .build());
StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .penaltyLog()
        .penaltyDeath()
        .build());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encode);
		Log.d(TAG, "onCreate: " + "wxh=" + width + "x" + height + ", fps=" + framerate + ", br=" + bitrate);

		try {
			//初始化编码器,在有的机器上失败
			avcCodec = new AvcEncoder(width,height,framerate,bitrate);
		} catch (IOException e1) {
			Log.d(TAG, "onCreate: Fail to new AvcEncoder");
		}

		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//取消状态栏
        m_prevewview = (SurfaceView ) findViewById(R.id.surfaceViewPlay);
//		m_prevewview = (TextureView) findViewById(R.id.surfaceViewPlay);

//		TextureView textureView = new TextureView(this);
//		textureView.setSurfaceTextureListener(this);
//		textureView.setRotation(45.0f);//可以像普通View一样使用平移、缩放、旋转等变换操作
//		textureView.setAlpha(0.5f);
//		setContentView(textureView);

		m_surfaceHolder = m_prevewview.getHolder();
		m_surfaceHolder.setFixedSize(width, height);
		m_surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		m_surfaceHolder.addCallback((Callback) this);

//		m_prevewview.setSurfaceTextureListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(m_camera == null) {
			if (Camera.getNumberOfCameras() > 0) {
				m_camera = Camera.open(0);
				Camera.Parameters params = m_camera.getParameters();
				params.setPreviewSize(width, height);
				params.setPictureSize(width, height);
				//params.setPreviewFormat(ImageFormat.YV12);
				//params.setRotation(180);
				m_camera.setParameters(params);
				//m_camera.setDisplayOrientation(180);
				int size = params.getPreviewSize().width * params.getPreviewSize().height * 3 /2;
				mPreviewBuffer = new byte[][]{new byte[size], new byte[size],new byte[size]};
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (m_camera != null) {
			m_camera.setPreviewCallbackWithBuffer(null);
			m_camera.stopPreview();
			m_camera.release();
			m_camera = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		if (m_camera == null) {
			return;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			if (m_camera != null) {
//				for (byte[] buffer : mPreviewBuffer)
//					m_camera.addCallbackBuffer(buffer);
//
//				m_camera.setPreviewCallbackWithBuffer(this);
				m_camera.setPreviewDisplay(m_surfaceHolder);

				m_camera.startPreview();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try
//		{
//			m_camera = Camera.open();
////			m_camera = Camera.open(cameraId);
//			m_camera.setPreviewDisplay(m_surfaceHolder);
//			Camera.Parameters parameters = m_camera.getParameters();
//			parameters.setPreviewSize(width, height);
//			parameters.setPictureSize(width, height);
//			parameters.setPreviewFormat(ImageFormat.YV12);
//			//parameters.set("rotation", 270);
//			//parameters.set("orientation", "portrait");
//			m_camera.setParameters(parameters);
//			//m_camera.setDisplayOrientation(270);
//
//			int size = width * height * 3 /2;
//			mPreviewBuffer = new byte[][]{new byte[size], new byte[size],new byte[size]};
//			for (byte[] buffer : mPreviewBuffer)
//				m_camera.addCallbackBuffer(buffer);
//
////			int len = width * height * ImageFormat.getBitsPerPixel(ImageFormat.YV12) / 8;
////			m_camera.addCallbackBuffer(new byte[len]);
//
//			m_camera.setPreviewCallbackWithBuffer((PreviewCallback) this);
//			m_camera.startPreview();
//
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
		

//		try {
//			File fileFolder = new File(mImagePath);
//			if (!fileFolder.exists())
//				fileFolder.mkdirs();
//			File files = new File(mImagePath, filename);
//			if (!files.exists()) {
//				Log.e(TAG, "file create success ");
//				files.createNewFile();
//			}
//			file = new FileOutputStream(files);
//			Log.e(TAG, "file save success ");
//		} catch (IOException e) {
//			Log.e(TAG, e.toString());
//			e.printStackTrace();
//		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0)
	{
//		m_camera.setPreviewCallback(null);
//		m_camera.setPreviewCallbackWithBuffer(null);
//		m_camera.release();
		m_camera = null; 
		avcCodec.close();
//		try {
//			file.flush();
//			file.close();
//		} catch (IOException e) {
//			Log.d(TAG, "surfaceDestroyed: File close error");
//			e.printStackTrace();
//		}
	}

	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera)
	{
//		long newTime = System.currentTimeMillis();
//		long diff = newTime - lastTime;
//		lastTime = newTime;
//		mFrameCount ++;
//		Log.i(TAG, "onPreviewFrame: " + mFrameCount);

        //把摄像头的数据传给编码器
//		int ret = avcCodec.offerEncoder(data,h264);

//		if(ret > 0)
//		{
//			try {
//				byte[] length_bytes = intToBytes(ret);
//				file.write(length_bytes);
//				file.write(h264, 0, ret);
//
//			} catch (IOException e) {
//				Log.d(TAG, "onPreviewFrame exception: " + e.toString());
//			}
//		}

		m_camera.addCallbackBuffer(data);
		showFps();
	}


	private void showFps() {
		mFrameCount++;
		if (mLastTime == 0)
			mLastTime = System.currentTimeMillis();
		if (System.currentTimeMillis() - mLastTime > 1000) {
			Log.d(TAG, "current fps:" + mFrameCount);
			mFrameCount = 0;
			mLastTime = System.currentTimeMillis();
		}

	}

	public static byte[] intToBytes( int value )   
	{   
	    byte[] src = new byte[4];  
	    src[3] =  (byte) ((value>>24) & 0xFF);  
	    src[2] =  (byte) ((value>>16) & 0xFF);  
	    src[1] =  (byte) ((value>>8) & 0xFF);    
	    src[0] =  (byte) (value & 0xFF);                  
	    return src;   
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureAvailable: width = " + width);
		try {
			if (m_camera != null) {
				for (byte[] buffer : mPreviewBuffer)
					m_camera.addCallbackBuffer(buffer);

				m_camera.setPreviewCallbackWithBuffer(this);
				m_camera.setPreviewTexture(surface);

				m_camera.startPreview();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		m_camera = null;
		avcCodec.close();
		return true;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {

	}
}