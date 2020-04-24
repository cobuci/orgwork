package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orgwork.renewed.R;

import java.util.ArrayList;
import java.util.List;

import Class.Blog;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private List<Blog> mBlogList;
    private Context context;
    private DatabaseReference referenciaFirebase;
    private List<Blog> blogs;
    private Blog todosPosts;

    public BlogAdapter (List<Blog> l, Context c){
        context = c;
        mBlogList = l;
    }

    @NonNull
    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_blog, viewGroup, false);

        return new BlogAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogAdapter.ViewHolder holder, int position) {

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



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.txtNome.setText(item.getNome());
        holder.txtDescricao.setText(item.getDescricao());


        holder.linearLayoutBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    @Override

    public int getItemCount() {

        return mBlogList.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        protected TextView txtNome;
        protected TextView txtDescricao;
        protected LinearLayout linearLayoutBlog;

        public ViewHolder (View itemView){
            super(itemView);

            txtNome = (TextView)itemView.findViewById(R.id.txt_Nome_Post);
            txtDescricao = (TextView)itemView.findViewById(R.id.txt_Descricao_Post);
            linearLayoutBlog = (LinearLayout)itemView.findViewById(R.id.linearLayoutBlog);

        }
    }
}
