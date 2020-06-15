package Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
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

import Adapter.BlogAdapter;
import Class.Blog;
import Class.Conexao;


public class HomeFragment extends Fragment {


    private FirebaseAuth auth;
    private FirebaseUser user;

    private ShimmerFrameLayout shimmerFrameLayout;

    private RecyclerView mRecyclerView;
    private BlogAdapter adapter;
    private List<Blog> blogs;
    private DatabaseReference referenciaFirebase;
    private Blog todosPosts;
    private LinearLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        shimmerFrameLayout = view.findViewById(R.id.layout_shimmer_homeAdm);
        shimmerFrameLayout.startShimmer();



        // Lista
        mRecyclerView = view.findViewById(R.id.recyclerViewBlog);

        carregarPosts();

        return view;
    }

    private void carregarPosts() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        blogs = new ArrayList<>();

        referenciaFirebase = FirebaseDatabase.getInstance().getReference();

        // Inverter Lista
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);

        referenciaFirebase.child("post").orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blogs.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    todosPosts = postSnapshot.getValue(Blog.class);

                    blogs.add(todosPosts);

                    mRecyclerView.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

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
