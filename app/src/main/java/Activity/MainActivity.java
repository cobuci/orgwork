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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orgwork.renewed.Menu;
import com.orgwork.renewed.R;

import Class.Conexao;
import Class.ProgressButton;
import Class.Usuario;

public class MainActivity extends AppCompatActivity {

    View LoginButton, RecoverButton;

    private DatabaseReference referenciaFirebase;
    private String tipoUsuarioEmail;
    private TextView tipoUsuario;

    // Firebase
    private EditText txtEmail,txtPassword;
    private FirebaseAuth auth;
    private Usuario usuario;


    TextInputLayout ILemailLogin, ILsenhaLogin;

    // Bottom Sheet TELA LOGIN
    private BottomSheetBehavior bottomSheetBehavior;
    Button btn_login;
    LinearLayout login_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Layout Edit Text
        ILemailLogin = findViewById(R.id.ILemailLogin);
        ILsenhaLogin = findViewById(R.id.ILsenhaLogin);

        // FIREBASE
        txtPassword = findViewById(R.id.txtPassword);
        txtEmail = findViewById(R.id.txtEmail);
        tipoUsuario = findViewById(R.id.txtTipoLogin);
        referenciaFirebase = FirebaseDatabase.getInstance().getReference();

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
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finishAffinity();
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



    // LOGIN com FIREBASE

    public void login(){

        // VIBRAR

        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();


        final ProgressButton progressButton = new ProgressButton(MainActivity.this, LoginButton);


        if(email.isEmpty() || password.isEmpty()){

            if(email.isEmpty() && password.isEmpty()){

                ILemailLogin.setError("Este campo não pode ficar em branco");
                ILsenhaLogin.setError("Este campo não pode ficar em branco");
            }else if(email.isEmpty()){
                ILemailLogin.setError("Este campo não pode ficar em branco");
                ILsenhaLogin.setError(null);

            }else {
                ILemailLogin.setError(null);
                ILsenhaLogin.setError("Este campo não pode ficar em branco");
            }

        }
        else{

            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setSenha(password);

            progressButton.buttonActivatedLoggin();
            auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        txtEmail.setBackgroundResource(R.drawable.edit_text);
                        txtPassword.setBackgroundResource(R.drawable.edit_text);

                        progressButton.buttonFinished();

                        // Abrir activity principal
                        auth = FirebaseAuth.getInstance();
                        String emailFir = auth.getCurrentUser().getEmail();

                        referenciaFirebase.child("usuarios").orderByChild("email").equalTo(emailFir).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    tipoUsuarioEmail = postSnapshot.child("tipoUsuario").getValue().toString();
                                    tipoUsuario.setText(tipoUsuarioEmail);

                                    if (tipoUsuarioEmail.equals("ADM")) {

                                        Intent iAdm = new Intent(MainActivity.this,
                                                AdministratorActivity.class);
                                        startActivity(iAdm);
                                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                                    } else{
                                        Intent i = new Intent(MainActivity.this,
                                                Menu.class);
                                        startActivity(i);
                                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                    }
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }else{
                        // VIBRAR
                        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.EFFECT_DOUBLE_CLICK));

                        // Mudar a borda do edit text
                        ILsenhaLogin.setError(" ");
                        ILemailLogin.setError(" ");

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

    // Botão pra abrir a activity de cadastro

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


        // Se a janela de login estiver aberta , fechar ela
        if(bottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    // Botão pra abrir a activity de recuperar senha
    public void recover() {
        Intent intent = new Intent(this, RecoveryActivity.class);

        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        // Se a janela de login estiver aberta , fechar ela
        if(bottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    // Executa quando startar
    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();

    }




}
