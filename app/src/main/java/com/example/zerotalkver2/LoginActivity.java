package com.example.zerotalkver2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    EditText userEditLogin, pwEditLogin;
    Button loginBtn, registerBtn;

    // Firebase
    FirebaseAuth  auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEditLogin = findViewById(R.id.userLogin_EditText);
        pwEditLogin = findViewById(R.id.pwLogin_EditText);
        loginBtn = findViewById(R.id.login_Btn);
        registerBtn = findViewById(R.id.login_Btn_ForReg);

        // Firebase Auth:
        auth = FirebaseAuth.getInstance();

        // 사용자가 로그인 할 수 있도록 허용
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        // RegisterBtn



        // Login Button:
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = userEditLogin.getText().toString();
                String pw_text = pwEditLogin.getText().toString();

                // Checking if it is empty
                if(TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pw_text)){
                    Toast.makeText(LoginActivity.this, "모두 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(email_text,pw_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // 성공적으로 들어왔으면 파이어베이스에 들어가기 위해 Gradle을 확인..
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                };
            }
        });


    }
}