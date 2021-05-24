package com.gaadi.neon.interfaces;

import com.gaadi.neon.enumerations.CameraFacing;
import com.gaadi.neon.enumerations.CameraOrientation;
import com.gaadi.neon.enumerations.CameraType;

/**
 * @author princebatra
 * @version 1.0
 * @since 25/1/17
 */
public interface ICameraParam extends IParam{

    CameraFacing getCameraFacing();

    CameraOrientation getCameraOrientation();

    boolean getFlashEnabled();

    boolean getCameraSwitchingEnabled();

    boolean getVideoCaptureEnabled();

    CameraType getCameraViewType();

    boolean cameraToGallerySwitchEnabled();

}
