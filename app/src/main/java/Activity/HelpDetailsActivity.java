package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.orgwork.renewed.R;

public class HelpDetailsActivity extends AppCompatActivity {

    EditText txtAutor, txtTipo, txtMensagem, txtEmailAutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_details);

        txtAutor = findViewById(R.id.txtAutorDuvida);
        txtTipo = findViewById(R.id.txtTipo);
        txtMensagem = findViewById(R.id.txtMensagemDuvida);
        txtEmailAutor = findViewById(R.id.txtAutorDuvida2);


        String autor = getIntent().getExtras().getString("autor");
        txtAutor.setText(autor);

        String tipo = getIntent().getExtras().getString("tipo");
        txtTipo.setText(tipo);


        String mensagem  = getIntent().getExtras().getString("mensagem");
        txtMensagem.setText(mensagem);

        String emailAutor = getIntent().getExtras().getString("emailAutor");
        txtEmailAutor.setText(emailAutor);


        Button responderEmail = findViewById(R.id.btnResponderEmail);

        responderEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                String autor = getIntent().getExtras().getString("autor");
                String emailAutor = getIntent().getExtras().getString("emailAutor");

                String[] recipients = new String[] {
                        emailAutor
                };


                // Quem recebe
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
                // Assunto
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "OrgWork - Reposta ");
                // Conteúdo
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Olá, "+autor+"\n\n");


                emailIntent.setType("text/plain");

                startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            }
        });


    }



}