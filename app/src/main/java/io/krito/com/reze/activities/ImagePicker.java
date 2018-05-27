package io.krito.com.reze.activities;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import io.krito.com.reze.R;
import io.krito.com.reze.helper.GalleryPhotoAlbum;

//MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

public class ImagePicker extends AppCompatActivity {

    ArrayList<GalleryPhotoAlbum> albums;
    Cursor mPhotoCursor;
    Cursor mVideoCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
    }

    private void getMediaList(Boolean isImage) {
        String[] PROJECTION_BUCKET = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA
        };

        String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        Uri uri = null;
        if (isImage) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor cur = getContentResolver().query(uri, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);
        GalleryPhotoAlbum album;

        if (cur.moveToFirst()) {
            String bucket;
            String date;
            String data;
            long bucketId;

            int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            int bucketIdColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

            do {
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                data = cur.getString(dataColumn);
                bucketId = cur.getInt(bucketIdColumn);

                if (bucket != null && bucket.length() > 0) {
                    album = new GalleryPhotoAlbum();
                    album.setBucketId(cur.getInt(bucketIdColumn));
                    album.setBucketName(bucket);
                    album.setDateTaken(date);
                    album.setData(data);

                    if (isImage)
                        album.setTotalCount(photoCountByAlbum(bucket));
                    else
                        album.setTotalCount(videoCountByAlbum(bucket));

                    albums.add(album);

                    // Do something with the values.

                    Log.v("ListingImages", " bucket=" + bucket
                            + "  date_taken=" + date + "  _data=" + data
                            + " bucket_id=" + bucketId); }

            } while (cur.moveToNext());
        }

        cur.close();
    }

    private int photoCountByAlbum(String bucketName) {
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";
            // final String[] columns = { MediaStore.Images.Media.DATA,

            // MediaStore.Images.Media._ID };

            Cursor mPhotoCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    searchParams,
                    null,
                    orderBy + " DESC");
            if (mPhotoCursor.getCount() > 0) {
                return mPhotoCursor.getCount();
            }
            mPhotoCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int videoCountByAlbum(String bucketName) {
        try {
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";
            // final String[] columns = { MediaStore.Video.Media.DATA,

            // MediaStore.Video.Media._ID };

            Cursor mVideosCursor = getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null,
                    searchParams,
                    null,
                    orderBy + " DESC");

            if (mVideosCursor.getCount() > 0) {
                return mVideosCursor.getCount();
            }
            mVideosCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void initPhotoImages(String bucketName) {
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = null;
            searchParams = "bucket_display_name = \"" + bucketName + "\"";
            // final String[] columns = { MediaStore.Images.Media.DATA,
            // MediaStore.Images.Media._ID };

            mPhotoCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    searchParams,
                    null,
                    orderBy + " DESC");
//            if (mPhotoCursor.getCount() > 0) {
//                cursorData = new ArrayList<MediaObject>();
//                cursorData.addAll(Utils.extractMediaList(mPhotoCursor,
//                        MediaType.PHOTO));
//            }
//            // setAdapter(mImageCursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
