package Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orgwork.renewed.R;

import java.util.ArrayList;
import java.util.List;

import Activity.NewPostActivity;
import Adapter.BlogAdapter;
import Class.Blog;
import Class.Conexao;
public class HomeFragmentADM extends Fragment {



    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button btnNovoPost;
    private RecyclerView mRecyclerView;
    private BlogAdapter adapter;
    private List<Blog> blogs;
    private DatabaseReference referenciaFirebase;
    private Blog todosPosts;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home_adm, container, false);

        // botao new post
        btnNovoPost = view.findViewById(R.id.btnNovoPost);

        btnNovoPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAdm = new Intent(getActivity(),
                        NewPostActivity.class);
                startActivity(iAdm);
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });


        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewBlog);

        carregarPosts();

        return view;
    }

    private void carregarPosts() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        blogs = new ArrayList<>();

        referenciaFirebase = FirebaseDatabase.getInstance().getReference();


        referenciaFirebase.child("post").orderByChild("keyPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    todosPosts = postSnapshot.getValue(Blog.class);

                    blogs.add(todosPosts);


                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new BlogAdapter(blogs, getContext());

        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();

    }
}


