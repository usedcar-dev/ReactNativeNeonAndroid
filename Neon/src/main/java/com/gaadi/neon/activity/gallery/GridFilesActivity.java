package com.gaadi.neon.activity.gallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;
import com.gaadi.neon.enumerations.CameraFacing;
import com.gaadi.neon.enumerations.CameraOrientation;
import com.gaadi.neon.enumerations.CameraType;
import com.gaadi.neon.PhotosLibrary;
import com.gaadi.neon.activity.ImageShow;
import com.gaadi.neon.adapter.GridFilesAdapter;
import com.gaadi.neon.enumerations.ResponseCode;
import com.gaadi.neon.interfaces.ICameraParam;
import com.gaadi.neon.interfaces.OnPermissionResultListener;
import com.gaadi.neon.model.ImageTagModel;
import com.gaadi.neon.model.PhotosMode;
import com.gaadi.neon.util.Constants;
import com.gaadi.neon.util.CustomParameters;
import com.gaadi.neon.util.FileInfo;
import com.gaadi.neon.util.ManifestPermission;
import com.gaadi.neon.util.NeonException;
import com.gaadi.neon.util.PermissionType;
import com.gaadi.neon.util.NeonImagesHandler;
import com.scanlibrary.R;

import java.util.ArrayList;
import java.util.List;

public class GridFilesActivity extends NeonBaseGalleryActivity {

    List<FileInfo> recentelyImageCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_grid_files, frameLayout);
        recentelyImageCollection = new ArrayList<>();
        bindXml();
        String title = getIntent().getStringExtra(Constants.BucketName);
        if (title == null || title.length() <= 0) {
            title = getString(R.string.gallery);
        }
        setTitle(title);
    }

    public void addImageToRecentelySelected(FileInfo fileInfo) {
        recentelyImageCollection.add(fileInfo);
    }

    public boolean removeImageFromRecentCollection(FileInfo fileInfo) {
        if (recentelyImageCollection == null || recentelyImageCollection.size() <= 0) {
            return true;
        }
        for (int i = 0; i < recentelyImageCollection.size(); i++) {
            if (recentelyImageCollection.get(i).getFilePath().equals(fileInfo.getFilePath())) {
                return recentelyImageCollection.remove(i) != null;
            }
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done_file, menu);
        MenuItem textViewDone = menu.findItem(R.id.menu_next);
        MenuItem menuItemCamera = menu.findItem(R.id.menuCamera);
        if(NeonImagesHandler.getSingleonInstance().getGalleryParam()!= null && NeonImagesHandler.getSingleonInstance().getGalleryParam().galleryToCameraSwitchEnabled()){
            menuItemCamera.setVisible(NeonImagesHandler.getSingleonInstance().getGalleryParam().galleryToCameraSwitchEnabled());
        } else {
            menuItemCamera.setVisible(false);
        }
        textViewDone.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.menu_next) {
            if (NeonImagesHandler.getSingleonInstance().getImagesCollection() == null ||
                    NeonImagesHandler.getSingleonInstance().getImagesCollection().size() <= 0) {
                Toast.makeText(this, R.string.no_image_selected, Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            } else {
                if (!NeonImagesHandler.getSingleonInstance().isNeutralEnabled()) {
                    if (NeonImagesHandler.getSingletonInstance().getGalleryParam().enableImageEditing()
                            || NeonImagesHandler.getSingletonInstance().getGalleryParam().getTagEnabled()) {
                        Intent intent = new Intent(this, ImageShow.class);
                        startActivity(intent);
                        setResult(Constants.destroyPreviousActivity);
                        finish();
                    } else {
                        if (NeonImagesHandler.getSingletonInstance().getGalleryParam().enableFolderStructure()) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            if (NeonImagesHandler.getSingletonInstance().validateNeonExit(this)) {
                                NeonImagesHandler.getSingletonInstance().sendImageCollectionAndFinish(this, ResponseCode.Success);
                                finish();
                            }
                        }
                    }
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        } else if (id == R.id.menuCamera) {
            performCameraOperation();
            setResult(Constants.destroyPreviousActivity);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (recentelyImageCollection != null && recentelyImageCollection.size() > 0) {
            new AlertDialog.Builder(this).setTitle("Are you sure want to loose all selected images?")
                    .setCancelable(true).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    for (int i = 0; i < recentelyImageCollection.size(); i++) {
                        NeonImagesHandler.getSingletonInstance().removeFromCollection(recentelyImageCollection.get(i));
                    }
                    finish();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
            return;
        }
        if (NeonImagesHandler.getSingleonInstance().isNeutralEnabled()) {
            super.onBackPressed();
        } else {
            if (!NeonImagesHandler.getSingleonInstance().getGalleryParam().enableFolderStructure()) {
                NeonImagesHandler.getSingleonInstance().showBackOperationAlertIfNeeded(this);
            } else {
                super.onBackPressed();
            }

        }
    }

    private void performCameraOperation() {

        ICameraParam cameraParam = NeonImagesHandler.getSingleonInstance().getCameraParam();
        if (cameraParam == null) {
            cameraParam = new ICameraParam() {
                @Override
                public CameraFacing getCameraFacing() {
                    return CameraFacing.front;
                }

                @Override
                public CameraOrientation getCameraOrientation() {
                    return CameraOrientation.portrait;
                }

                @Override
                public boolean getFlashEnabled() {
                    return true;
                }

                @Override
                public boolean getCameraSwitchingEnabled() {
                    return true;
                }

                @Override
                public boolean getVideoCaptureEnabled() {
                    return false;
                }

                @Override
                public CameraType getCameraViewType() {
                    return CameraType.normal_camera;
                }

                @Override
                public boolean cameraToGallerySwitchEnabled() {
                    return true;
                }

                @Override
                public int getNumberOfPhotos() {
                    return NeonImagesHandler.getSingleonInstance().getGalleryParam().getNumberOfPhotos();
                }

                @Override
                public boolean getTagEnabled() {
                    return NeonImagesHandler.getSingletonInstance().getGalleryParam().getTagEnabled();
                }

                @Override
                public List<ImageTagModel> getImageTagsModel() {
                    return NeonImagesHandler.getSingleonInstance().getGalleryParam().getImageTagsModel();
                }

                @Override
                public ArrayList<FileInfo> getAlreadyAddedImages() {
                    return null;
                }

                @Override
                public boolean enableImageEditing() {
                    return NeonImagesHandler.getSingletonInstance().getGalleryParam().enableImageEditing();
                }

                @Override
                public CustomParameters getCustomParameters() {
                    return NeonImagesHandler.getSingletonInstance().getGalleryParam().getCustomParameters();
                }

            };
        }
        try {
            PhotosLibrary.collectPhotos(NeonImagesHandler.getSingletonInstance().getRequestCode(),this, NeonImagesHandler.getSingletonInstance().getLibraryMode(), PhotosMode.setCameraMode().setParams(cameraParam), NeonImagesHandler.getSingleonInstance().getImageResultListener());
        } catch (NeonException e) {
            e.printStackTrace();
        }
    }

    private void bindXml() {
        try {
            PermissionType permissionType = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ? PermissionType.read_external_storage:PermissionType.write_external_storage;

            askForPermissionIfNeeded(permissionType, new OnPermissionResultListener() {
                @Override
                public void onResult(boolean permissionGranted) {
                    if (permissionGranted) {
                        GridFilesAdapter adapter = new GridFilesAdapter(GridFilesActivity.this, getFileFromBucketId(getIntent().getStringExtra(Constants.BucketId)));
                        GridView gvFolderPhotos = findViewById(R.id.gvFolderPhotos);
                        gvFolderPhotos.setAdapter(adapter);
                    } else {
                        if (NeonImagesHandler.getSingletonInstance().isNeutralEnabled()) {
                            finish();
                        } else {
                            NeonImagesHandler.getSingletonInstance().sendImageCollectionAndFinish(GridFilesActivity.this,
                                    ResponseCode.Write_Permission_Error);
                        }
                        Toast.makeText(GridFilesActivity.this, R.string.permission_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (ManifestPermission manifestPermission) {
            manifestPermission.printStackTrace();
        }
    }
}
