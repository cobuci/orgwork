package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orgwork.renewed.Menu;
import com.orgwork.renewed.R;

import Class.Conexao;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;


    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Tempo do splash
        int SPLASH_TIME_OUT = 1600;

        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                if(usuarioLogado()){
                    telaInicio();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    // Fecha esta activity
                    finish();
                }else{
                    telaLogin();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    // Fecha esta activity
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }

    public void telaLogin(){
        Intent i = new Intent(SplashActivity.this,
                MainActivity.class);
        startActivity(i);

    }

    public void telaInicio(){
        Intent i = new Intent(SplashActivity.this,
                Menu.class);
        startActivity(i);
    }

    public Boolean usuarioLogado(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }
}