package com.example.kursovaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kursovaya.Users.Profile;
import com.example.kursovaya.prevalent.prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button JoinButton;
    private Button RegisterButton;
    private Button GuestButton;
    private ProgressDialog loadingBar;
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        mAuth = FirebaseAuth.getInstance();

        Paper.init(this);

        JoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, Login.class);
                startActivity(loginIntent);
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, Registration.class);
                startActivity(registerIntent);
            }
        });

        String UserEmailKey=Paper.book().read(prevalent.UserEmailKey);
        String UserPasswordKey=Paper.book().read(prevalent.UserPasswordKey);

        if(UserEmailKey != "" && UserPasswordKey != "")
        {
            if(!TextUtils.isEmpty(UserEmailKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                ValidateUser(UserEmailKey, UserPasswordKey);

                loadingBar.setTitle("Авторизация");
                loadingBar.setMessage("Выполняется вход...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void ValidateUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Вы успешно вошли!", Toast.LENGTH_SHORT).show();
                    Intent succesLogin = new Intent(MainActivity.this, Profile.class);
                    startActivity(succesLogin);
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Неправильный логин или пароль!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
        private void init()
        {
            JoinButton = (Button) findViewById(R.id.join_btn);
            RegisterButton = (Button) findViewById(R.id.register_btn);
            GuestButton = (Button) findViewById(R.id.joinguest_btn);
            loadingBar = new ProgressDialog(this);
            mDataBase = FirebaseDatabase.getInstance().getReference();
        }
    }