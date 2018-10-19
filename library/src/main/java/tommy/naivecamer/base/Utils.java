package tommy.naivecamer.base;

import android.content.Context;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.Objects;

/**
 * @author : Tommy
 * <p>
 * Camera Utils
 */
public final class Utils {

    /**
     * Get orientation that camera need to rotate
     * <p>
     * Android API: Display Orientation Setting
     * Just change screen display orientation,
     * the rawFrame data never be changed.
     *
     * @param context
     * @param facing
     * @return
     */
    public static int getDisplayOrientation(Context context, int facing) {

        Display display = ((WindowManager) Objects.requireNonNull(context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(facing, cameraInfo);

        int displayDegree;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            displayDegree = (cameraInfo.orientation + degrees) % 360;
            displayDegree = (360 - displayDegree) % 360;

        } else {
            displayDegree = (cameraInfo.orientation - degrees + 360) % 360;
        }

        return displayDegree;
    }
}
