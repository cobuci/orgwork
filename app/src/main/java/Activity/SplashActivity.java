package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orgwork.renewed.Menu;
import com.orgwork.renewed.R;

import Class.Conexao;
import Class.Usuario;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference referenciaFirebase;
    private Usuario usuario;
    private String tipoUsuarioEmail;
    private TextView tipoUsuario;

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        // Tempo do splash
        int SPLASH_TIME_OUT = 400;

        tipoUsuario = findViewById(R.id.txtTipoUsuario);
        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                if(usuarioLogado()){
                    telaInicio();

                }else{
                    telaLogin();
                }

            }
        }, SPLASH_TIME_OUT);
    }

    public void telaLogin(){
        Intent i = new Intent(SplashActivity.this,
                MainActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


    }

    public void telaInicio(){

        auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail();


        referenciaFirebase.child("usuarios").orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    tipoUsuarioEmail = postSnapshot.child("tipoUsuario").getValue().toString();

                    tipoUsuario.setText(tipoUsuarioEmail);

                    if (tipoUsuarioEmail.equals("ADM")) {

                        Intent i = new Intent(SplashActivity.this,
                                AdministratorActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        finish();
                    } else{
                        Intent i = new Intent(SplashActivity.this,
                                Menu.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });


    }

    public Boolean usuarioLogado(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }
}