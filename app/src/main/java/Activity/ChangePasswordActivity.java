package Activity;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orgwork.renewed.R;

import Class.Conexao;
import Class.ProgressButton;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText txtNewPassword,txtNewPassword2;

    TextInputLayout textInputLayout,textInputLayout2;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Layout Password
        textInputLayout = findViewById(R.id.textInputLayout);
        textInputLayout2 = findViewById(R.id.textInputLayout2);

        // Edit Text Password
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtNewPassword2 = findViewById(R.id.txtNewPassword2);

        // Botão Alterar senha
        view = findViewById(R.id.btnChangePassword);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changePassword();


            }
        });






    }

    private void changePassword() {

        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        shortPasswordError();

        if(txtNewPassword.getText().toString().equals(txtNewPassword2.getText().toString()) && !txtNewPassword.getText().toString().isEmpty() && !txtNewPassword2.getText().toString().isEmpty()){


            if(user!=null){

                user.updatePassword(txtNewPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                ToastCurto("Senha alterada com sucesso");
                                finish();
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            }else{
                                ToastCurto("Ocorreu um erro.");
                                escondeTeclado();

                            }
                        }
                    });
            }


        }else{
            escondeTeclado();
            ToastCurto("As senhas não coincidem.");
            textInputLayout2.setError("As senhas devem ser iguais.");
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

        }



    }

    public void escondeTeclado(){
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void ToastCurto(String a) {
        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
    }

    public void shortPasswordError(){
        if(txtNewPassword.getText().toString().length() < 8){

            textInputLayout.setError("A senha deve ter pelo menos 8 caracteres.");

        }else{
            textInputLayout.setError(null);
        }

        if(txtNewPassword2.getText().toString().length() < 8 ){

            textInputLayout2.setError("A senha deve ter pelo menos 8 caracteres.");

        }else{
            textInputLayout2.setError(null);
        }

    }

    // Botão Voltar
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    public void onBackPressed() {

            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = Conexao.getFirebaseAuth();

        ProgressButton progressButton = new ProgressButton(ChangePasswordActivity.this, view);
        progressButton.buttonChange();
    }
}