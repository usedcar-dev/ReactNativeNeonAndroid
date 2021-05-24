package com.gaadi.neon.interfaces;

import com.gaadi.neon.model.ImageTagModel;
import com.gaadi.neon.util.CustomParameters;
import com.gaadi.neon.util.FileInfo;

import java.util.List;

/**
 * @author princebatra
 * @version 1.0
 * @since 25/1/17
 */
public interface IParam {

    /**
     * Pass 0 if you want no restriction in number of photos
     */
    int getNumberOfPhotos();

    /**
     * If passes true then  List<ImageTagModel> must not be null or empty
     */
    boolean getTagEnabled();

    List<ImageTagModel> getImageTagsModel();

    List<FileInfo> getAlreadyAddedImages();

    boolean enableImageEditing();

    CustomParameters getCustomParameters();

}
