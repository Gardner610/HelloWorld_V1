package com.example.tomas.helloworld_v1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * My Image Downloader Task class
 */

class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    //do nothing for now
                    //Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                    //imageView.setImageDrawable(placeholder);
                }
            }
        }
    }


    private Bitmap downloadBitmap(String url) {

        // should manage correct bitmap size for different devices
        BitmapFactory.Options options = new BitmapFactory.Options();
        // This defines the density that the image is designed for.
        options.inDensity = DisplayMetrics.DENSITY_MEDIUM;

        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setUseCaches(true); //auto cache image ..I wish :D

            int statusCode = urlConnection.getResponseCode();
/*
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
*/
            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream != null) {
                //Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //return bitmap;
                return BitmapFactory.decodeStream(inputStream, null, options);
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
