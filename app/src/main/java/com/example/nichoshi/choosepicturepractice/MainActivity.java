package com.example.nichoshi.choosepicturepractice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 0;
    public static final int CROP_PHOTO =1;
    private Button takeBtn;
    private Button chooseBtn;
    private ImageView photoImageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takeBtn = (Button) findViewById(R.id.takeBtn);
        chooseBtn = (Button) findViewById(R.id.chooseBtn);
        photoImageView = (ImageView) findViewById(R.id.photoImageView);

        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File OutputImage = new File(Environment.getExternalStorageDirectory(),"tempImage.jpg");
                try{
                    if(OutputImage.exists()){
                        OutputImage.delete();
                    }
                    OutputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(OutputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File OutputImage = new File(Environment.getExternalStorageDirectory(),"chooseImage.jpg");
                try{
                    if(OutputImage.exists()){
                        OutputImage.delete();
                    }
                    OutputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(OutputImage);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setDataAndType(imageUri,"image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                intent.putExtra("scale",true);
                intent.putExtra("crop",true);
                startActivityForResult(intent,CROP_PHOTO);

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TAKE_PHOTO){
            if(resultCode == RESULT_OK){
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imageUri,"image/*");
                intent.putExtra("scale",true);
                intent.putExtra("crop",true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,CROP_PHOTO);

            }
        }
        if(requestCode == CROP_PHOTO){
            if(resultCode == RESULT_OK){
                try{
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    photoImageView.setImageBitmap(bitmap);
                }catch (IOException e){
                    e.printStackTrace();
                }


            }
        }
    }
}
