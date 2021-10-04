package com.gaadi.neon.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.scanlibrary.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Lakshay
 *
 * @since 13-02-2015.
 */
public class Constants {
    public static final String Gallery_Params = "Gallery_Params";
    public static final String Camera_Params = "Camera_Params";
    public static final String BucketName = "BucketName";
    public static final String BucketId = "BucketId";

    public static final int destroyPreviousActivity = 300;

    public static final int TYPE_IMAGE = 1;
    public static final String APP_SHARED_PREFERENCE = "com.gcloud.gaadi.prefs";
    public static final String TAG = "Gallery";
    public static final String RESULT_IMAGES = "result_images";

    public static final String SINGLE_IMAGE_PATH = "singleImagePath";
    public static final String REVIEW_TITLE = "reviewTitle";

    public static final String IMAGES_SELECTED = "imagesSelected";
    public static final String IMAGE_PATH = "image_path";
    public static final int REQUEST_PERMISSION_CAMERA = 104;
    public static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 105;
    public static final String FLASH_MODE = "flashMode";
    public static final String IMAGE_TAGS_FOR_REVIEW = "imageTagsReview";
    public static final String IMAGE_MODEL_FOR__REVIEW = "imageModelReview";
    public static final String IMAGE_REVIEW_POSITION = "imageReviewPosition";
    public static final String SINGLE_TAG_SELECTION = "singleTagSelection";
    public static final String ALREADY_SELECTED_TAGS = "alreadySelectedTags";
    public static String FLAG = "Flag";
    public static final String CATEGORY = "category";
    public static final String SUB_CATEGORY = "subCategory";
    public static final String CAM_SCANNER_API_KEY = "camScannerApiKey";

    public static File getMediaOutputFileNew(Context context, int type, String folderName)
    {
        String appName = context.getString(R.string.app_name);
        if(appName.length() > 0)
        {
            appName = appName.replace(" ", "");
        }

        ContextWrapper cw = new ContextWrapper(context);
        File mediaStorageDir = new File(cw.getExternalFilesDir(Environment.DIRECTORY_PICTURES), appName);

        // Create the storage directory if it does not exist
        if(!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(new Date());
        File mediaFile;

        if(type == TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir, "IMG_" + System.currentTimeMillis() + ".jpg");
        }
        else
        {
            return null;
        }
        return mediaFile;
    }

    public static File getMediaOutputFile(Context context, int type, String folderName)
    {
        String appName = context.getString(R.string.app_name);
        if(appName.length() > 0)
        {
            appName = appName.replace(" ", "");
        }
        String path;
        if(folderName != null)
        {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + appName + File.separator + folderName;
        }
        else
        {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + appName;
        }

        File mediaStorageDir = new File(path);

        // Create the storage directory if it does not exist
        if(!mediaStorageDir.exists())
        {
            if(!mediaStorageDir.mkdirs())
            {
                Log.d("MyCameraApp", "failed to create directory");
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(new Date());
        File mediaFile;

        if(type == TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg");
        }
        else
        {
            return null;
        }
        return mediaFile;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static OutputStream getMediaOutputStream(Context context, int type, String folderName)
    {
        OutputStream fos = null;
        File imageFile = null;
        Uri imageUri = null;
        try
        {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_" + System.currentTimeMillis());
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + folderName);
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            if(imageUri == null)
            {
                throw new IOException("Failed to create new MediaStore record.");
            }

            fos = resolver.openOutputStream(imageUri);
            return fos;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return fos;
    }

    public static String getAppName(Context context)
    {
        String appName = context.getString(R.string.app_name);
        if(appName.length() > 0)
        {
            appName = appName.replace(" ", "");
        }
        return appName;
    }
}
