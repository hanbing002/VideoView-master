package com.ws.videoview.videoview;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;

import java.io.IOException;

public class CameraTextureActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener,Camera.PreviewCallback{

    public static final String TAG = "CameraTextureActivity";
    private TextureView myTexture;
    private Camera mCamera;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: CameraTextureActivity 1");
        setContentView(R.layout.activity_camera_texture);
        Log.i(TAG, "onCreate: CameraTextureActivity 2");
        myTexture = new TextureView(this);
        // 回调 onSurfaceTextureAvailable onSurfaceTextureDestroyed onSurfaceTextureSizeChanged onSurfaceTextureUpdated
        myTexture.setSurfaceTextureListener(this);
        Log.i(TAG, "onCreate: setContentView");
        setContentView(myTexture);
    }

    @SuppressLint("NewApi")
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
                                          int arg2) {
        Log.i(TAG, "onSurfaceTextureAvailable: Camera.open");
        mCamera = Camera.open();
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        Log.i(TAG, "onSurfaceTextureAvailable: previewSize = " + previewSize.width + "x" + previewSize.height);
        myTexture.setLayoutParams(new FrameLayout.LayoutParams(
                previewSize.width, previewSize.height, Gravity.CENTER));
        try {
            Log.i(TAG, "onSurfaceTextureAvailable: setPreviewTexture");
            mCamera.setPreviewTexture(arg0);
        } catch (IOException t) {
        }
        Log.i(TAG, "onSurfaceTextureAvailable: setPreviewCallback");
        mCamera.setPreviewCallback(this);   // 回调 onPreviewFrame
        mCamera.startPreview();
        myTexture.setAlpha(1.0f);
//        myTexture.setRotation(90.0f);
        Log.i(TAG, "onSurfaceTextureAvailable: ");
    }
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
        Log.i(TAG, "onSurfaceTextureDestroyed: ");
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
                                            int arg2) {
        // TODO Auto-generated method stub
//        Log.i(TAG, "onSurfaceTextureSizeChanged: ");
    }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
        // TODO Auto-generated method stub
//        Log.i(TAG, "onSurfaceTextureUpdated: ");
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
//        Log.i(TAG, "onPreviewFrame: ");
    }
}

