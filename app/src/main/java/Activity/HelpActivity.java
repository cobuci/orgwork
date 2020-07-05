package Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orgwork.renewed.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import Class.Ajuda;
import Class.Conexao;
import Class.Usuario;

public class HelpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Usuario usuario;
    private FirebaseUser user;
    private Ajuda ajuda;
    private EditText etMsg,etDescricao,etLink;
    private DatabaseReference myRef;
    private Button btnAjuda;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        etMsg = findViewById(R.id.etMensagemAjuda);

        database = FirebaseDatabase.getInstance();
        myRef =  database.getReference();
        ajuda = new Ajuda();
        // spinner
        Spinner spinner = findViewById(R.id.spinnerAjuda);



        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_solicitacao, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        // botão pedir ajuda
        btnAjuda = findViewById(R.id.btnNewAjuda);

        btnAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botaoAjuda();
            }
        });


    }

    public void botaoAjuda(){

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();


        Spinner spinner = findViewById(R.id.spinnerAjuda);

        String tipoSelecionado = spinner.getSelectedItem().toString();


        String etMsg1 = etMsg.getText().toString();


        String dataFormatada = formataData.format(data);

        novaAjuda(tipoSelecionado , etMsg1, dataFormatada);

    }

    public void novaAjuda(String tipo, String mensagem, String data){


        String key = myRef.child("ajuda").push().getKey();


        ajuda.setKeyPost(key);
        ajuda.setAutor(user.getDisplayName());
        ajuda.setEmailAutor(user.getEmail());
        ajuda.setTimestamp(data);
        ajuda.setTipo(tipo);
        ajuda.setMensagem(mensagem);
        ajuda.setStatusAjuda("Aberta");



        myRef.child("ajuda").child(key).setValue(ajuda);


        finish();


    }

    // Executa quando startar
    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();


    }

}