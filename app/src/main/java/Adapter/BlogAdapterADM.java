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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
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
import Class.*;


public class BlogAdapterADM extends RecyclerView.Adapter<BlogAdapterADM.ViewHolder> {

    private List<Blog> mBlogList;
    private Context context;
    private DatabaseReference referenciaFirebase;
    private List<Blog> blogs;
    private Blog todosPosts;
    Dialog popAcessarPost ;
    Button popupAcessar, popupCancelar;
    Dialog popAddPost ;
    TextView popupTitle,popupDescription,popupLink,popupAutor;
    Button popupAddBtn, popup_cancel;
    ImageView popupPostImage;

    public BlogAdapterADM(List<Blog> l, Context c){
        context = c;
        mBlogList = l;
    }




    @NonNull
    @Override
    public BlogAdapterADM.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_blog_adm, viewGroup, false);

            // ini popup
            iniPopup();

        return new BlogAdapterADM.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final BlogAdapterADM.ViewHolder holder, int position) {


        final String idPostBlog = mBlogList.get(position).getKeyPost();

        final Blog item = mBlogList.get(position);
        final String nomePost = mBlogList.get(position).getNome();


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

        final String titulo = mBlogList.get(position).getNome();
        final String descricao = mBlogList.get(position).getDescricao();
        final String link = mBlogList.get(position).getLink();
        final String autor = mBlogList.get(position).getAutor();
       final String postId = mBlogList.get(position).getKeyPost();

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
        referenciaFirebase  = Conexao.getFirebase();
        holder.btnMenuPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btnMenuPost);
                popupMenu.inflate(R.menu.options_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu1) {

                            referenciaFirebase.child("post").child(idPostBlog).removeValue().equals(idPostBlog);
                            ToastCurto("O Post " + nomePost + " foi excluido");
                        }else{
                            iniPopupEDIT(titulo, descricao,link,autor,postId);
                            popAddPost.show();


                        }
                        return false;
                    }
                });

                popupMenu.show();


            }
        });
    }


    private void iniPopupEDIT(String titulo, String descricao, String link, String autor,final String postId){

        popAddPost = new Dialog(context);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.BOTTOM;


        // EditText
        popupLink = popAddPost.findViewById(R.id.popup_Link);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAutor = popAddPost.findViewById(R.id.popup_autor);

        // Botões
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popup_cancel = popAddPost.findViewById(R.id.popup_cancel);

        popup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.cancel();
            }
        });

        popupTitle.setText(titulo);
        popupDescription.setText(descricao);
        popupLink.setText(link);
        popupAutor.setText(autor);

        popupAddBtn.setText("Editar");

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenciaFirebase = Conexao.getFirebase();

                referenciaFirebase.child("post").child(postId).child("autor").setValue(popupAutor.getText().toString());
                referenciaFirebase.child("post").child(postId).child("descricao").setValue(popupDescription.getText().toString());
                referenciaFirebase.child("post").child(postId).child("link").setValue(popupLink.getText().toString());
                referenciaFirebase.child("post").child(postId).child("nome").setValue(popupTitle.getText().toString());
                popAddPost.cancel();

            }
        });





    }


    @Override
    public int getItemCount() {

        return mBlogList.size();

    }

    public void ToastCurto(String a) {
        Toast.makeText(context, a, Toast.LENGTH_SHORT).show();

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
        protected ImageView fotoPost, btnMenuPost;
        protected CardView linearLayout;
        protected ConstraintLayout constraintLayout;
        protected ImageView btnSharePost;





        public ViewHolder (View itemView){
            super(itemView);

            txtNome = itemView.findViewById(R.id.txt_Nome_Post);
            txtDescricao = itemView.findViewById(R.id.placeholderDescricao);
            txtAutor = itemView.findViewById(R.id.placeholderAutor);
            txtData = itemView.findViewById(R.id.dataholder);
            btnMenuPost= itemView.findViewById(R.id.btnMenuPost);
            btnSharePost = itemView.findViewById(R.id.btn_sharePost);

            fotoPost = itemView.findViewById(R.id.imagePost);
            linearLayout = itemView.findViewById(R.id.cardViewLista);
            constraintLayout = itemView.findViewById(R.id.layoutLista);


        }
    }


}
