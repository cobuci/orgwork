package Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orgwork.renewed.R;

import Activity.Fragment.AgendaFragment;
import Activity.Fragment.AjudaFragment;
import Activity.Fragment.HomeFragmentADM;
import Activity.Fragment.ProfileFragment;
import Class.Conexao;


public class AdministratorActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        BottomNavigationView bottomNav = findViewById(R.id.menu_adm);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_adm,
                new HomeFragmentADM()).commit();

    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_inicio_adm:
                            selectedFragment = new HomeFragmentADM();
                            break;
                        case R.id.nav_ajuda_adm:
                            selectedFragment = new AjudaFragment();
                            break;
                        case R.id.nav_agenda:
                            selectedFragment = new AgendaFragment();
                            break;
                        case R.id.nav_perfil:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_adm,
                            selectedFragment).commit();

                    return true;
                }
    };

    @Override
    public void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();

    }




}