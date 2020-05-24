package Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orgwork.renewed.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import Class.Blog;
import Class.Conexao;
import Class.Usuario;

public class NewPostActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private Usuario usuario;
    private FirebaseUser user;
    private Blog blog;
    private EditText etTitulo,etDescricao,etLink;
    private DatabaseReference myRef;
    private Button btnAdd;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);




        database = FirebaseDatabase.getInstance();
        myRef =  database.getReference();

        blog = new Blog();

        etTitulo = findViewById(R.id.etTitulo);
        etDescricao = findViewById(R.id.etDescricao);
        etLink = findViewById(R.id.etLink);



    }


    public void botao(View v){


        String titulo1 = etTitulo.getText().toString();
        String descricao1 = etDescricao.getText().toString();
        String link1 = etLink.getText().toString().toLowerCase();

        if (!link1.startsWith("http://") && !link1.startsWith("https://")){
            link1 = "http://" + link1;
        }

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();



        String dataFormatada = formataData.format(data);

        criarPost(titulo1, descricao1, dataFormatada, link1);
    }



    public void criarPost(String titulo, String descricao, String data, String link){


        String key = myRef.child("post").push().getKey();

        blog.setNome(titulo);
        blog.setAutor(user.getDisplayName());
        blog.setDescricao(descricao);
        blog.setKeyPost(key);
        blog.setTimestamp(data);
        blog.setLink(link);

        myRef.child("post").child(key).setValue(blog);


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