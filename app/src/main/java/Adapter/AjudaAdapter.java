package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orgwork.renewed.R;

import java.util.List;

import Activity.HelpDetailsActivity;
import Class.Ajuda;

public class AjudaAdapter extends RecyclerView.Adapter<AjudaAdapter.MyViewHolder> {


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

        holder.tvTipo.setText(mData.get(position).getTipo());
        holder.tvAutor.setText(mData.get(position).getAutor());
        holder.tvData.setText(mData.get(position).getTimestamp());

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTipo, tvAutor, tvData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvData = itemView.findViewById(R.id.tvData);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postDetailActivity = new Intent(mContext,HelpDetailsActivity.class);
                int position = getAdapterPosition();

                postDetailActivity.putExtra("tipo", mData.get(position).getTipo());
                postDetailActivity.putExtra("autor", mData.get(position).getAutor());
                postDetailActivity.putExtra("mensagem", mData.get(position).getMensagem());
                postDetailActivity.putExtra("emailAutor",mData.get(position).getEmailAutor());
                postDetailActivity.putExtra("timestamp",mData.get(position).getTimestamp());
                postDetailActivity.putExtra("keyPost",mData.get(position).getKeyPost());

                mContext.startActivity(postDetailActivity);

            }
        });



        }


    }


}
