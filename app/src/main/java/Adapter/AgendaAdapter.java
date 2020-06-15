package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orgwork.renewed.R;

import java.util.List;

import Class.Agenda;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder>{

    Context mContext;
    List<Agenda> mData;

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

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView tvTituloAgenda,tvDescricaoAgenda,tvDataAgenda;
        ImageView ivBtnShareAgenda;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTituloAgenda = itemView.findViewById(R.id.tvTituloAgenda);
            tvDescricaoAgenda = itemView.findViewById(R.id.tvDescricaoAgenda);
            tvDataAgenda = itemView.findViewById(R.id.tvDataAgenda);
            ivBtnShareAgenda = itemView.findViewById(R.id.ivBtnShareAgenda);



        }
    }



}