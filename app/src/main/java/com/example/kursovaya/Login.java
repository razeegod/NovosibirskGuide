package com.example.kursovaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursovaya.Admins.AdminPanel;
import com.example.kursovaya.Users.Profile;
import com.example.kursovaya.prevalent.prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private Button LoginButton;
    private EditText InputEmail;
    private EditText InputPassword;
    private ProgressDialog loadingBar;
    private CheckBox CBrememberUser;
    private TextView ForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        mAuth = FirebaseAuth.getInstance();
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(InputEmail.getText().toString())) {
                    Toast.makeText(Login.this, "Введите почту!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(InputPassword.getText().toString())) {
                    Toast.makeText(Login.this, "Введите пароль!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Авторизация");
                    loadingBar.setMessage("Выполняется вход...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    loginUser(InputEmail.getText().toString(), InputPassword.getText().toString());
                }
            }

        });

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetPassIntent = new Intent(Login.this, FogetPassword.class);
                startActivity(resetPassIntent);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User null", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(String email, String password) {
        if (CBrememberUser.isChecked()) {
            Paper.book().write(prevalent.UserEmailKey, email);
            Paper.book().write(prevalent.UserPasswordKey, password);
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    DatabaseReference roleRef;
                    String userUid = mAuth.getCurrentUser().getUid();

                    roleRef = FirebaseDatabase.getInstance().getReference("User").child(userUid);

                    roleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String roleName = snapshot.child("role").getValue(String.class);

                            if (roleName != null && roleName.equals("Admin")) {
                                Intent adminIntent = new Intent(Login.this, AdminPanel.class);
                                startActivity(adminIntent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(Login.this, "Вы успешно вошли!", Toast.LENGTH_SHORT).show();
                                Intent succesLogin = new Intent(Login.this, Profile.class);
                                startActivity(succesLogin);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(Login.this, "Неправильный логин или пароль!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        LoginButton = findViewById(R.id.login_btn);
        InputEmail = findViewById(R.id.input_login);
        InputPassword = findViewById(R.id.input_password);
        loadingBar = new ProgressDialog(this);
        CBrememberUser = findViewById(R.id.login_remember);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        Paper.init(this);
        ForgetPassword = findViewById(R.id.forget_password);
    }
}

