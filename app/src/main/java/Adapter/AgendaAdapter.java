package Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.orgwork.renewed.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import Class.Agenda;
import Class.Conexao;


public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder> {


    private Agenda agenda;

    Context mContext;
    List<Agenda> mData;
    String uid;
    private AlertDialog alerta;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Dialog popAddPost;
    Button btnCancelarPostAgenda, btnAddPostAgenda;
    EditText etDataAgenda, etTituloAddAgenda, etDescricaoAgenda;
    Calendar calendario;

    public AgendaAdapter(Context mContext, List<Agenda> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.list_agenda, parent, false);


        return new MyViewHolder(row);

    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        holder.tvTituloAgenda.setText(mData.get(position).getNomeAtividade());
        holder.tvDescricaoAgenda.setText(mData.get(position).getTextoAtividade());
        holder.tvDataAgenda.setText(mData.get(position).getDataEntrega());


        final String titulo = mData.get(position).getNomeAtividade();
        final String descricao = mData.get(position).getTextoAtividade();
        final String dataDeEntrega = mData.get(position).getDataEntrega();
        final String idPostAgenda = mData.get(position).getAgendaKey();
        final String statusAtividade = mData.get(position).getStatusAtividade();

        if (statusAtividade.equals("desativada")) {

            holder.checkAtividade.setChecked(true);
            holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Desativada));

        } else {
            holder.checkAtividade.setChecked(false);
            holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Ativada));
        }


        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();


//        DateTimeFormatter parser = DateTimeFormatter.ofPattern("d/M/uuuu");
//        LocalDate data = LocalDate.parse(dataDeEntrega, parser);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/uuuu");
//        final String dataFormatada = formatter.format(data);
//
//        final String mes = String.valueOf(data.getMonthValue());
//        final String dia = String.valueOf(data.getDayOfMonth());
//        final String ano = String.valueOf(data.getYear());


        holder.checkAtividade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if (!holder.checkAtividade.isChecked()) {

                    if (statusAtividade.equals("desativada")) {

                        reference.child("Agenda").child(uid).child(idPostAgenda).child("statusAtividade").setValue("ativada");
                        holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Desativada));


                    } else {
                        reference.child("Agenda").child(uid).child(idPostAgenda).child("statusAtividade").setValue("desativada");
                        holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Ativada));
                        ToastCurto("A atividade " + titulo + " foi concluida !");
                    }
                } else{
                    if (statusAtividade.equals("desativada")) {
                        reference.child("Agenda").child(uid).child(idPostAgenda).child("statusAtividade").setValue("ativada");
                        holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Desativada));


                    } else {
                        reference.child("Agenda").child(uid).child(idPostAgenda).child("statusAtividade").setValue("desativada");
                        holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Ativada));
                        ToastCurto("A atividade " + titulo + " foi concluida !");
                    }
                }
            }
        });



        holder.ivBtnShareAgenda.setOnClickListener(v -> {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");


                String shareMessage = "Titulo: " + titulo +
                        "\nDescrição: " + descricao +
                        "\nData de entrega: " + dataDeEntrega;

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                mContext.startActivity(shareIntent);
            } catch (Exception e) {
                //e.toString();
            }

        });

        reference = Conexao.getFirebase();
        holder.ivBtnMenuAgenda.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(mContext, holder.ivBtnMenuAgenda);

            popupMenu.inflate(R.menu.options_menu);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu1) {

                    reference.child("Agenda").child(uid).child(idPostAgenda).removeValue().equals(idPostAgenda);
                    ToastCurto("A tarefa " + titulo + " foi excluida");
                }else{
                    iniPopup(titulo,dataDeEntrega,descricao,idPostAgenda);

                    popAddPost.show();

                }
                return false;
            });

            popupMenu.show();

        });


    }



    public void ToastCurto(String a) {
        Toast.makeText(mContext, a, Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("SetTextI18n")
    private void iniPopup(String titulo, String data, String descricao, final String idPostAgenda) {

        popAddPost = new Dialog(Objects.requireNonNull(mContext));
        popAddPost.setContentView(R.layout.popup_add_agenda);
        Objects.requireNonNull(popAddPost.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.BOTTOM;


        etTituloAddAgenda = popAddPost.findViewById(R.id.etTituloAddAgenda);
        etDataAgenda = popAddPost.findViewById(R.id.etDataAgenda);
        etDescricaoAgenda = popAddPost.findViewById(R.id.etDescricaoAgenda);
        btnCancelarPostAgenda = popAddPost.findViewById(R.id.btnCancelarPostAgenda);
        btnAddPostAgenda = popAddPost.findViewById(R.id.btnCriarPostAgenda);

        btnAddPostAgenda.setText("Editar");

        etTituloAddAgenda.setText(titulo);
        etDataAgenda.setText(data);
        etDescricaoAgenda.setText(descricao);

        btnAddPostAgenda.setOnClickListener(v -> {

            reference = Conexao.getFirebase();
            reference.child("Agenda").child(uid).child(idPostAgenda).child("nomeAtividade").setValue(etTituloAddAgenda.getText().toString());
            reference.child("Agenda").child(uid).child(idPostAgenda).child("textoAtividade").setValue(etDescricaoAgenda.getText().toString());
            reference.child("Agenda").child(uid).child(idPostAgenda).child("dataEntrega").setValue(etDataAgenda.getText().toString());

            popAddPost.cancel();
        });


    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected ConstraintLayout layoutAgenda;
        TextView tvTituloAgenda, tvDescricaoAgenda, tvDataAgenda;
        CardView cardAgenda;
        CheckBox checkAtividade;
        ImageView ivBtnShareAgenda, ivBtnMenuAgenda, ivBtnAlarme;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardAgenda = itemView.findViewById(R.id.cardAgenda);
            layoutAgenda = itemView.findViewById(R.id.layoutListaAgenda);
            tvTituloAgenda = itemView.findViewById(R.id.tvTituloAgenda);
            tvDescricaoAgenda = itemView.findViewById(R.id.tvDescricaoAgenda);
            tvDataAgenda = itemView.findViewById(R.id.tvDataAgenda);
            ivBtnShareAgenda = itemView.findViewById(R.id.ivBtnShareAgenda);
            ivBtnMenuAgenda = itemView.findViewById(R.id.ivBtnMenuAgenda);
            ivBtnAlarme = itemView.findViewById(R.id.ivBtnAlarme);
            checkAtividade = itemView.findViewById(R.id.checkAtividade);

        }
    }


}