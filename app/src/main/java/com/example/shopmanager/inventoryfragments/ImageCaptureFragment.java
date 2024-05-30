package com.example.shopmanager.inventoryfragments;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopmanager.R;
import com.google.android.material.button.MaterialButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class ImageCaptureFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    PreviewView cameraPreview;
    MaterialButton clickBtn;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    public ImageCaptureFragment() {
        // Required empty public constructor
    }


    public static ImageCaptureFragment newInstance() {
        ImageCaptureFragment fragment = new ImageCaptureFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_image_capture, container, false);
       cameraPreview = v.findViewById(R.id.camera_preview);
       clickBtn = v.findViewById(R.id.click_picture);



        Dexter.withContext(getActivity())
                .withPermissions(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {

                            startCameraX();
                        } else {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();


    return v;
    }

    private void startCameraX() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());



        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {

            }
        }, ContextCompat.getMainExecutor(getActivity()));




    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

        ImageCapture imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(cameraPreview.getDisplay().getRotation())
                        .build();



        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,imageCapture, preview);

        clickBtn.setOnClickListener(view -> {

//            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            File outputFile = new File(storageDir, "ShopManagerImages/image.jpg");

            File outputDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ShopManagerImages");
            if (!outputDirectory.exists()) {
                Log.d("dir","doesn't exist");
                outputDirectory.mkdirs(); // Create the directory if it doesn't exist
            }
            Log.d("dir"," exist");
            String fileName = System.currentTimeMillis() + ".jpg";

            File outputFile = new File(outputDirectory, fileName);
            Log.d("outputfile",outputFile.getAbsolutePath());

            ImageCapture.OutputFileOptions outputFileOptions =
                    new ImageCapture.OutputFileOptions.Builder(outputFile).build();



            long timeStamp = System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");



            imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(
                            getActivity().getContentResolver(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            contentValues
                    ).build(), Executors.newSingleThreadExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                            //Toast.makeText(getActivity(),"Image added",Toast.LENGTH_LONG).show();
                            Log.d("imageAdded",outputFileResults.getSavedUri().toString());
                            //Toast.makeText(getActivity(), "Image saved", Toast.LENGTH_LONG).show();
                            Log.d("ImageSaved", outputFile.getAbsolutePath());
                            Bundle bundle = new Bundle();
                            bundle.putString("ImageURI",outputFileResults.getSavedUri().toString());
                            getParentFragmentManager().setFragmentResult("imagerequestkey",bundle);
                            getParentFragmentManager().popBackStack("add_item_frag_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        }
                        @Override
                        public void onError(ImageCaptureException error) {
                            Log.d("errorImage",error.getLocalizedMessage());
                            Log.d("errorImage",String.valueOf(error.getImageCaptureError()));
                            Log.d("errorImage",error.getMessage());

                            //Toast.makeText(getActivity(),"Error adding image",Toast.LENGTH_LONG).show();
                        }
                    }
            );

        });

    }
}