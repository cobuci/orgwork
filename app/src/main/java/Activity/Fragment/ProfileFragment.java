package Activity.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orgwork.renewed.R;

import Activity.HelpActivity;
import Activity.SplashActivity;
import Class.Conexao;
import Class.Usuario;


public class ProfileFragment extends Fragment {


    private FirebaseAuth auth;
    private FirebaseUser user;
    TextView email, name;
    Button btn, btnPass, btnExclude, btnAjuda, btnChangePass, btnCancelPass;
    private DatabaseReference reference;

    TextView popupTexto1, popupTexto2;
    EditText senha1, senha2;
    Button popup_btnExcluir, popup_btnCancelar;
    Dialog popExclude, popChange;
    TextInputLayout inputSenha, inputSenha2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        iniPopup();
        iniPopupSenha();

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
              popExclude.show();
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
                                           popChange.show();
                                       }
                                   }
        );


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


    private void mudarSenha(String novaSenha, String novaSenha2) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (novaSenha.equals(novaSenha2) && !novaSenha.isEmpty() && !novaSenha2.isEmpty()) {

            if (novaSenha.length() < 8 || novaSenha2.length() < 8) {

                ToastCurto("A senha deve ter pelo menos 8 caracteres.");

            } else {
                if (user != null) {

                    user.updatePassword(novaSenha)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        ToastCurto("Senha alterada com sucesso");
                                        popChange.cancel();


                                    } else {
                                        ToastCurto("Ocorreu um erro.");


                                    }
                                }
                            });
                }

            }


        } else {

            ToastCurto("As senhas não coincidem.");


        }


    }


    private void iniPopup() {

        popExclude = new Dialog(getContext());
        popExclude.setContentView(R.layout.popup_exclude_account);
        popExclude.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popExclude.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popExclude.getWindow().getAttributes().gravity = Gravity.BOTTOM;

        popupTexto1 = popExclude.findViewById(R.id.popup_excluirConta_Texto1);
        popupTexto2 = popExclude.findViewById(R.id.popup_excluirConta_Texto2);
        popup_btnExcluir = popExclude.findViewById(R.id.popup_btnExcluir);
        popup_btnCancelar = popExclude.findViewById(R.id.popup_btnCancelar);

        popup_btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirUsuario();
            }
        });

        popup_btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popExclude.cancel();
            }
        });


    }


    private void iniPopupSenha() {

        popChange = new Dialog((getContext()));
        popChange.setContentView(R.layout.popup_change_password);
        popChange.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popChange.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popChange.getWindow().getAttributes().gravity = Gravity.BOTTOM;

        senha1 = popChange.findViewById(R.id.etMudarSenha1);

        senha2 = popChange.findViewById(R.id.etMudarSenha2);

        btnChangePass = popChange.findViewById(R.id.btnAlterarSenha);
        btnCancelPass = popChange.findViewById(R.id.btnCancelarSenha);


        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mudarSenha(senha1.getText().toString(), senha2.getText().toString());


            }
        });

        btnCancelPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popChange.cancel();
            }
        });
    }


    private void verificaUser() {
        if (user != null) {
            email = getView().findViewById(R.id.txt_email_perfil);
            name = getView().findViewById(R.id.txt_nome_perfil);


            email.setText(user.getEmail());
            name.setText(user.getDisplayName());

        }
    }
}
