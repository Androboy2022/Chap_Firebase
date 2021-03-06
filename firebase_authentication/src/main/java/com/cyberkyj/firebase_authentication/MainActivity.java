package com.cyberkyj.firebase_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 10 ;
    EditText edtEmail, edtPassword;
    TextView txtFindPassword, txtSignIn, txtGoogleSignIn;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    Button btnSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail2);
        edtPassword = findViewById(R.id.edtPassword2);
        txtFindPassword = findViewById(R.id.txtFindPassword);
        txtSignIn = findViewById(R.id.txtViewSign);
        btnSignIn = findViewById(R.id.btnSignUP2);
        txtGoogleSignIn = findViewById(R.id.googleSignIn);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(this, ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);
        btnSignIn.setOnClickListener(this);
        txtFindPassword.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
        txtGoogleSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==btnSignIn){
            userLogin();
        }else if(v==txtSignIn){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }else if(v==txtFindPassword){
            startActivity(new Intent(this, FindActivity.class));
            finish();
        }else if(v==txtGoogleSignIn){
            signIn();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                           startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                           finish();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "????????? ??????!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void userLogin(){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Email ????????? Password??? ??????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("?????????????????????. ????????? ??????????????????");
        progressDialog.show();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                            finish();

                        }else{
                            Toast.makeText(MainActivity.this, "????????? ??????!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}