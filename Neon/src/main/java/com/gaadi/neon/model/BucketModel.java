package com.gaadi.neon.model;

/**
 * @author princebatra
 * @version 1.0
 * @since 2/2/17
 */
public class BucketModel {

    private String bucketId;
    private String bucketName;
    private int fileCount;
    private String bucketCoverImagePath;

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int imagesCount) {
        this.fileCount = imagesCount;
    }

    public String getBucketCoverImagePath() {
        return bucketCoverImagePath;
    }

    public void setBucketCoverImagePath(String bucketCoverImagePath) {
        this.bucketCoverImagePath = bucketCoverImagePath;
    }
}
