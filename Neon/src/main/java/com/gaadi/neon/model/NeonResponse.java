package com.gaadi.neon.model;

import com.gaadi.neon.enumerations.ResponseCode;
import com.gaadi.neon.util.FileInfo;

import java.util.HashMap;
import java.util.List;

/**
 * @author princebatra
 * @version 1.0
 * @since 29/03/17
 */
public class NeonResponse {
    private HashMap<String,List<FileInfo>> imageTagsCollection;
    private List<FileInfo> imageCollection;
    private ResponseCode responseCode;
    private int requestCode;

    public HashMap<String, List<FileInfo>> getImageTagsCollection() {
        return imageTagsCollection;
    }

    public void setImageTagsCollection(HashMap<String, List<FileInfo>> imageTagsCollection) {
        this.imageTagsCollection = imageTagsCollection;
    }

    public List<FileInfo> getImageCollection() {
        return imageCollection;
    }

    public void setImageCollection(List<FileInfo> imageCollection) {
        this.imageCollection = imageCollection;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
