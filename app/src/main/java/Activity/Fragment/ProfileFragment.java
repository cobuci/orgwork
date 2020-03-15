package Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orgwork.renewed.R;

import Activity.SplashActivity;
import Class.Conexao;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    TextView email,name;
    Button btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        //Botão deslogar
      btn = view.findViewById(R.id.btn_Sair);

       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Conexao.logOut();
               getActivity().finish();
               Intent intent = new Intent(v.getContext(), SplashActivity.class);
               startActivity(intent);
           }
       });



        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        verificaUser();

    }




    private void verificaUser() {
        if (user == null){


        }else{
            email = getView().findViewById(R.id.txt_email_perfil);
            name = getView().findViewById(R.id.txt_nome_perfil);

            email.setText(user.getEmail());
            name.setText(user.getDisplayName());

        }
    }
}
