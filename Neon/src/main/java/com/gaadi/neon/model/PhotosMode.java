package com.gaadi.neon.model;

import com.gaadi.neon.interfaces.IParam;

/**
 * @author princebatra
 * @version 1.0
 * @since 25/1/17
 */
public class PhotosMode {

    private IParam params;

    public IParam getParams() {
        return params;
    }

    protected PhotosMode(IParam _params){
        params = _params;
    }

    public static GalleryModel setGalleryMode(){
        return new GalleryModel();
    }

    public static CameraModel setCameraMode(){
        return new CameraModel();
    }

    public static NeutralModel setNeutralMode(){
        return new NeutralModel();
    }


}
