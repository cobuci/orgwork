package Acitivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.orgwork.renewed.R;

import Class.Conexao;
import Class.ProgressButton;

public class activity_register extends AppCompatActivity {


    private EditText txtUsername,txtPassword,txtEmail;
    private FirebaseAuth auth;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        txtUsername = findViewById(R.id.txtUsername);
        txtPassword =  findViewById(R.id.txtPassword);
        txtEmail =  findViewById(R.id.txtEmail);

        view = findViewById(R.id.btnRegister);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cadastro();


            }
        });

    }


    // Toast
    public void ToastCurto(String a) {
        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
    }


    // Botão Voltar
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                return;

        } else {
                ToastCurto("Pressione voltar novamente para cancelar o cadastro.");
        }

        mBackPressed = System.currentTimeMillis();
    }


    public void alreadyAccount(View v){
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();


    }


    // CADASTRO
    public void cadastro(){

            String username = txtUsername.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();


        if(username.equals("")){
            ToastCurto("Name is required");
        }else if(email.equals("")){
            ToastCurto("Email is required");
        }else if (password.equals("")){
            ToastCurto("Password is required");
        }else if(password.length() <6){
            ToastCurto("A Senha é curta demais");
        }else{


            final ProgressButton progressButton = new ProgressButton(activity_register.this, view);

            progressButton.buttonActivatedRegister();


            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity_register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                userProfile();

                                progressButton.buttonFinished();


                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        finish();
                                    }
                                },1000);

                            }else {


                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressButton.buttonRegister();
                                    }
                                },1500);

                                progressButton.buttonError();
                            }

                        }
                    });
        }

    }




    private void userProfile(){
        FirebaseUser user = auth.getCurrentUser();
        if (user!= null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(txtUsername.getText().toString().trim())
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TESTING", "User profile updated.");
                            }
                        }
                    });
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        ProgressButton progressButton = new ProgressButton(activity_register.this, view);
        progressButton.buttonRegister();
    }

    }