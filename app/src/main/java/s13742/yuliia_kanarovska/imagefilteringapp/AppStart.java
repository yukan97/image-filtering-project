package s13742.yuliia_kanarovska.imagefilteringapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import s13742.yuliia_kanarovska.imagefilteringapp.utils.BitmapUtils;
import s13742.yuliia_kanarovska.imagefilteringapp.filters.*;
import s13742.yuliia_kanarovska.imagefilteringapp.utils.Filter;
import s13742.yuliia_kanarovska.imagefilteringapp.utils.FiltersAdapter;
import s13742.yuliia_kanarovska.imagefilteringapp.utils.ThumbnailModel;

public class AppStart extends AppCompatActivity implements FiltersAdapter.FiltersAdapterOnClickHandler, SeekBar.OnSeekBarChangeListener{
    public static final int GALLERY = 101;
    Bitmap originalImg;
    Bitmap filteredImg;
    Bitmap finalImg;
    ImageView imageView;

    RecyclerView myRecyclerView;
    FiltersAdapter mAdapter;
    List<ThumbnailModel> thumbnail_models_list;
    LinearLayout editTab;
    SeekBar red_controller, green_controller, blue_controller;
    Button edit, filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);

        imageView = (ImageView) findViewById(R.id.imageView);
        myRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        thumbnail_models_list = new ArrayList<>();
        mAdapter = new FiltersAdapter(thumbnail_models_list, this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());
        //myRecyclerView.addItemDecoration(new SpacesItemDecoration(space));
        myRecyclerView.setAdapter(mAdapter);

        editTab = (LinearLayout) findViewById(R.id.seekbar_layout);
        red_controller = findViewById(R.id.red_controller);
        red_controller.setMax(200);
        red_controller.setProgress(100);
        red_controller.setOnSeekBarChangeListener(this);

        green_controller = findViewById(R.id.green_controller);
        green_controller.setMax(200);
        green_controller.setProgress(100);
        green_controller.setOnSeekBarChangeListener(this);

        blue_controller = findViewById(R.id.blue_controller);
        blue_controller.setMax(200);
        blue_controller.setProgress(100);
        blue_controller.setOnSeekBarChangeListener(this);

        edit = findViewById(R.id.button_color_balance);
        filter = findViewById(R.id.button_filters);

        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showEditTab();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showFilterTab();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.open_btn) {
            openImageFromGallery();
            return true;
        }

        if (id == R.id.save_btn) {
            saveImageToGallery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openImageFromGallery(){

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }
//    private void takePhotoFromCamera() {
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA);
//    }
    private void saveImageToGallery(){
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY) {
            if (data != null){
                Uri contentURI = data.getData();
                try {

                    mAdapter = new FiltersAdapter(thumbnail_models_list, this);
                    myRecyclerView.setAdapter(mAdapter);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    originalImg.recycle();
//                    filteredImg.recycle();
//                    finalImg.recycle();
                    //String path = saveImage(bitmap);
                    originalImg = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    filteredImg = originalImg.copy(Bitmap.Config.ARGB_8888, true);
                    finalImg = originalImg.copy(Bitmap.Config.ARGB_8888, true);
                    imageView.setImageBitmap(filteredImg);
                    thumbnail_models_list.clear();
                    setThumbnails(originalImg);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AppStart.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    @Override
    public void onClick(Filter filter) {
        //filteredImg = filter.doFilter(originalImg);
            Log.i("MAIN IF-ELSE ", "started");
          if(filter instanceof Blur){

              filteredImg = Blur.applyBlurRunnable(originalImg);

          } else if (filter instanceof Sharpen){

              filteredImg = Sharpen.applySharpenRunnable(originalImg);

          } else if (filter instanceof  Brightness){

              BrightnessRunnable k1 = new BrightnessRunnable(originalImg, 0, 0, originalImg.getWidth()/2, originalImg.getHeight()/2);
              BrightnessRunnable k2 = new BrightnessRunnable(originalImg, originalImg.getWidth()/2, 0, originalImg.getWidth(), originalImg.getHeight()/2);
              BrightnessRunnable k3 = new BrightnessRunnable(originalImg, 0, originalImg.getHeight()/2, originalImg.getWidth()/2, originalImg.getHeight());
              BrightnessRunnable k4 = new BrightnessRunnable(originalImg, originalImg.getWidth()/2, originalImg.getHeight()/2, originalImg.getWidth(), originalImg.getHeight());

              ExecutorService es = Executors.newCachedThreadPool();
              es.execute(new Thread(k1));
              es.execute(new Thread(k2));
              es.execute(new Thread(k3));
              es.execute(new Thread(k4));

              es.shutdown();

              try {
                  es.awaitTermination(5, TimeUnit.MINUTES);
              } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }

//              for (int i = 0; i < originalImg.getWidth(); i++){
//                  for (int j = 0; j < originalImg.getHeight(); j++){
//                      if(i <= originalImg.getWidth()/2 && j <= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k1.getResultingImg().getPixel(i, j));
//                      } else if(i >= originalImg.getWidth()/2 && j <= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k2.getResultingImg().getPixel(i, j));
//                      } else if(i <= originalImg.getWidth()/2 && j >= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k3.getResultingImg().getPixel(i, j));
//                      } else if(i >= originalImg.getWidth()/2 && j >= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k4.getResultingImg().getPixel(i, j));
//                      }
//                  }
//              }
              int[] pixels = new int[originalImg.getWidth() * originalImg.getHeight()];
              Bitmap bitmap = k1.getResultingImg();
              bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k2.getResultingImg();
              bitmap.getPixels(pixels, bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getWidth()/2, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k3.getResultingImg();
              bitmap.getPixels(pixels, pixels.length/2, bitmap.getWidth(), 0, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k4.getResultingImg();
              bitmap.getPixels(pixels, pixels.length/2 + bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getHeight()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
              filteredImg.setPixels(pixels, 0, filteredImg.getWidth(), 0, 0, filteredImg.getWidth(), filteredImg.getHeight());

          } else if (filter instanceof Contrast) {

              ContrastRunnable k1 = new ContrastRunnable(originalImg, 0, 0, originalImg.getWidth()/2, originalImg.getHeight()/2);
              ContrastRunnable k2 = new ContrastRunnable(originalImg, originalImg.getWidth()/2, 0, originalImg.getWidth(), originalImg.getHeight()/2);
              ContrastRunnable k3 = new ContrastRunnable(originalImg, 0, originalImg.getHeight()/2, originalImg.getWidth()/2, originalImg.getHeight());
              ContrastRunnable k4 = new ContrastRunnable(originalImg, originalImg.getWidth()/2, originalImg.getHeight()/2, originalImg.getWidth(), originalImg.getHeight());

              ExecutorService es = Executors.newCachedThreadPool();
              es.execute(new Thread(k1));
              es.execute(new Thread(k2));
              es.execute(new Thread(k3));
              es.execute(new Thread(k4));

              es.shutdown();

              try {
                  es.awaitTermination(5, TimeUnit.MINUTES);
              } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }

//              for (int i = 0; i < originalImg.getWidth(); i++){
//                  for (int j = 0; j < originalImg.getHeight(); j++){
//                  if(i <= originalImg.getWidth()/2 && j <= originalImg.getHeight()/2){
//                      filteredImg.setPixel(i, j, k1.getResultingImg().getPixel(i, j));
//                  } else if(i >= originalImg.getWidth()/2 && j <= originalImg.getHeight()/2){
//                      filteredImg.setPixel(i, j, k2.getResultingImg().getPixel(i, j));
//                  } else if(i <= originalImg.getWidth()/2 && j >= originalImg.getHeight()/2){
//                      filteredImg.setPixel(i, j, k3.getResultingImg().getPixel(i, j));
//                  } else if(i >= originalImg.getWidth()/2 && j >= originalImg.getHeight()/2){
//                      filteredImg.setPixel(i, j, k4.getResultingImg().getPixel(i, j));
//                    }
//                  }
//              }
              int[] pixels = new int[originalImg.getWidth() * originalImg.getHeight()];
              Bitmap bitmap = k1.getResultingImg();
              bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k2.getResultingImg();
              bitmap.getPixels(pixels, bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getWidth()/2, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k3.getResultingImg();
              bitmap.getPixels(pixels, pixels.length/2, bitmap.getWidth(), 0, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k4.getResultingImg();
              bitmap.getPixels(pixels, pixels.length/2 + bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getHeight()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
              filteredImg.setPixels(pixels, 0, filteredImg.getWidth(), 0, 0, filteredImg.getWidth(), filteredImg.getHeight());

          } else if (filter instanceof  Grayscale){

              GrayscaleRunnable k1 = new GrayscaleRunnable(originalImg, 0, 0, originalImg.getWidth()/2, originalImg.getHeight()/2);
              GrayscaleRunnable k2 = new GrayscaleRunnable(originalImg, originalImg.getWidth()/2, 0, originalImg.getWidth(), originalImg.getHeight()/2);
              GrayscaleRunnable k3 = new GrayscaleRunnable(originalImg, 0, originalImg.getHeight()/2, originalImg.getWidth()/2, originalImg.getHeight());
              GrayscaleRunnable k4 = new GrayscaleRunnable(originalImg, originalImg.getWidth()/2, originalImg.getHeight()/2, originalImg.getWidth(), originalImg.getHeight());

              ExecutorService es = Executors.newCachedThreadPool();
              es.execute(new Thread(k1));
              es.execute(new Thread(k2));
              es.execute(new Thread(k3));
              es.execute(new Thread(k4));

              es.shutdown();

              try {
                  es.awaitTermination(5, TimeUnit.MINUTES);
              } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }

//              for (int i = 0; i < originalImg.getWidth(); i++){
//                  for (int j = 0; j < originalImg.getHeight(); j++){
//                      if(i <= originalImg.getWidth()/2 && j <= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k1.getResultingImg().getPixel(i, j));
//                      } else if(i >= originalImg.getWidth()/2 && j <= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k2.getResultingImg().getPixel(i, j));
//                      } else if(i <= originalImg.getWidth()/2 && j >= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k3.getResultingImg().getPixel(i, j));
//                      } else if(i >= originalImg.getWidth()/2 && j >= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k4.getResultingImg().getPixel(i, j));
//                      }
//                  }
//              }
              int[] pixels = new int[originalImg.getWidth() * originalImg.getHeight()];
              Bitmap bitmap = k1.getResultingImg();
              bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k2.getResultingImg();
              bitmap.getPixels(pixels, bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getWidth()/2, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k3.getResultingImg();
              bitmap.getPixels(pixels, pixels.length/2, bitmap.getWidth(), 0, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k4.getResultingImg();
              bitmap.getPixels(pixels, pixels.length/2 + bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getHeight()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
              filteredImg.setPixels(pixels, 0, filteredImg.getWidth(), 0, 0, filteredImg.getWidth(), filteredImg.getHeight());

          } else if (filter instanceof Sepia){

              SepiaRunnable k1 = new SepiaRunnable(originalImg, 0, 0, originalImg.getWidth()/2, originalImg.getHeight()/2);
              SepiaRunnable k2 = new SepiaRunnable(originalImg, originalImg.getWidth()/2, 0, originalImg.getWidth(), originalImg.getHeight()/2);
              SepiaRunnable k3 = new SepiaRunnable(originalImg, 0, originalImg.getHeight()/2, originalImg.getWidth()/2, originalImg.getHeight());
              SepiaRunnable k4 = new SepiaRunnable(originalImg, originalImg.getWidth()/2, originalImg.getHeight()/2, originalImg.getWidth(), originalImg.getHeight());

              ExecutorService es = Executors.newCachedThreadPool();
              es.execute(new Thread(k1));
              es.execute(new Thread(k2));
              es.execute(new Thread(k3));
              es.execute(new Thread(k4));

              es.shutdown();

              try {
                  es.awaitTermination(5, TimeUnit.MINUTES);
              } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }

              Log.i("MAIN SEPIA ", "loop");
//              for (int i = 0; i < originalImg.getWidth(); i++){
//                  for (int j = 0; j < originalImg.getHeight(); j++){
//                      if(i <= originalImg.getWidth()/2 && j <= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k1.getResultingImg().getPixel(i, j));
//                      } else if(i >= originalImg.getWidth()/2 && j <= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k2.getResultingImg().getPixel(i, j));
//                      } else if(i <= originalImg.getWidth()/2 && j >= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k3.getResultingImg().getPixel(i, j));
//                      } else if(i >= originalImg.getWidth()/2 && j >= originalImg.getHeight()/2){
//                          filteredImg.setPixel(i, j, k4.getResultingImg().getPixel(i, j));
//                      }
//                  }
//              }

              int[] pixels = new int[originalImg.getWidth() * originalImg.getHeight()];
              Bitmap bitmap = k1.getResultingImg();
              bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k2.getResultingImg();
              bitmap.getPixels(pixels, bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getWidth()/2, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k3.getResultingImg();
              bitmap.getPixels(pixels, pixels.length/2, bitmap.getWidth(), 0, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
              bitmap = k4.getResultingImg();
              bitmap.getPixels(pixels, pixels.length/2 + bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getHeight()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
              filteredImg.setPixels(pixels, 0, filteredImg.getWidth(), 0, 0, filteredImg.getWidth(), filteredImg.getHeight());


              Log.i("MAIN SEPIA ", "loop finished");
          }
//
        //filteredImg = Blur.applyBlurRunnable(originalImg);

        imageView.setImageBitmap(filteredImg);
    }

    public void setThumbnails(Bitmap src){

        Bitmap thumbnail = BitmapUtils.createThumbnail(src);

        thumbnail_models_list.add(new ThumbnailModel(thumbnail, new Grayscale()));
        thumbnail_models_list.add(new ThumbnailModel(thumbnail, new Sepia()));
        thumbnail_models_list.add(new ThumbnailModel(thumbnail, new Blur()));
        thumbnail_models_list.add(new ThumbnailModel(thumbnail, new Sharpen()));
        thumbnail_models_list.add(new ThumbnailModel(thumbnail, new Brightness()));
        thumbnail_models_list.add(new ThumbnailModel(thumbnail, new Contrast()));
        thumbnail_models_list.add(new ThumbnailModel(thumbnail, new CustomFilter()));

    }

    public void showFilterTab(){
        myRecyclerView.setVisibility(View.VISIBLE);
        editTab.setVisibility(View.INVISIBLE);
    }
    public void showEditTab(){
        myRecyclerView.setVisibility(View.INVISIBLE);
        editTab.setVisibility(View.VISIBLE);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        ColorCorrectionRunnable k1 = new ColorCorrectionRunnable(originalImg, 0, 0, originalImg.getWidth()/2, originalImg.getHeight()/2, (red_controller.getProgress() - 100), green_controller.getProgress() -100, blue_controller.getProgress()-100);
        ColorCorrectionRunnable k2 = new ColorCorrectionRunnable(originalImg, originalImg.getWidth()/2, 0, originalImg.getWidth(), originalImg.getHeight()/2, (red_controller.getProgress() - 100), green_controller.getProgress() -100, blue_controller.getProgress()-100);
        ColorCorrectionRunnable k3 = new ColorCorrectionRunnable(originalImg, 0, originalImg.getHeight()/2, originalImg.getWidth()/2, originalImg.getHeight(), (red_controller.getProgress() - 100), green_controller.getProgress() -100, blue_controller.getProgress()-100);
        ColorCorrectionRunnable k4 = new ColorCorrectionRunnable(originalImg, originalImg.getWidth()/2, originalImg.getHeight()/2, originalImg.getWidth(), originalImg.getHeight(), (red_controller.getProgress() - 100), green_controller.getProgress() -100, blue_controller.getProgress()-100);

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new Thread(k1));
        es.execute(new Thread(k2));
        es.execute(new Thread(k3));
        es.execute(new Thread(k4));

        es.shutdown();

        try {
            es.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i("progress ", progress + "");
        int[] pixels = new int[originalImg.getWidth() * originalImg.getHeight()];
        Bitmap bitmap = k1.getResultingImg();
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
        bitmap = k2.getResultingImg();
        bitmap.getPixels(pixels, bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getWidth()/2, 0, bitmap.getWidth()/2, bitmap.getHeight()/2);
        bitmap = k3.getResultingImg();
        bitmap.getPixels(pixels, pixels.length/2, bitmap.getWidth(), 0, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
        bitmap = k4.getResultingImg();
        bitmap.getPixels(pixels, pixels.length/2 + bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getHeight()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2);
        filteredImg.setPixels(pixels, 0, filteredImg.getWidth(), 0, 0, filteredImg.getWidth(), filteredImg.getHeight());

        //filteredImg = ColorCorrection.doColorCorrection(originalImg,(red_controller.getProgress() - 100), green_controller.getProgress() -100, blue_controller.getProgress()-100);
        imageView.setImageBitmap(filteredImg);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
