package com.cyberkyj.firestorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView ivPriview, ivResult;
    Uri filePath;
    String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPriview = findViewById(R.id.iv_Preview);
        ivResult = findViewById(R.id.iv_Result);
        Button btChoose = findViewById(R.id.bt_Choose);
        Button btUpload = findViewById(R.id.bt_Upload);
        Button btDownload = findViewById(R.id.bt_Download);

        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"이미지를 선택하세요."),1000);
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath!=null){
                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("업로드 중...");
                    progressDialog.show();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMHH_mmss");
                    Date date = new Date();
                    fileName = format.format(date)+".png";
                    StorageReference reference = storage.getReferenceFromUrl("gs://fir-2021-37b62.appspot.com").child("images/"+fileName);
                    reference.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "업로드 완료!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            @SuppressWarnings("VisibleFortests")
                            double progress = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                            progressDialog.setMessage("UPLOAD "+(int)progress+"%...");
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this, "파일을 먼저 선택하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference reference = storage.getReferenceFromUrl("gs://fir-2021-37b62.appspot.com").child("images/"+fileName);
                reference.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(MainActivity.this, "다운로드 성공 : "+uri, Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"다운로드 실패",Toast.LENGTH_SHORT).show();
                    }
                });
                try {
                    File localFile = File.createTempFile("images",".png");
                    reference.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(MainActivity.this, "파일 저장 성공", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    ivResult.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "파일 저장 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000 && resultCode==RESULT_OK){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                ivPriview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}