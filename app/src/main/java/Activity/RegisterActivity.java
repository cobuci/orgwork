package Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
    TextInputLayout textInputLayoutUserCad,textInputLayoutSenhaCad,textInputLayoutEmailCad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textInputLayoutUserCad = findViewById(R.id.textInputLayoutUserCad);
        textInputLayoutSenhaCad = findViewById(R.id.textInputLayoutSenhaCad);
        textInputLayoutEmailCad = findViewById(R.id.textInputLayoutEmailCad);

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

        // Remover erros quando clicar


    }

    public void TextFieldClicked(View view){
        if(view.getId()==R.id.textInputLayoutUserCad);
        textInputLayoutUserCad.setError(null);
        if(view.getId()==R.id.textInputLayoutSenhaCad);
        textInputLayoutSenhaCad.setError(null);
        if(view.getId()==R.id.textInputLayoutEmailCad);
        textInputLayoutEmailCad.setError(null);
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
        escondeTeclado();


        String username = txtUsername.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();


        if(username.isEmpty() || password.isEmpty() || email.isEmpty()){
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                if(username.isEmpty() && password.isEmpty() && email.isEmpty()){
                    // Error

                    textInputLayoutUserCad.setError("Este campo não pode ficar em branco.");
                    textInputLayoutSenhaCad.setError("Este campo não pode ficar em branco.");
                    textInputLayoutEmailCad.setError("Este campo não pode ficar em branco.");
                }else if(username.isEmpty() && password.isEmpty()){
                    // Error
                    textInputLayoutUserCad.setError("Este campo não pode ficar em branco.");
                    textInputLayoutSenhaCad.setError("Este campo não pode ficar em branco.");
                    // success
                    textInputLayoutEmailCad.setError(null);
                }else if(username.isEmpty() && email.isEmpty()){
                    // Error
                    textInputLayoutUserCad.setError("Este campo não pode ficar em branco.");
                    textInputLayoutEmailCad.setError("Este campo não pode ficar em branco.");
                    // success
                    textInputLayoutSenhaCad.setError(null);
                } else if(email.isEmpty() && password.isEmpty()){
                    // Error
                    textInputLayoutEmailCad.setError("Este campo não pode ficar em branco.");
                    textInputLayoutSenhaCad.setError("Este campo não pode ficar em branco.");
                    // success
                    textInputLayoutUserCad.setError(null);
                }else if (email.isEmpty()){
                    // Error
                    textInputLayoutEmailCad.setError("Este campo não pode ficar em branco.");
                    // success
                    textInputLayoutUserCad.setError(null);
                    textInputLayoutSenhaCad.setError(null);
                }else if(username.isEmpty()){
                    // Error
                    textInputLayoutUserCad.setError("Este campo não pode ficar em branco.");
                    // success
                    textInputLayoutSenhaCad.setError(null);
                    textInputLayoutEmailCad.setError(null);
                }else{
                    // Error
                    textInputLayoutSenhaCad.setError("Este campo não pode ficar em branco.");
                    // success
                    textInputLayoutUserCad.setError(null);
                    textInputLayoutEmailCad.setError(null);
                }


            }else {

                if(txtPassword.length() <8){
                    textInputLayoutSenhaCad.setError("A senha deve ter pelo menos 8 caracteres.");
                }else
                {

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

    public void escondeTeclado(){
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}