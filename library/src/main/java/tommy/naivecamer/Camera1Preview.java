package tommy.naivecamer;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author : Tommmy
 * @e-mail : tommyfenv@163.com
 * @date : 2018/7/30
 * @desc: 自定义SurfaceView 类，用于相机的预览层
 */
public class Camera1Preview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "Camera1Preview";

    private SurfaceHolder mHolder;

    private Camera mCamera;

    public Camera1Preview(Context context) {
        this(context, null);
    }

    public Camera1Preview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Camera1Preview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    private static Camera getCameraInstance() {
        Camera camera = null;
        try {
//            int cameraCount = Camera.getNumberOfCameras();
            //前置摄像头
            camera = Camera.open(0);
            Camera.Parameters parameters = camera.getParameters();
//            parameters.setRotation(90);
            parameters.setMeteringAreas(null);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
        } catch (Exception e) {
            Log.w(TAG, "getCameraInstance: " + e.getMessage());
        }
        return camera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //初始化时调用
        mCamera = getCameraInstance();
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.w(TAG, "surfaceCreated: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //销毁时调用
        mHolder.removeCallback(this);
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

    }
}
