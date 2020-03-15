package Activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.orgwork.renewed.Menu;
import com.orgwork.renewed.R;

import Class.Conexao;
import Class.ProgressButton;

public class MainActivity extends AppCompatActivity {

    View LoginButton, RecoverButton;





    // Firebase
    private EditText txtEmail,txtPassword;
    private FirebaseAuth auth;



    // Bottom Sheet TELA LOGIN
    private BottomSheetBehavior bottomSheetBehavior;
    Button btn_login;
    LinearLayout login_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FIREBASE
        txtPassword = findViewById(R.id.txtPassword);
        txtEmail = findViewById(R.id.txtEmail);

        //Status bar Transparente
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //
        btn_login = findViewById(R.id.btn_login);
        LoginButton = findViewById(R.id.login);
        RecoverButton = findViewById(R.id.btnForgot);

        RecoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recover();

            }
        });


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });


        //get the bottom sheet view
        login_layout = findViewById(R.id.login_layout);

        //init the bottom sheet view
        bottomSheetBehavior = BottomSheetBehavior.from(login_layout);



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int i) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float v) {

            }
        });


    }


    // Botão Voltar
    private static final int TIME_INTERVAL = 600; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {

            if(bottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

        } else {
            if(bottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        mBackPressed = System.currentTimeMillis();
    }

    // Toast
    public void ToastCurto(String a) {
        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
    }


    // LOGIN com FIREBASE

    public void login(){

        // VIBRAR

        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();



        final ProgressButton progressButton = new ProgressButton(MainActivity.this, LoginButton);


        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);




        if(email.isEmpty() || password.isEmpty()){
            if(email.isEmpty() && password.isEmpty()){

                txtEmail.setBackgroundResource(R.drawable.edit_text_error);
                txtPassword.setBackgroundResource(R.drawable.edit_text_error);
            }else if(email.isEmpty()){
                txtEmail.setBackgroundResource(R.drawable.edit_text_error);
                txtPassword.setBackgroundResource(R.drawable.edit_text);
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            }else if(password.isEmpty()){
                txtEmail.setBackgroundResource(R.drawable.edit_text);
                txtPassword.setBackgroundResource(R.drawable.edit_text_error);
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            }

            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else{

            progressButton.buttonActivatedLoggin();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                                            
                        txtEmail.setBackgroundResource(R.drawable.edit_text);
                        txtPassword.setBackgroundResource(R.drawable.edit_text);

                        progressButton.buttonFinished();

                        Intent i = new Intent(MainActivity.this, Menu.class);
                        startActivity(i);
                        finish();


                    }else{
                        // VIBRAR
                        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.EFFECT_DOUBLE_CLICK));

                        txtEmail.setBackgroundResource(R.drawable.edit_text_error);
                        txtPassword.setBackgroundResource(R.drawable.edit_text_error);

                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressButton.buttonLoggin();
                            }
                        },1500);


                        progressButton.buttonErrorLoggin();



                    }
                }
            });



        }


    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        if(bottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    public void recover() {
        Intent intent = new Intent(this, RecoveryActivity.class);

        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        if(bottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();

    }


}
