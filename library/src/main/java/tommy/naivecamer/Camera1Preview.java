package tommy.naivecamer;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import tommy.naivecamer.base.CameraViewImpl;
import tommy.naivecamer.base.Utils;

/**
 * @author : Tommy
 * <p>
 * 针对于 Camera
 * 自定义SurfaceView 类，用于相机的预览层
 */
@SuppressWarnings("deprecation")
public class Camera1Preview extends SurfaceView implements SurfaceHolder.Callback, CameraViewImpl {

    private static final String TAG = "Camera1Preview";

    private static final int INVALID_CAMERA_ID = -1;

    private SurfaceHolder mHolder;

    private Camera mCamera;

    private int facing = INVALID_CAMERA_ID;

    private int mFrontCameraId;

    private int mBackCameraId;

    private int mFrontCameraOriention;

    private int mBackCameraOriention;

    private Camera.Parameters mCameraParameters;

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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        mCamera = getCameraInstance();
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        adjustDisplayRatio(Utils.getDisplayOrientation(getContext(), facing));
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mHolder.removeCallback(this);
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

    }

    public Camera getCamera() {
        return mCamera;
    }

    @Override
    public void start() {
        if (null != mCamera) {
//            mCamera.startPreview();
        }
    }

    @Override
    public void stop() {
        if (null != mCamera) {
            mCamera.stopPreview();
        }
    }

    @Override
    public void setFacing(int facing) {
        this.facing = facing;
    }

    @Override
    public int getFacing() {
        return facing;
    }

    @Override
    public boolean isCameraOpened() {
        return false;
    }

    @Override
    public void takePicture(Camera.PictureCallback callback) {
        if (null == mCamera) return;
        mCamera.takePicture(null, null, callback);
    }

    /**
     * choose Camera which you want, front one or back one
     */
    private void chooseCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; ++i) {
            final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

            Camera.getCameraInfo(i, cameraInfo);
            //back camera
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mBackCameraId = i;
                mBackCameraOriention = cameraInfo.orientation;
            }
            //front camera
            else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mFrontCameraId = i;
                facing = i;
                mFrontCameraOriention = cameraInfo.orientation;
            }
        }

    }


    private Camera getCameraInstance() {
        if (mCamera == null) {
            try {
                chooseCamera();
//                open front camera in default
                if (INVALID_CAMERA_ID == facing) {
                    mCamera = Camera.open();
                } else {
                    mCamera = Camera.open(facing);
                }
                setCameraParameters();
                setDisplayOrientation();

            } catch (Exception e) {
                Log.w(TAG, "getCameraInstance: " + e.getMessage());
            }
        }
        return mCamera;
    }

    private void setDisplayOrientation() {
        if (mCamera == null) return;
        mCamera.setDisplayOrientation(Utils.getDisplayOrientation(getContext(), facing));

    }

    private void setCameraParameters() {
        if (mCamera != null) {
            mCameraParameters = mCamera.getParameters();
            //rotation for picture
            mCameraParameters.setRotation(Utils.getDisplayOrientation(getContext(), facing) + 180);
            //关闭测光
            mCameraParameters.setMeteringAreas(null);

            // TODO: 2018/10/18 设置其它属性
            mCamera.setParameters(mCameraParameters);
        }
    }

    private void adjustDisplayRatio(int rotation) {

        ViewGroup parent = ((ViewGroup) getParent());
        Rect rect = new Rect();
        parent.getLocalVisibleRect(rect);
        int width = rect.width();
        int height = rect.height();
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        int previewWidth;
        int previewHeight;
        if (rotation == 90 || rotation == 270) {
            previewWidth = previewSize.height;
            previewHeight = previewSize.width;
        } else {
            previewWidth = previewSize.width;
            previewHeight = previewSize.height;
        }

        if (width * previewHeight > height * previewWidth) {
            final int scaledChildWidth = previewWidth * height / previewHeight;

            layout((width - scaledChildWidth) / 2, 0,
                    (width + scaledChildWidth) / 2, height);
        } else {
            // align bottom side
            final int scaledChildHeight = previewHeight * width / previewWidth;
            layout(0, height - scaledChildHeight, width, height);
        }
    }


}
