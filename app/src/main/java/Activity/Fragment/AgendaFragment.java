package Activity.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orgwork.renewed.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import Adapter.AgendaAdapter;
import Class.Agenda;
import Class.Conexao;

public class AgendaFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView ivBtnAlarme;
    Dialog popAddPost;
    FloatingActionButton btnNovoPostAgenda;
    private LinearLayoutManager mLayoutManager;
    Button btnCancelarPostAgenda, btnAddPostAgenda;
    EditText etDataAgenda, etTituloAddAgenda, etDescricaoAgenda;
    Calendar calendario;
    DatePickerDialog dpd;


    private Agenda todasAgendas;
    RecyclerView recyclerView;
    AgendaAdapter agendaAdapter;
    List<Agenda> agendas;


    String uid;
    private Agenda agenda;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);


        iniPopup();

        agenda = new Agenda();

        recyclerView = view.findViewById(R.id.agendaRV);


        ivBtnAlarme = view.findViewById(R.id.ivBtnAlarme);
        carregarAgenda();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();

        btnNovoPostAgenda = view.findViewById(R.id.fabAddPost);


        btnNovoPostAgenda.setOnClickListener(v -> popAddPost.show());


        return view;
    }



    public void carregarAgenda() {

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        agendas = new ArrayList<>();


        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();




        databaseReference.child("Agenda").child(uid).orderByChild("dataEntrega").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                agendas.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    todasAgendas = postSnapshot.getValue(Agenda.class);

                    agendas.add(todasAgendas);


                }

                agendaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        agendaAdapter = new AgendaAdapter(getActivity(), agendas);

        recyclerView.setAdapter(agendaAdapter);

        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


    }


    @Override
    public void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();


    }

    private void iniPopup() {

        popAddPost = new Dialog(Objects.requireNonNull(getContext()));
        popAddPost.setContentView(R.layout.popup_add_agenda);
        Objects.requireNonNull(popAddPost.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.BOTTOM;

        etTituloAddAgenda = popAddPost.findViewById(R.id.etTituloAddAgenda);
        etDataAgenda = popAddPost.findViewById(R.id.etDataAgenda);
        etDescricaoAgenda = popAddPost.findViewById(R.id.etDescricaoAgenda);

        btnCancelarPostAgenda = popAddPost.findViewById(R.id.btnCancelarPostAgenda);

        btnAddPostAgenda = popAddPost.findViewById(R.id.btnCriarPostAgenda);


        btnAddPostAgenda.setOnClickListener(v -> {

            if (!etTituloAddAgenda.getText().toString().isEmpty() && !etDataAgenda.getText().toString().isEmpty() && !etDescricaoAgenda.getText().toString().isEmpty()) {

                novaAgenda(etTituloAddAgenda.getText().toString(), etDescricaoAgenda.getText().toString(), etDataAgenda.getText().toString());

            } else {
                ToastCurto("Todos os campos devem ser preenchidos !");
            }


        });


        btnCancelarPostAgenda.setOnClickListener(v -> popAddPost.cancel());

        etDataAgenda.setOnClickListener(v -> {

            calendario = Calendar.getInstance();
            int day = calendario.get(Calendar.DAY_OF_MONTH);
            int month = calendario.get(Calendar.MONTH);
            int year = calendario.get(Calendar.YEAR);


            dpd = new DatePickerDialog(Objects.requireNonNull(getContext()), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                    etDataAgenda.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                }
            }, day, month, year);
            dpd.getDatePicker().setMinDate(System.currentTimeMillis());
            dpd.show();

        });


    }


    public void ToastCurto(String a) {
        Toast.makeText(getActivity(), a, Toast.LENGTH_SHORT).show();
    }

    public void novaAgenda(String titulo, String texto, String data) {

        String key = databaseReference.child("Agenda").child("key").push().getKey();


        agenda.setAgendaKey(key);
        agenda.setNomeAtividade(titulo);
        agenda.setTextoAtividade(texto);
        agenda.setDataEntrega(data);
        agenda.setStatusAtividade("ativada");
        agenda.setAutorAtividade(user.getEmail());


        assert key != null;
        databaseReference.child("Agenda").child(user.getUid()).child(key).setValue(agenda).addOnSuccessListener(aVoid -> {
            etTituloAddAgenda.setText(null);
            etDescricaoAgenda.setText(null);
            etDataAgenda.setText(null);
            popAddPost.dismiss();
        });


    }

}
