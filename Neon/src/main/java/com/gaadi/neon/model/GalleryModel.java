package com.gaadi.neon.model;

import com.gaadi.neon.interfaces.IGalleryParam;

/**
 * @author princebatra
 * @version 1.0
 * @since 25/1/17
 */
public class GalleryModel {

    public PhotosMode setParams(IGalleryParam galleryParams){
        return new PhotosMode(galleryParams);
    }

}
