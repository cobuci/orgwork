package com.orgwork.renewed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    TextView email,name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


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
