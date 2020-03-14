package com.orgwork.renewed;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class recover extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText txtEmail;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);



        view = findViewById(R.id.btnRecover);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetPassword();


            }
        });
    }

    public void ToastCurto(String a) {
        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
    }

    private void resetPassword() {

        txtEmail =  findViewById(R.id.txtEmailRecover);
        String email = txtEmail.getText().toString().trim();

        final ProgressButton progressButton = new ProgressButton(recover.this, view);

        if (email.equals("")){
            ToastCurto("Digite um email valido");

        }else {
            auth
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressButton.buttonFinished();
                                ToastCurto("Um email para redefinição de senha foi enviado");
                                finish();
                            }else {
                                progressButton.buttonError();
                            }
                        }
                    });

        }

    }




    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        ProgressButton progressButton = new ProgressButton(recover.this, view);
        progressButton.buttonRecover();
    }
}