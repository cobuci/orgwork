package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.orgwork.renewed.R;

import java.util.List;

import Activity.HelpDetailsActivity;
import Class.Ajuda;
import Class.*;

public class AjudaAdapter extends RecyclerView.Adapter<AjudaAdapter.MyViewHolder> {

    private DatabaseReference reference;
    Context mContext;
    List<Ajuda> mData ;

    public AjudaAdapter(Context mContext, List<Ajuda> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.list_help, parent, false);


        return new MyViewHolder(row);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String idPostAjuda = mData.get(position).getKeyPost();
        final String checarAjuda = mData.get(position).getStatusAjuda();

        if (checarAjuda.equals("Aberta")) {

            holder.cardViewListaAjuda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Aberta));

        } else if (checarAjuda.equals("Progresso")){
            holder.cardViewListaAjuda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Progresso));
        }else{
            holder.cardViewListaAjuda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Resolvida));
        }


        holder.tvTipo.setText(mData.get(position).getTipo());
        holder.tvAutor.setText(mData.get(position).getAutor());
        holder.tvData.setText(mData.get(position).getTimestamp());

        reference = Conexao.getFirebase();

        holder.menuHelp.setOnClickListener(view ->  {

            PopupMenu popupMenu = new PopupMenu(mContext, holder.menuHelp);

            popupMenu.inflate(R.menu.options_help);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu1) {

                    reference.child("ajuda").child(idPostAjuda).child("statusAjuda").setValue("Aberta");
                    ToastCurto(  "Definido como Em aberto");
                    holder.cardViewListaAjuda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Aberta));


                } else if(item.getItemId() == R.id.menu2)  {
                    reference.child("ajuda").child(idPostAjuda).child("statusAjuda").setValue("Progresso");
                    ToastCurto(  "Definido como Em progresso");

                    holder.cardViewListaAjuda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Progresso));

                }else{
                    reference.child("ajuda").child(idPostAjuda).child("statusAjuda").setValue("Resolvida");
                    ToastCurto(  "Definido como Resolvido");

                    holder.cardViewListaAjuda.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.Resolvida));

                }
                return false;
            });

            popupMenu.show();

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

        TextView tvTipo, tvAutor, tvData;
        ImageView menuHelp;
        ConstraintLayout layoutCardAjuda;
        CardView cardViewListaAjuda;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvData = itemView.findViewById(R.id.tvData);
            menuHelp = itemView.findViewById(R.id.menu_ajuda);
            layoutCardAjuda = itemView.findViewById(R.id.layoutCardAjuda);
            cardViewListaAjuda = itemView.findViewById(R.id.cardViewListaAjuda);
        itemView.setOnClickListener(v -> {
            Intent postDetailActivity = new Intent(mContext,HelpDetailsActivity.class);
            int position = getAdapterPosition();

            postDetailActivity.putExtra("tipo", mData.get(position).getTipo());
            postDetailActivity.putExtra("autor", mData.get(position).getAutor());
            postDetailActivity.putExtra("mensagem", mData.get(position).getMensagem());
            postDetailActivity.putExtra("emailAutor",mData.get(position).getEmailAutor());
            postDetailActivity.putExtra("timestamp",mData.get(position).getTimestamp());
            postDetailActivity.putExtra("keyPost",mData.get(position).getKeyPost());

            mContext.startActivity(postDetailActivity);

        });



        }


    }


}
