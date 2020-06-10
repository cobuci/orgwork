package Activity.Fragment;

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

import Adapter.AjudaAdapter;
import Class.Ajuda;
import Class.Conexao;

public class AjudaFragment extends Fragment {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button btnNovoPost;
    private RecyclerView mRecyclerView;
    private AjudaAdapter adapter;
    private List<Ajuda> ajudas;
    private DatabaseReference referenciaFirebase;
    private Ajuda todasAjudas;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_ajuda, container, false);



        mRecyclerView = view.findViewById(R.id.recyclerViewAjuda);

        carregarAjuda();

        return view;
    }


    private void carregarAjuda() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ajudas = new ArrayList<>();

        referenciaFirebase = FirebaseDatabase.getInstance().getReference();

        // Inverter Lista
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);

        referenciaFirebase.child("ajuda").orderByChild("keyPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    todasAjudas = postSnapshot.getValue(Ajuda.class);

                    ajudas.add(todasAjudas);


                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new AjudaAdapter(getActivity(), ajudas);

        mRecyclerView.setAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();

    }

}
