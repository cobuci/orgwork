package Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orgwork.renewed.R;

import Class.Conexao;
import Class.ProgressButton;
import Class.Usuario;


public class RegisterActivity extends AppCompatActivity {


    private EditText txtUsername,txtPassword,txtEmail;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Usuario usuario;
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
                usuario = new Usuario();
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
            // Vibração
        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);



        String username = txtUsername.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();


        if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                if(username.isEmpty() && password.isEmpty() && email.isEmpty()){
                    // Error
                    txtUsername.setBackgroundResource(R.drawable.edit_text_error);
                    txtEmail.setBackgroundResource(R.drawable.edit_text_error);
                    txtPassword.setBackgroundResource(R.drawable.edit_text_error);
                }else if(username.isEmpty() && password.isEmpty()){
                    // Error
                    txtUsername.setBackgroundResource(R.drawable.edit_text_error);
                    txtPassword.setBackgroundResource(R.drawable.edit_text_error);
                    // success
                    txtEmail.setBackgroundResource(R.drawable.edit_text);
                }else if(username.isEmpty() && email.isEmpty()){
                    // Error
                    txtUsername.setBackgroundResource(R.drawable.edit_text_error);
                    txtEmail.setBackgroundResource(R.drawable.edit_text_error);
                    // success
                    txtPassword.setBackgroundResource(R.drawable.edit_text);
                } else if(email.isEmpty() && password.isEmpty()){
                    // Error
                    txtEmail.setBackgroundResource(R.drawable.edit_text_error);
                    txtPassword.setBackgroundResource(R.drawable.edit_text_error);
                    // success
                    txtUsername.setBackgroundResource(R.drawable.edit_text);
                }else if (email.isEmpty()){
                    // Error
                    txtEmail.setBackgroundResource(R.drawable.edit_text_error);
                    // success
                    txtUsername.setBackgroundResource(R.drawable.edit_text);
                    txtPassword.setBackgroundResource(R.drawable.edit_text);
                }else if(username.isEmpty()){
                    // Error
                    txtUsername.setBackgroundResource(R.drawable.edit_text_error);
                    // success
                    txtPassword.setBackgroundResource(R.drawable.edit_text);
                    txtEmail.setBackgroundResource(R.drawable.edit_text);
                }else{
                    // Error
                    txtPassword.setBackgroundResource(R.drawable.edit_text_error);
                    // success
                    txtUsername.setBackgroundResource(R.drawable.edit_text);
                    txtEmail.setBackgroundResource(R.drawable.edit_text);
                }


            }else {


            usuario.setEmail(txtEmail.getText().toString());
            usuario.setSenha(txtPassword.getText().toString());
            usuario.setNome(txtUsername.getText().toString());

            final ProgressButton progressButton = new ProgressButton(RegisterActivity.this, view);

            progressButton.buttonActivatedRegister();


            auth.createUserWithEmailAndPassword(
                    usuario.getEmail(),
                    usuario.getSenha())

                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                userProfile();
                                usuario.setTipoUsuario("Usuario");
                                insereUsuario(usuario);

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
        String username = txtUsername.getText().toString().trim();
        FirebaseUser user = auth.getCurrentUser();
        if (user!= null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(usuario.getNome())
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
        ProgressButton progressButton = new ProgressButton(RegisterActivity.this, view);
        progressButton.buttonRegister();
    }

    private boolean insereUsuario(Usuario usuario){
        try{
            reference = Conexao.getFirebase().child("usuarios");
            reference.push().setValue(usuario);

            return true;
        }catch (Exception e){
            return false;
        }
    }

}