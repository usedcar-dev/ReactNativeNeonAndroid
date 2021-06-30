package com.gaadi.neon.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.canhub.cropper.CropImage;
import com.gaadi.neon.adapter.ImageTagsAdapter;
import com.gaadi.neon.events.ImageEditEvent;
import com.gaadi.neon.interfaces.FragmentListener;
import com.gaadi.neon.model.ImageTagModel;
import com.gaadi.neon.util.Constants;
import com.gaadi.neon.util.FileInfo;
import com.gaadi.neon.util.NeonImagesHandler;
import com.gaadi.neon.util.NeonUtils;
import com.scanlibrary.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

/**
 * @author dipanshugarg
 * @version 1.0
 * @since 25/1/17
 */
public class ImageReviewViewPagerFragment extends Fragment implements View.OnClickListener
{

    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private ImageView deleteBtn;
    private ImageView rotateBtn;
    private TextView txtVwTagSpinner;
    private ImageView draweeView;
    private LinearLayout tagLayout;
    private FileInfo imageModel;
    List<ImageTagModel> tagModels;
    private Context mContext;
    private ImageView cropBtn;
    private File cropFilePath;
    private RelativeLayout fileEditLayout;

    public ImageReviewViewPagerFragment() {
    }

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ImageReviewViewPagerFragment create(int pageNumber, FileInfo imageModel) {
        ImageReviewViewPagerFragment fragment = new ImageReviewViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putSerializable(Constants.IMAGE_MODEL_FOR__REVIEW, imageModel);
        fragment.setArguments(args);
        return fragment;
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        if (NeonImagesHandler.getSingletonInstance() != null &&
                NeonImagesHandler.getSingletonInstance().getGenericParam() != null &&
                NeonImagesHandler.getSingletonInstance().getGenericParam().getImageTagsModel().size() > 0
                ) {
            tagModels = NeonImagesHandler.getSingletonInstance().getGenericParam().getImageTagsModel();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_image_review_viewpager, container, false);

        fileEditLayout = (RelativeLayout) rootView.findViewById(R.id.header_options_imageereview);



        deleteBtn = (ImageView) rootView.findViewById(R.id.imagereview_deletebtn);
        cropBtn = (ImageView) rootView.findViewById(R.id.imagereview_cropbtn);
        rotateBtn = (ImageView) rootView.findViewById(R.id.imagereview_rotatebtn);
        txtVwTagSpinner = (TextView) rootView.findViewById(R.id.imagereview_tag_spinner);
        draweeView = (ImageView) rootView.findViewById(R.id.imagereview_imageview);
        tagLayout = (LinearLayout) rootView.findViewById(R.id.footer_layout_imagereview_fragment);

        if (NeonImagesHandler.getSingleonInstance().getGenericParam() != null &&
                NeonImagesHandler.getSingleonInstance().getGenericParam().getTagEnabled()) {
            tagLayout.setVisibility(View.VISIBLE);
        } else {
            tagLayout.setVisibility(View.GONE);
        }

        deleteBtn.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        txtVwTagSpinner.setOnClickListener(this);
        onLoad(savedInstanceState);
        if (imageModel != null && imageModel.getFilePath() != null && (imageModel.getFilePath().contains("http") ||
                imageModel.getFilePath().contains("https"))) {
            fileEditLayout.setVisibility(View.INVISIBLE);
        } else {
            fileEditLayout.setVisibility(View.VISIBLE);
        }

        if(NeonImagesHandler.getSingletonInstance().getLivePhotosListener() != null)
        {
            fileEditLayout.setVisibility(View.GONE);
            tagLayout.setVisibility(View.GONE);
        }

        return rootView;
    }

    public void onLoad(Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        imageModel = (FileInfo) bundle.getSerializable(Constants.IMAGE_MODEL_FOR__REVIEW);
        if(savedInstanceState != null)
        {
            Object o = bundle.getSerializable(Constants.IMAGE_MODEL_FOR__REVIEW);
            if(o != null)
            {
                imageModel = (FileInfo) o;
            }
        }
        if(imageModel.getFileTag() != null)
        {
            txtVwTagSpinner.setText(imageModel.getFileTag().getTagName());
        }

        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.default_placeholder);
        if(imageModel.getFilePath().contains("content://") || imageModel.getFilePath().contains("file://"))
        {
            Glide.with(mContext).load(Uri.parse(imageModel.getFilePath())).apply(options).into(draweeView);
        }
        else
        {
            Glide.with(mContext).load(imageModel.getFilePath()).apply(options).into(draweeView);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.IMAGE_MODEL_FOR__REVIEW, imageModel);
        super.onSaveInstanceState(outState);
    }

    private void showTagsDropDown(View v) {
        final ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setModal(true);
        listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        ImageTagsAdapter imageTagsAdapter = new ImageTagsAdapter(getActivity(), imageModel);
        listPopupWindow.setAdapter(imageTagsAdapter);
        listPopupWindow.setAnchorView(v);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageTagModel singleModel = tagModels.get(position);
                if (singleModel.getNumberOfPhotos() > 0 && NeonImagesHandler.getSingleonInstance().getNumberOfPhotosCollected(singleModel) >= singleModel.getNumberOfPhotos()) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.max_tag_count_error, singleModel.getNumberOfPhotos()) + singleModel.getTagName(), Toast.LENGTH_SHORT).show();
                    return;
                }
                imageModel.setFileTag(new ImageTagModel(singleModel.getTagName(), singleModel.getTagId(), singleModel.isMandatory(), singleModel.getNumberOfPhotos()));
                ImageEditEvent event = new ImageEditEvent();
                event.setModel(imageModel);
                ((FragmentListener) getActivity()).getFragmentChanges(event);
                listPopupWindow.dismiss();
                txtVwTagSpinner.setText(singleModel.getTagName());
            }
        });

        txtVwTagSpinner.post(new Runnable() {
            @Override
            public void run() {
                listPopupWindow.show();
            }
        });
    }


    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    @Override
    public void onClick(View v) {
        ImageEditEvent event = new ImageEditEvent();
        event.setModel(imageModel);
        if(v.getId() == R.id.imagereview_deletebtn)
        {
            event.setImageEventType(ImageEditEvent.EVENT_DELETE);
            event.setPosition(mPageNumber);
            warnDeleteDialog(event);
        }
        else if(v.getId() == R.id.imagereview_rotatebtn)
        {
            if(imageModel.getSource() == FileInfo.SOURCE.PHONE_CAMERA && !imageModel.getFilePath()
                    .contains("content://") && !imageModel.getFilePath().contains("file://"))
            {
                rotateImage(imageModel.getFilePath());
            }
            else
            {
                String filePath = copyFileToInternalStorage(Uri.parse(imageModel.getFilePath()), "Evaluator");
                if(null != filePath)
                {
                    rotateImage(filePath);
                }
                else
                {
                    rotateImage(imageModel.getFilePath());
                }
            }
        }
        else if(v.getId() == R.id.imagereview_tag_spinner)
        {
            showTagsDropDown(v);
        } else if (v.getId() == R.id.imagereview_cropbtn) {
            try {
                cropFilePath = NeonUtils.getEmptyStoragePath(getActivity());
                Uri inputUri = Uri.fromFile(new File(imageModel.getFilePath()));
                Uri outputUri = Uri.fromFile(cropFilePath);
//                Uri inputUri = FileProvider.getUriForFile(getActivity(), NeonUtils.getFileProviderAuthority(getActivity()), new File(imageModel.getFilePath()));
//                Uri outputUri = FileProvider.getUriForFile(getActivity(), NeonUtils.getFileProviderAuthority(getActivity()), cropFilePath);
                Crop.of(inputUri, outputUri).start(getActivity(), ImageReviewViewPagerFragment.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void warnDeleteDialog(final ImageEditEvent event) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.remove_img_title);
        builder.setMessage(R.string.removeImage);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.okDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isAdded())
                    ((FragmentListener) getActivity()).getFragmentChanges(event);
            }
        });
        builder.setNegativeButton(R.string.cancelDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }


    @SuppressLint("NewApi")
    private Bitmap getBitmap(String path) {
        DisplayMetrics displaymetrics;
        displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        Log.e("inside of", "getBitmap = " + path);
        try {
            Bitmap b = null;
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            Matrix matrix = new Matrix();
            ExifInterface exifReader = new ExifInterface(path);
            int orientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            int rotate = 0;
            if (orientation == ExifInterface.ORIENTATION_NORMAL) {
                // Do nothing. The original image is fine.
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotate = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotate = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                rotate = 270;
            } else {
                Toast.makeText(getActivity(), "Not Able to rotate image due to missing orientation tag", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "Not Able to rotate image due to orientation tag=" + orientation);
                return null;
            }


            draweeView.setRotation(draweeView.getRotation() + 90.0f);
            ImageEditEvent event = new ImageEditEvent();
            imageModel.setFilePath(path);
            event.setModel(imageModel);
            event.setImageEventType(ImageEditEvent.EVENT_ROTATE);
            ((FragmentListener) getActivity()).getFragmentChanges(event);


            // matrix.postRotate(rotate);
            //Button btn_RotateImg = (Button) findViewById(R.id.btn_RotateImg);
           /* try {
                b = loadBitmap(path, rotate, screenWidth, screenHeight);

                //btn_RotateImg.setEnabled(true);
            } catch (OutOfMemoryError e) {
                // btn_RotateImg.setEnabled(false);
            }*/
            //System.gc();
            // return b;
        }
        catch(Exception e)
        {
            Log.e("my tag", e.getMessage(), e);
            // return null;
        }
        return null;
    }

    public void rotateImage(String path)
    {
        File file = new File(path);
        ExifInterface exifInterface = null;
        try
        {
            exifInterface = new ExifInterface((path.contains("content://") || path.contains("file://")) ? path : file.getPath());
        }
        catch(IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(null != exifInterface)
        {
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if((orientation == ExifInterface.ORIENTATION_NORMAL) | (orientation == 0))
            {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "" + ExifInterface.ORIENTATION_ROTATE_90);
            }
            else if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "" + ExifInterface.ORIENTATION_ROTATE_180);
            }
            else if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "" + ExifInterface.ORIENTATION_ROTATE_270);
            }
            else if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "" + ExifInterface.ORIENTATION_NORMAL);
            }
            try
            {
                exifInterface.saveAttributes();
            }
            catch(IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        getBitmap(path);
    }

    private String copyFileToInternalStorage(Uri uri, String newDirName)
    {
        Uri returnUri = uri;

        Cursor returnCursor = mContext.getContentResolver()
                .query(returnUri, new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE}, null, null, null);
        if(returnCursor == null)
        {
            return null;
        }

        try
        {
            if(returnCursor.moveToFirst())
            {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                String name = (returnCursor.getString(nameIndex));
                String size = (Long.toString(returnCursor.getLong(sizeIndex)));

                File output;
                if(!newDirName.equals(""))
                {
                    File dir = new File(mContext.getFilesDir() + "/" + newDirName);
                    if(!dir.exists())
                    {
                        dir.mkdir();
                    }
                    output = new File(mContext.getFilesDir() + "/" + newDirName + "/" + name);
                }
                else
                {
                    output = new File(mContext.getFilesDir() + "/" + name);
                }
                try
                {
                    InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
                    FileOutputStream outputStream = new FileOutputStream(output);
                    int read = 0;
                    int bufferSize = 1024;
                    final byte[] buffers = new byte[bufferSize];
                    while((read = inputStream.read(buffers)) != -1)
                    {
                        outputStream.write(buffers, 0, read);
                    }

                    inputStream.close();
                    outputStream.close();
                }
                catch(Exception e)
                {

                    Log.e("Exception", e.getMessage());
                }

                return output.getPath();
            }
        }
        finally
        {
            returnCursor.close();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK && null != result)
            {
                // String filePath = result.getUriFilePath(requireContext(), false);
                if(imageModel.getSource() == FileInfo.SOURCE.PHONE_CAMERA || (null != result.getUriContent() && result.getUriContent()
                        .toString()
                        .contains("file://")))
                {
                    imageModel.setFilePath(result.getUriFilePath(requireContext(), true));
                }
                else
                {
                    imageModel.setFilePath(result.getUriContent().toString());
                }
                Uri uri = result.getUriContent();
                RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(R.drawable.default_placeholder);
                Glide.with(this).load(uri).apply(options).into(draweeView);
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE && null != result)
            {
                Exception error = result.getError();
                Log.e("Exception", error.getMessage());
            }
        }
    }

}
