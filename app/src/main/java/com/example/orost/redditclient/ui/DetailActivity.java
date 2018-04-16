package com.example.orost.redditclient.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orost.redditclient.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Detail activity for displaying image
 * Created by o.rostovtseva on 15.04.2018.
 */

public class DetailActivity extends AppCompatActivity {
    public static final String KEY_EXTRA_URL = "item_url";
    public static final String KEY_EXTRA_TITLE = "item_title";
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    @BindView(R.id.imageTitle)
    TextView imageTitle;
    @BindView(R.id.image)
    ImageView image;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        title = getIntent().getExtras().getString(KEY_EXTRA_TITLE);
        imageTitle.setText(title);
        image.setDrawingCacheEnabled(true);
        String imageUrl = getIntent().getExtras().getString(KEY_EXTRA_URL);

        // There could be a video url
        // We don't handle this situation and show a blank thumb instead
        Picasso.with(this)
                .load(imageUrl)
                .error(R.drawable.ic_empty_picture)
                .fit().centerInside()
                .into(image);
    }

    /**
     * Save picture to a phone gallery and ask permissions
     */
    @OnClick(R.id.fabSave)
    public void savePictureToGallery() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        String savedUrl = savePictureExternalStorage();
        if (savedUrl != null) {
            Toast.makeText(this, getResources().getText(R.string.permission_storage_success),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePictureExternalStorage();
                    Toast.makeText(this,
                            getResources().getString(R.string.permission_storage_success),
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this,
                            getResources().getString(R.string.permission_storage_failure),
                            Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }

    private String savePictureExternalStorage() {
        Bitmap bitmap = image.getDrawingCache();
        return MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                title,
                ""
        );
    }
}
