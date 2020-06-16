package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.orgwork.renewed.R;

import java.util.List;

import Class.Agenda;
import Class.Conexao;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder> {

    Context mContext;
    List<Agenda> mData;
    String uid;
    private AlertDialog alerta;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public AgendaAdapter(Context mContext, List<Agenda> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.list_agenda, parent,false);

        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTituloAgenda.setText(mData.get(position).getNomeAtividade());
        holder.tvDescricaoAgenda.setText(mData.get(position).getTextoAtividade());
        holder.tvDataAgenda.setText(mData.get(position).getDataEntrega());

        final String titulo = mData.get(position).getNomeAtividade();
        final String descricao = mData.get(position).getTextoAtividade();
        final String dataDeEntrega = mData.get(position).getDataEntrega();
        final String idPostAgenda = mData.get(position).getAgendaKey();


        holder.ivBtnShareAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");


                    String shareMessage = "\nTitulo: " + titulo +
                            "\nDescrição: " + descricao +
                            "\nData de entrega: " + dataDeEntrega;

                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    mContext.startActivity(shareIntent);
                } catch (Exception e) {
                    //e.toString();
                }

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();


        holder.layoutAgenda.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle(titulo);


                builder.setNegativeButton("Apagar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        reference = Conexao.getFirebase();
                        reference.child("Agenda").child(uid).child(idPostAgenda).removeValue().equals(idPostAgenda);
                        ToastCurto("A tarefa " + titulo + " foi excluida");


                    }
                });

                alerta = builder.create();

                alerta.show();

                return false;
            }
        });

    }

    public void ToastCurto(String a) {
        Toast.makeText(mContext, a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        protected ConstraintLayout layoutAgenda;
        TextView tvTituloAgenda, tvDescricaoAgenda, tvDataAgenda;
        ImageView ivBtnShareAgenda;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutAgenda = itemView.findViewById(R.id.layoutListaAgenda);
            tvTituloAgenda = itemView.findViewById(R.id.tvTituloAgenda);
            tvDescricaoAgenda = itemView.findViewById(R.id.tvDescricaoAgenda);
            tvDataAgenda = itemView.findViewById(R.id.tvDataAgenda);
            ivBtnShareAgenda = itemView.findViewById(R.id.ivBtnShareAgenda);



        }
    }


}