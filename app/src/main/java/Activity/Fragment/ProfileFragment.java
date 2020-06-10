package Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orgwork.renewed.R;

import Activity.ChangePasswordActivity;
import Activity.HelpActivity;
import Activity.SplashActivity;
import Class.Conexao;
import Class.Usuario;


public class ProfileFragment extends Fragment {

    private Usuario usuario;
    private FirebaseAuth auth;
    private FirebaseUser user;
    TextView email,name;
    Button btn,btnPass, btnExclude, btnAjuda;
    private DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        btnAjuda = view.findViewById(R.id.btn_Ajuda);

        btnAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HelpActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });


        btnExclude = view.findViewById(R.id.btn_ExcluirConta);

        btnExclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirUsuario();
            }
        });

        //Botão deslogar
      btn = view.findViewById(R.id.btn_Sair);

       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Conexao.logOut();
               getActivity().finish();
               Intent intent = new Intent(v.getContext(), SplashActivity.class);
               startActivity(intent);
               getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
           }
       });

       // Botão mudar senha

        btnPass = view.findViewById(R.id.btn_MudarSenha);

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ChangePasswordActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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

    public void ToastCurto(String a) {
        Toast.makeText(getView().getContext(), a, Toast.LENGTH_SHORT).show();
    }

    private void excluirUsuario(){

        reference = Conexao.getFirebase();

        String emailUsuario = auth.getCurrentUser().getEmail();


        reference.child("usuarios").orderByChild("email").equalTo(emailUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    final Usuario usuario = postSnapshot.getValue(Usuario.class);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Log.d("USUARIO_EXCLUIDO", "User Deleted");
                                        ToastCurto("Cadastro deletado");

                                        reference = Conexao.getFirebase();
                                        reference.child("usuarios").child(usuario.getKeyUsuario()).removeValue();

                                        Conexao.logOut();
                                        getActivity().finish();
                                        Intent intentF = new Intent(getContext(), SplashActivity.class);
                                        startActivity(intentF);
                                        getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                    }else{
                                        ToastCurto("Ocorreu um erro");
                                    }


                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void verificaUser() {
        if (user != null){
            email = getView().findViewById(R.id.txt_email_perfil);
            name = getView().findViewById(R.id.txt_nome_perfil);


            email.setText(user.getEmail());
            name.setText(user.getDisplayName());

        }
    }
}
