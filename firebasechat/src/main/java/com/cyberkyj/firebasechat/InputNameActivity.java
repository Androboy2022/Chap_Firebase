package com.cyberkyj.firebasechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputNameActivity extends AppCompatActivity {
    EditText edtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_name);
        Button btnStart = findViewById(R.id.btnStart);
        edtName = findViewById(R.id.edtName);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtName.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"이름을 입력하세요.",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("data", edtName.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
