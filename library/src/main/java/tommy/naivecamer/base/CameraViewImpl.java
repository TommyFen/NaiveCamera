package tommy.naivecamer.base;

import android.hardware.Camera;

/**
 * @author : Tommy
 * @e-mail : tommyfenv@163.com
 * @date : 2018/10/17
 * @desc:
 */
public interface CameraViewImpl {

    /**
     * 开始预览
     */
    void start();

    /**
     * 停止预览
     */
    void stop();

    /**
     * 设置摄像头
     * @param facing 表示前置或者后置摄像头
     */
    void setFacing(int facing);

    /**
     * 获取当前的摄像头
     * @return
     */
    int getFacing();

    /**
     * 判断 Camera 是否打开
     * @return
     */
    boolean isCameraOpened();

    void takePicture(Camera.PictureCallback callback);

}
