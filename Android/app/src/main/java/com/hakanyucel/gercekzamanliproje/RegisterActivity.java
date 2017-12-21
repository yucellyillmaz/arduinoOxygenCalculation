package com.hakanyucel.gercekzamanliproje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private Button btnBack, btnRegister;
    private EditText edtAdSoyad, edtEmail, edtPassword;
    private MyDbHelper mydbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        mydbhelper = new MyDbHelper(this);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnBack = (Button) findViewById(R.id.btnBack);

        edtAdSoyad = (EditText) findViewById(R.id.edtAdSoyad);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, password;
                name = edtAdSoyad.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                boolean result = mydbhelper.insertData_User(name, email, password);
                if(result) {
                    Toast.makeText(RegisterActivity.this, "Kaydedildi"
                            , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(RegisterActivity.this, "Kaydedilemedi."
                            , Toast.LENGTH_LONG).show();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
