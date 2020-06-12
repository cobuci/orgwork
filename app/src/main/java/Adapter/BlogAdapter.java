package Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orgwork.renewed.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Class.Blog;


public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private List<Blog> mBlogList;
    private Context context;
    private DatabaseReference referenciaFirebase;
    private List<Blog> blogs;
    private Blog todosPosts;
    Dialog popAcessarPost ;
    Button popupAcessar, popupCancelar;



    public BlogAdapter (List<Blog> l, Context c){
        context = c;
        mBlogList = l;
    }




        @NonNull
    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_blog, viewGroup, false);

            // ini popup
            iniPopup();

        return new BlogAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final BlogAdapter.ViewHolder holder, int position) {



        final Blog item = mBlogList.get(position);


        blogs = new ArrayList<>();

        referenciaFirebase = FirebaseDatabase.getInstance().getReference();

        referenciaFirebase.child("post").orderByChild("keyPost").equalTo(item.getKeyPost()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                blogs.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todosPosts = postSnapshot.getValue(Blog.class);

                    blogs.add(todosPosts);

                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

                    final int height = (displayMetrics.heightPixels) / 4 ;
                    final int width = (displayMetrics.widthPixels) / 2 ;

                    Picasso.get().load(todosPosts.getPicture()).resize(width, height).centerCrop().into(holder.fotoPost);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.txtNome.setText(item.getNome());
        holder.txtDescricao.setText(item.getDescricao());
        holder.txtAutor.setText(item.getAutor());
        holder.txtData.setText(item.getTimestamp());

        holder.btnSharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");

                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, item.getNome());

                    String shareMessage= item.getNome() +"\n " +item.getLink();

                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    context.startActivity(shareIntent);
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        holder.fotoPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popAcessarPost.show();

                popupAcessar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(item.getLink()));
                        context.startActivity(i);

                        popAcessarPost.cancel();

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {

        return mBlogList.size();

    }

    private void iniPopup() {

        popAcessarPost = new Dialog(context);
        popAcessarPost.setContentView(R.layout.popup_acessar_post);
        popAcessarPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAcessarPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAcessarPost.getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL;


        popupAcessar = popAcessarPost.findViewById(R.id.popup_acessar_btnAcessar);
        popupCancelar = popAcessarPost.findViewById(R.id.popup_acessar_btnCancelar);

        popupCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popAcessarPost.cancel();
            }
        });



    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        protected TextView txtNome;
        protected TextView txtDescricao;
        protected TextView txtAutor;
        protected TextView txtData;
        protected ImageView fotoPost;
        protected CardView linearLayout;
        protected ConstraintLayout constraintLayout;
        protected ImageView btnSharePost;


        public ViewHolder (View itemView){
            super(itemView);

            txtNome = itemView.findViewById(R.id.txt_Nome_Post);
            txtDescricao = itemView.findViewById(R.id.placeholderDescricao);
            txtAutor = itemView.findViewById(R.id.placeholderAutor);
            txtData = itemView.findViewById(R.id.dataholder);

            btnSharePost = itemView.findViewById(R.id.btn_sharePost);

            fotoPost = itemView.findViewById(R.id.imagePost);
            linearLayout = itemView.findViewById(R.id.cardViewLista);
            constraintLayout = itemView.findViewById(R.id.layoutLista);


        }
    }


}
