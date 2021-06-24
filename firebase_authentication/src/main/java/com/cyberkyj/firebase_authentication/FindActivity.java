package com.cyberkyj.firebase_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindActivity extends AppCompatActivity {

    EditText edtEmail;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        edtEmail = findViewById(R.id.edtEmail3);
        Button btnFind = findViewById(R.id.btnFind);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("처리중입니다. 잠시 기다려주세요..");;
                progressDialog.show();

                String email = edtEmail.getText().toString();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(FindActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(FindActivity.this, "이메일 보내기 실패!", Toast.LENGTH_SHORT).show();

                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });
    }
}