package Class;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.orgwork.renewed.R;

public class ProgressButton {

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;



    public ProgressButton(Context ct, View view){

        cardView = view.findViewById(R.id.cardView);
        layout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textLoginProgress);

    }

    public void buttonRegister(){
        textView.setText("Registrar");
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.colorPrimary));
    }
    public void buttonLoggin(){
        textView.setText("Login");
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.colorPrimary));
    }
    public void buttonActivatedRegister(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Cadastrando...");
    }
    public void buttonActivatedLoggin(){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Entrando...");
    }
    public void buttonFinished(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.green));
        progressBar.setVisibility(View.GONE);
        textView.setText("Success");
    }
    public void buttonError(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.Red));
        progressBar.setVisibility(View.GONE);

    }

    public void buttonErrorLoggin(){
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.Red));
        progressBar.setVisibility(View.GONE);
        textView.setText("Credenciais invalidas");
    }
    public void buttonRecover(){
        textView.setText("Recuperar");
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.colorPrimary));
    }
    public void buttonChange(){
        textView.setText("Alterar senha");
        layout.setBackgroundColor(cardView.getResources().getColor(R.color.colorPrimary));
    }

}
