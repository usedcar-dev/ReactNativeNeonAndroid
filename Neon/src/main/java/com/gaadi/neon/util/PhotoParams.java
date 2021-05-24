package com.gaadi.neon.util;

import com.gaadi.neon.model.ImageTagModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lakshaygirdhar
 * @since 13-08-2016
 */
public class PhotoParams implements Serializable {

    private String imageName;
    private CameraOrientation orientation;
    private int noOfPhotos;
    private FolderOptions folderOptions;
    private String uploadApi;
    private MODE mode = MODE.NEUTRAL;
    private ArrayList<?> imagePathList;
    private ArrayList<ImageTagModel> imageTags;
    private CameraFacing cameraFace;

    private boolean cameraFaceSwitchEnabled;
    private boolean tagEnabled;
    private boolean metaEnabled;
    private boolean enableCapturedReview;
    private boolean enableExtraBrightness;
    private boolean restrictedExtensionEnabled;
    private boolean galleryFromCameraEnabled;
    private boolean cameraHorizontalPreviewEnabled;
    private boolean flashOptionsEnabled;

    public PhotoParams() {

    }

    public boolean isCameraFaceSwitchEnabled() {
        return cameraFaceSwitchEnabled;
    }

    public void setCameraFaceSwitchEnabled(boolean cameraFaceSwitchEnabled) {
        this.cameraFaceSwitchEnabled = cameraFaceSwitchEnabled;
    }

    public boolean isFlashOptionsEnabled() {
        return flashOptionsEnabled;
    }

    public void setFlashOptionsEnabled(boolean flashOptionsEnabled) {
        this.flashOptionsEnabled = flashOptionsEnabled;
    }

    public boolean isTagEnabled() {
        return tagEnabled;
    }

    public void setTagEnabled(boolean tagEnabled) {
        this.tagEnabled = tagEnabled;
    }

    public boolean isMetaEnabled() {
        return metaEnabled;
    }

    public void setMetaEnabled(boolean metaEnabled) {
        this.metaEnabled = metaEnabled;
    }

    public boolean isEnableCapturedReview() {
        return enableCapturedReview;
    }

    public void setEnableCapturedReview(boolean enableCapturedReview) {
        this.enableCapturedReview = enableCapturedReview;
    }

    public boolean isEnableExtraBrightness() {
        return enableExtraBrightness;
    }

    public void setEnableExtraBrightness(boolean enableExtraBrightness) {
        this.enableExtraBrightness = enableExtraBrightness;
    }

    public ArrayList<ImageTagModel> getImageTags() {
        return imageTags;
    }

    public void setImageTags(ArrayList<ImageTagModel> imageTags) {
        this.imageTags = imageTags;
    }

    public boolean isCameraHorizontalPreviewEnabled() {
        return cameraHorizontalPreviewEnabled;
    }

    public void setCameraHorizontalPreviewEnabled(boolean cameraHorizontalPreviewEnabled) {
        this.cameraHorizontalPreviewEnabled = cameraHorizontalPreviewEnabled;
    }

    public boolean isGalleryFromCameraEnabled() {
        return galleryFromCameraEnabled;
    }

    public void setGalleryFromCameraEnabled(boolean galleryFromCameraEnabled) {
        this.galleryFromCameraEnabled = galleryFromCameraEnabled;
    }

    public CameraFacing getCameraFace() {
        return cameraFace;
    }

    public void setCameraFace(CameraFacing cameraFace) {
        this.cameraFace = cameraFace;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ArrayList<?> getImagePathList() {
        return imagePathList;
    }

    public void setImagePathList(ArrayList<?> imagePathList) {
        this.imagePathList = imagePathList;
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public int getNoOfPhotos() {
        return noOfPhotos;
    }

    public void setNoOfPhotos(int noOfPhotos) {
        this.noOfPhotos = noOfPhotos;
    }

    public CameraOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(CameraOrientation orientation) {
        this.orientation = orientation;
    }

    public FolderOptions getFolderOptions() {
        return folderOptions;
    }

    public void setFolderOptions(FolderOptions folderOptions) {
        this.folderOptions = folderOptions;
    }

    public String getUploadApi() {
        return uploadApi;
    }

    public void setUploadApi(String uploadApi) {
        this.uploadApi = uploadApi;
    }

    public boolean getEnableExtraBrightness() {
        return enableExtraBrightness;
    }

    public void setEnableExtraBrightness(Boolean enableExtraBrightness) {
        this.enableExtraBrightness = enableExtraBrightness;
    }

    public boolean isRestrictedExtensionEnabled() {
        return restrictedExtensionEnabled;
    }

    public void setRestrictedExtensionEnabled(boolean restrictedExtensionEnabled) {
        this.restrictedExtensionEnabled = restrictedExtensionEnabled;
    }

    private enum FolderOptions {
        PUBLIC_DIR, PUBLIC_DIR_DCIM, PUBLIC_DIR_SOCIAL, PUBLIC_DIR_ALL;
    }

    public enum CameraOrientation {
        LANDSCAPE, PORTRAIT, BOTH;
    }

    public enum CameraFacing {
        FRONT, BACK
    }

    public enum MODE {CAMERA_PRIORITY, NEUTRAL, GALLERY_PRIORITY, CAMERA_ONLY, GALLERY_ONLY}
}
