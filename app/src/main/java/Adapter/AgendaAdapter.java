package Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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


    Context mContext;
    List<Agenda> mData;
    String uid;

    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Dialog popEditPost;
    private Button btnCancelarPostAgenda, btnAddPostAgenda;
    private EditText etDataAgenda, etTituloAddAgenda, etDescricaoAgenda;
    private Calendar calendario;
    private DatePickerDialog dpd;

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

        holder.checkAtividade.setOnClickListener(view -> {

            if (!holder.checkAtividade.isChecked()) {

                if (statusAtividade.equals("desativada")) {

                    reference.child("Agenda").child(uid).child(idPostAgenda).child("statusAtividade").setValue("ativada");
                    holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Desativada));


                } else {
                    reference.child("Agenda").child(uid).child(idPostAgenda).child("statusAtividade").setValue("desativada");
                    holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Ativada));
                    ToastCurto("A atividade " + titulo + " foi concluida !");
                }
            } else {
                if (statusAtividade.equals("desativada")) {
                    reference.child("Agenda").child(uid).child(idPostAgenda).child("statusAtividade").setValue("ativada");
                    holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Desativada));

                } else {
                    reference.child("Agenda").child(uid).child(idPostAgenda).child("statusAtividade").setValue("desativada");
                    holder.cardAgenda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Ativada));
                    ToastCurto("A atividade " + titulo + " foi concluida !");
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
                } else {
                    iniPopup(titulo, dataDeEntrega, descricao, idPostAgenda);
                    popEditPost.show();

                }
                return false;
            });

            popupMenu.show();

        });
    }


    public void ToastCurto(String a) {
        Toast.makeText(mContext, a, Toast.LENGTH_SHORT).show();
    }


    private void iniPopup(String titulo, String data, String descricao, final String idPostAgenda) {

        popEditPost = new Dialog(Objects.requireNonNull(mContext));
        popEditPost.setContentView(R.layout.popup_add_agenda);
        Objects.requireNonNull(popEditPost.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popEditPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popEditPost.getWindow().getAttributes().gravity = Gravity.BOTTOM;


        // EDIT TEXT
        etTituloAddAgenda = popEditPost.findViewById(R.id.etTituloAddAgenda);
        etDataAgenda = popEditPost.findViewById(R.id.etDataAgenda);
        etDescricaoAgenda = popEditPost.findViewById(R.id.etDescricaoAgenda);

        //BOTÂO
        btnCancelarPostAgenda = popEditPost.findViewById(R.id.btnCancelarPostAgenda);
        btnAddPostAgenda = popEditPost.findViewById(R.id.btnCriarPostAgenda);

        btnAddPostAgenda.setText("Editar");

        etTituloAddAgenda.setText(titulo);
        etDataAgenda.setText(data);
        etDescricaoAgenda.setText(descricao);


        etDataAgenda.setOnClickListener(v -> {

            calendario = Calendar.getInstance();
            int day = calendario.get(Calendar.DAY_OF_MONTH);
            int month = calendario.get(Calendar.MONTH);
            int year = calendario.get(Calendar.YEAR);


            dpd = new DatePickerDialog(Objects.requireNonNull(mContext), (view, mYear, mMonth, mDay) -> etDataAgenda.setText(mDay + "/" + (mMonth + 1) + "/" + mYear), day, month, year);
            dpd.getDatePicker().setMinDate(System.currentTimeMillis());
            dpd.show();

        });

        btnAddPostAgenda.setOnClickListener(v -> {

            editarAgenda(idPostAgenda, etTituloAddAgenda.getText().toString(), etDescricaoAgenda.getText().toString(), etDataAgenda.getText().toString());

        });

        btnCancelarPostAgenda.setOnClickListener(view -> popEditPost.cancel());

    }


    private void editarAgenda(String idPostAgenda, String titulo, String texto, String data) {
        reference = Conexao.getFirebase();
        reference.child("Agenda").child(uid).child(idPostAgenda).child("nomeAtividade").setValue(titulo);
        reference.child("Agenda").child(uid).child(idPostAgenda).child("textoAtividade").setValue(texto);
        reference.child("Agenda").child(uid).child(idPostAgenda).child("dataEntrega").setValue(data);
        popEditPost.cancel();

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