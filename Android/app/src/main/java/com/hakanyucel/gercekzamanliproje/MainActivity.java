package com.hakanyucel.gercekzamanliproje;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin, btnRegister;
    private EditText edtEmail, edtPassword;
    private MyDbHelper mydbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mydbhelper = new MyDbHelper(this);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                Cursor result = mydbhelper.getAllData_User();
                boolean isCorrect = false;
                if(result.getCount() == 0)
                    Toast.makeText(MainActivity.this, "Kayıt Olmanız Gerekmektedir."
                            , Toast.LENGTH_LONG).show();
                else{
                    while(result.moveToNext())
                    {
                        if(email.equals(result.getString(2)) &&  password.equals(result.getString(3)))
                        {
                            isCorrect = true;
                            break;
                        }
                    }
                    if(isCorrect)
                    {
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(MainActivity.this, "Girmiş Olduğunuz Email/Parola Yanlıştır. Lütfen Tekrar Deneyin"
                                , Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
