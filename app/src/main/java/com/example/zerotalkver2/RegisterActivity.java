package com.example.zerotalkver2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText userEditTxt, pwEditTxt, emailEditTxt;
    Button registerBtn;

    // Firebase
    FirebaseAuth auth; // sync 연결해주어야 함
    DatabaseReference myRef; // 또한 sync


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEditTxt = findViewById(R.id.userLogin_EditText);
        pwEditTxt = findViewById(R.id.pwLogin_EditText);
        emailEditTxt = findViewById(R.id.email_EditText);

        registerBtn = findViewById(R.id.register_Btn);

        //Firebase Auth
        // auth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_text = userEditTxt.getText().toString();
                String email_text = emailEditTxt.getText().toString();
                String pw_text = pwEditTxt.getText().toString();

                if (TextUtils.isEmpty(username_text) || TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pw_text)){
                    Toast.makeText(RegisterActivity.this,"모두 작성해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    RegisterNow(username_text, email_text, pw_text);
                };

            }
        });


    }

    private  void RegisterNow(final String username, String pw, String email){
        // 파이어베이스의 식별 테스트를 만들어 줌
        auth.createUserWithEmailAndPassword(pw, email)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            // Opening the Main Activity after Success Registration
                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterActivity.this,"Invalid Email or Password ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}