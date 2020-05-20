package Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.orgwork.renewed.R;

import Class.Conexao;
import Class.ProgressButton;



public class RecoveryActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    View view;
    TextInputLayout ILemailRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);

        ILemailRecover = findViewById(R.id.ILemailRecover);

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

        EditText txtEmail = findViewById(R.id.txtEmailRecover);
        String email = txtEmail.getText().toString().trim();

        final ProgressButton progressButton = new ProgressButton(RecoveryActivity.this, view);

        if (email.isEmpty()){

            ILemailRecover.setError("Digite um email valido.");

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
                                ILemailRecover.setError("Digite um email valido.");

                            }
                        }
                    });

        }

    }




    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        ProgressButton progressButton = new ProgressButton(RecoveryActivity.this, view);
        progressButton.buttonRecover();
    }
}