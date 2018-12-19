package com.pythonanywhere.cozinfinitybehind.retrofituploader;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.pythonanywhere.cozinfinitybehind.retrofituploader.remote.IUploaderAPI;
import com.pythonanywhere.cozinfinitybehind.retrofituploader.remote.RetrofitClient;
import com.pythonanywhere.cozinfinitybehind.retrofituploader.utils.ProgressRequestBody;
import com.pythonanywhere.cozinfinitybehind.retrofituploader.utils.UploadCallBacks;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UploadCallBacks {

        public  static  final String BASE_URL = "http://10.0.2.2/";
        public static final int REQUEST_PERMISSION = 1000;

        IUploaderAPI mService;
        Button btnUpload, btnChoose, btnCount;
        TextView txt, txtCount;
        File choosedFile;
        ProgressDialog dialog;
        NotificationCompat.Builder mBuilder;
        NotificationManagerCompat notificationManager;
        String CHANNEL_ID ="uploadChannelID";

        private IUploaderAPI getAPIUpload(){
            return RetrofitClient.getClient(BASE_URL).create(IUploaderAPI.class);
        }

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check permission

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION);
        }


        //Service
        mService = getAPIUpload();
        btnUpload = findViewById(R.id.button);
        btnChoose = findViewById(R.id.button2);
        btnCount = findViewById(R.id.button3);
        txt = findViewById(R.id.textView);
        txtCount = findViewById(R.id.textView2);


        enable_button();

        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                txtCount.setText(""+count);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    private void enable_button() {

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialFilePicker filePicker = new MaterialFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(10);
                filePicker.start();
            }
        });

    }

    private void uploadFile() {

            if(choosedFile !=null){
                notificationManager = NotificationManagerCompat.from(this);
                mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
                mBuilder.setContentTitle("Uploading "+choosedFile.getName())
                        .setContentText("Uploading....")
                        .setSmallIcon(R.drawable.ic_video_box)
                        .setPriority(NotificationCompat.PRIORITY_LOW);

                int PROGRESS_MAX = 100;
                int PROGRESS_CURRENT = 0;
                mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
                notificationManager.notify(1, mBuilder.build());


                ProgressRequestBody requestFile = new ProgressRequestBody(choosedFile, this);

                final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", choosedFile.getName(), requestFile);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mService.uploadedFile(body)
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                                //Updating the notification to complete status
                                mBuilder.setContentText("Upload complete")
                                        .setProgress(0,0,false);
                                notificationManager.notify(1 ,mBuilder.build());
                                Toast.makeText(MainActivity.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                mBuilder.setContentText("File not uploaded. Retry")
                                        .setProgress(0,0,false);
                                notificationManager.notify(1 ,mBuilder.build());
                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10 && resultCode==RESULT_OK){

            if(data!=null){
                choosedFile = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                txt.setText(choosedFile.getAbsolutePath());
            }
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {

        mBuilder.setProgress(100,percentage,false);
        notificationManager.notify(1, mBuilder.build());
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION && (grantResults[0]==PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }
        else {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    //Permission not granted
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,},1000);
                    return;
                }
            }
        }
    }
}
