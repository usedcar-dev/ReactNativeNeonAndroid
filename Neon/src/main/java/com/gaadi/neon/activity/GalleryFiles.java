package com.gaadi.neon.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.gaadi.neon.adapter.SelectFilesAdapter;
import com.gaadi.neon.interfaces.UpdateSelection;
import com.gaadi.neon.util.ApplicationController;
import com.gaadi.neon.util.FileInfo;
import com.gaadi.neon.util.NeonUtils;
import com.scanlibrary.R;

import java.util.ArrayList;

/**
 * Created by Lakshay
 * @since 02-03-2015.
 *
 */
public class GalleryFiles extends BaseActivity implements UpdateSelection, LoaderManager.LoaderCallbacks {

    public static final int RESULT_SKIP_FOLDERS = 10;
    public static final String SELECTED_FILES = "selectedFiles";
    private SelectFilesAdapter adapter;
    private ArrayList<FileInfo> folderSelectedFiles;
    private ArrayList<FileInfo> deletedFiles, addedFiles;
    private GridView folderFiles;
    private MenuItem textViewDone;
    private Cursor mCursor;

    private String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =? and " + MediaStore.Images.Media.SIZE + " >?";
    private Uri mUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private String[] mProjection = {
            "_data",
            "_id",
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };
    private String[] selectionArgs;
    private String order = MediaStore.Images.Media.DATE_TAKEN + " DESC";
    private int maxCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.folder_files, frameLayout);

        folderSelectedFiles = new ArrayList<>();
        deletedFiles = new ArrayList<>();
        addedFiles = new ArrayList<>();

        String folderName = getIntent().getStringExtra(GalleryActivity.FOLDER_NAME);
        if (getIntent().getExtras().containsKey(GalleryActivity.MAX_COUNT))
            maxCount = getIntent().getExtras().getInt(GalleryActivity.MAX_COUNT);
        if (maxCount > 0) {
            if (ApplicationController.selectedFiles != null) {
                ApplicationController.selectedFiles.clear();
            }
        }
        selectionArgs = new String[]{folderName, String.valueOf(0)};

        if (folderName != null && folderName.equals(".thumbnails")) {
            mUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
            selection = null;
            selectionArgs = null;
            mProjection = null;
            order = MediaStore.Images.Thumbnails.IMAGE_ID + " DESC";
        }

        setTitleMsg(folderName);

        mCursor = getContentResolver().query(mUri, mProjection, selection, selectionArgs, order);
        if(mCursor != null)
            mCursor.moveToFirst();

        adapter = new SelectFilesAdapter(this, mCursor, 0, this);
        folderFiles = (GridView) findViewById(R.id.gvFolderPhotos);
        folderFiles.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(mCursor != null)
            mCursor.close();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            if (folderSelectedFiles != null && folderSelectedFiles.size() > 0) {
                folderSelectedFiles = null;
            }
            finish();
        } else if (id == R.id.menu_next) {
            folderSelectedFiles.addAll(addedFiles);
            NeonUtils.removeFileInfo(folderSelectedFiles, deletedFiles);
            if (maxCount == 0) {
                if (ApplicationController.selectedFiles == null)
                    ApplicationController.selectedFiles = new ArrayList<>();
                addFilesToSelected(ApplicationController.selectedFiles, addedFiles);
                removeFilesFromSelected(ApplicationController.selectedFiles, deletedFiles);
                NeonUtils.removeFileInfo(ApplicationController.selectedFiles, deletedFiles, false);
            }
            Intent intent = new Intent();
            intent.putExtra(GalleryFiles.SELECTED_FILES, folderSelectedFiles);
            setResult(RESULT_SKIP_FOLDERS, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateSelected(String imagePath, Boolean selected) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilePath(imagePath);
        if (selected) {
            textViewDone.setVisible(true);
            NeonUtils.addFileInfo(addedFiles, fileInfo);
            NeonUtils.removeFileInfo(deletedFiles, fileInfo);
        } else {
            SelectFilesAdapter adapter = (SelectFilesAdapter) folderFiles.getAdapter();
            if (adapter.selectedArr.size() == 0) {
                textViewDone.setVisible(true);
            }
            NeonUtils.addFileInfo(deletedFiles, fileInfo);
        }
        if (maxCount > 0)
            adapter.setStopSelection((addedFiles.size() - deletedFiles.size()) == maxCount);
    }

    public void addFilesToSelected(ArrayList<String> list, ArrayList<FileInfo> list1) {
        if (list1 != null) {
            for (int i = 0; i < list1.size(); i++) {
                list.add(list1.get(i).getFilePath());
            }
        }
    }

    public void removeFilesFromSelected(ArrayList<String> list, ArrayList<FileInfo> list1) {
        if (list1 != null && list != null) {
            for (int i = 0; i < list1.size(); i++) {
                list.remove(list1.get(i).getFilePath());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done_file, menu);
        textViewDone = menu.findItem(R.id.menu_next);
        textViewDone.setVisible(!ApplicationController.selectedFiles.isEmpty());
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, mUri, mProjection, selection, selectionArgs, order);
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
