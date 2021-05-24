package com.gaadi.neon.model;

import com.gaadi.neon.interfaces.ICameraParam;

/**
 * @author princebatra
 * @version 1.0
 * @since 25/1/17
 */
public class CameraModel {

    public PhotosMode setParams(ICameraParam iParam){
        return new PhotosMode(iParam);
    }
}
