package com.example.assignmentnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.example.assignmentnet.model.Photo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends Activity {
    ViewPager viewPager;
    int position;
    private ImageView imgFullSize;
    FullSizeAdapter fullSizeAdapter;
    private static final int PERMISSION_REQUEST_CODE = 1000;
    public FloatingActionButton fabAction1;
    public FloatingActionButton fabAction2;
    public FloatingActionButton fabAction3;
    PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        AndroidNetworking.initialize(getApplicationContext());
        fabAction1 = findViewById(R.id.fab_action1);
        fabAction2 = findViewById(R.id.fab_action2);
        fabAction3 = findViewById(R.id.fab_action3);
        viewPager = findViewById(R.id.viewPager);
        imgFullSize = findViewById(R.id.imgFullSize);

        Intent intent = getIntent();
        final ArrayList<Photo> list = (ArrayList<Photo>) intent.getSerializableExtra("LIST");
        position = intent.getIntExtra("POSITION", 0);

        fullSizeAdapter = new FullSizeAdapter(this, list);
        viewPager.setAdapter(fullSizeAdapter);
        viewPager.setCurrentItem(position, true);

        fabAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = list.get(viewPager.getCurrentItem());
                startDownload(photo.getUrlO());
            }
        });
        fabAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = list.get(viewPager.getCurrentItem());
                startDownload(photo.getUrlL());
            }

        });
        fabAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = list.get(viewPager.getCurrentItem());
                startDownload(photo.getUrlM());

            }
        });


    }

    public void startDownload(String url) {
        //Create download request
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                //Allow type of network to download files
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle("Download");
                request.setDescription("GalleryApp");
                request.allowScanningByMediaScanner();
                request.setMimeType("image/jpg");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //get current time for image file
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, System.currentTimeMillis() + ".jpg");

                //get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        } else {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            //Allow type of network to download files
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("Download");
            request.setDescription("GalleryApp");
            request.allowScanningByMediaScanner();
            request.setMimeType("image/jpg");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //get current time for image file
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, System.currentTimeMillis() + ".jpg");

            //get download service and enqueue file
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }


}
