package com.cyberkyj.firebase_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtEmail, edtPassword;
    TextView txtSignIn, txtMessage;
    Button btnSiginUp;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtSignIn = findViewById(R.id.viewSignIn);
        txtMessage = findViewById(R.id.textView2);
        btnSiginUp = findViewById(R.id.btnSignUP);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        }

        progressDialog = new ProgressDialog(this);

        btnSiginUp.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==btnSiginUp){
            registerUser();
        }else if(v==txtSignIn){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void registerUser(){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Email 주소나 Password를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("등록중입니다. 기다려주세요");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SignInActivity.this, "회원가입 등록실패!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}










