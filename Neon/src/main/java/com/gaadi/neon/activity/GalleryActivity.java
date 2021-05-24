package com.gaadi.neon.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.gaadi.neon.adapter.ImagesFoldersAdapter;
import com.gaadi.neon.util.FileInfo;
import com.gaadi.neon.util.NeonConstants;
import com.gaadi.neon.util.NeonUtils;
import com.gaadi.neon.util.PhotoParams;
import com.scanlibrary.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author lakshaygirdhar
 * @version 1.0
 * @since 13-08-12016
 *
 **/

public class GalleryActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final String MAX_COUNT = "maxCount";
    public static final String FOLDER_NAME = "folder_name";
    public static final String GALLERY_SELECTED_PHOTOS = "galleryPhotos";
    private static final int REQUEST_FOLDER_FILES = 100;
    private ArrayList<FileInfo> folders = new ArrayList<>();
    private PhotoParams mPhotoParams;
    private int maxCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_gallery, frameLayout);

        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.gallery));

        GridView gvFolders = (GridView) findViewById(R.id.gvFolders);

        if (getIntent().getSerializableExtra(NeonConstants.PHOTO_PARAMS) != null) {
            mPhotoParams = (PhotoParams) getIntent().getSerializableExtra(NeonConstants.PHOTO_PARAMS);
            maxCount = mPhotoParams.getNoOfPhotos();
        }
        //ImagesFoldersAdapter adapter = new ImagesFoldersAdapter(this, folders);
        ImagesFoldersAdapter adapter=null;
        gvFolders.setAdapter(adapter);

        Uri uri = NeonUtils.getImageStoreUri();

        String[] PROJECTION_BUCKET = {
                "" + MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA
        };
        Cursor mCursor ;
        if(mPhotoParams != null && mPhotoParams.isRestrictedExtensionEnabled()) {
            mCursor = getContentResolver().query(uri, PROJECTION_BUCKET,MediaStore.Images.Media.MIME_TYPE+" in (?, ?)", new String[] {"image/jpeg", "image/png"}, null);
        } else {
            mCursor = getContentResolver().query(uri, PROJECTION_BUCKET, "\"1) GROUP BY 1,(1\"", null, null);
        }
        if (mCursor == null) {
            Toast.makeText(GalleryActivity.this, getString(R.string.gallery_error), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mCursor.moveToFirst();


        HashMap<String, Integer> mapFolders = new HashMap<>();
        for (int i = 0; i < mCursor.getCount(); i++) {
            String bucketName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));

            Integer count = mapFolders.get(bucketName);
            if (count != null && count != 0) {
                mapFolders.put(bucketName, count + 1);
            } else {
                mapFolders.put(bucketName, 1);
            }
            mCursor.moveToNext();
        }
        mCursor.close();

        String selection = MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + "= ?";
        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC LIMIT 1";
        for (HashMap.Entry<String, Integer> entry : mapFolders.entrySet()) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setDisplayName(entry.getKey());
            fileInfo.setFileCount(entry.getValue());

            Cursor cursorImage = getContentResolver().query(uri, PROJECTION_BUCKET, selection, new String[]{fileInfo.getDisplayName()}, orderBy);
            if(cursorImage != null){
                cursorImage.moveToFirst();
                for (int j = 0; j < cursorImage.getCount(); j++) {
                    String imagePath = cursorImage.getString(cursorImage.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    fileInfo.setFilePath(imagePath);
                    cursorImage.moveToNext();
                }
                folders.add(fileInfo);
                cursorImage.close();
            }
        }

        adapter.notifyDataSetChanged();
        gvFolders.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        if (resultCode == GalleryFiles.RESULT_SKIP_FOLDERS) {
            intent.putExtra(NeonConstants.GALLERY_SELECTED_IMAGES, data.getExtras().getSerializable(GalleryFiles.SELECTED_FILES));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return false;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileInfo fileInfo = folders.get(position);
        Intent intent = new Intent(this, GalleryFiles.class);
        intent.putExtra(FOLDER_NAME, fileInfo.getDisplayName());
        if (maxCount > 0)
            intent.putExtra(MAX_COUNT, maxCount);
        startActivityForResult(intent, REQUEST_FOLDER_FILES);
    }
}
