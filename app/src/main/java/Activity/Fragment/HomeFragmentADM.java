package Activity.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orgwork.renewed.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapter.BlogAdapter;
import Class.Blog;
import Class.Conexao;

import static android.app.Activity.RESULT_OK;

public class HomeFragmentADM extends Fragment {


    private ShimmerFrameLayout shimmerFrameLayout;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FloatingActionButton btnNovoPost;
    private RecyclerView mRecyclerView;
    private BlogAdapter adapter;
    private List<Blog> blogs;
    private DatabaseReference referenciaFirebase;
    private Blog todosPosts;
    private LinearLayoutManager mLayoutManager;

    Dialog popAddPost ;
    ImageView popupPostImage,popupAddBtn;
    TextView popupTitle,popupDescription,popupLink;
    ProgressBar popupClickProgress;

    private Uri pickedImgUri = null;
    static int PReqCode = 2;
    static int REQUESCODE = 2;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home_adm, container, false);


        shimmerFrameLayout = view.findViewById(R.id.layout_shimmer_homeAdm);
        shimmerFrameLayout.startShimmer();

        // ini popup
        iniPopup();
        setupPopupImageClick();

        int id = getResources().getIdentifier("com.orgwork.renewed:drawable/ic_photo", null, null);

        popupPostImage.setImageResource(id);

        // botao new post
        btnNovoPost = view.findViewById(R.id.btnNovoPost);


        btnNovoPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.show();
            }
        });


        mRecyclerView = view.findViewById(R.id.recyclerViewBlog);


        carregarPosts();

        return view;
    }




    private void setupPopupImageClick() {


        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkAndRequestForPermission();


            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            popupPostImage.setImageURI(pickedImgUri);

        }


    }



    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);

    }


    private void checkAndRequestForPermission(){

        if(ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else {
                ActivityCompat.requestPermissions( getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }else{
            openGallery();
        }

    }

    private void iniPopup(){

        popAddPost = new Dialog(getContext());
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        popupLink = popAddPost.findViewById(R.id.popup_Link);
        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);


        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                if (!popupTitle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && pickedImgUri != null ) {

                                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                                final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                                imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageDownlaodLink = uri.toString();


                                                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                                                Date data = new Date();
                                                String dataFormatada = formataData.format(data);

                                                Blog blog = new Blog(popupTitle.getText().toString(),
                                                        popupDescription.getText().toString(),
                                                        dataFormatada,
                                                        imageDownlaodLink
                                                        );

                                                addPost(blog);



                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                popupClickProgress.setVisibility(View.INVISIBLE);
                                                popupAddBtn.setVisibility(View.VISIBLE);



                                            }
                                        });


                                    }
                                });



                }
                else {

                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);

                }



            }
        });



    }


    private void addPost(Blog blog) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("post").push();

        String link = popupLink.getText().toString().toLowerCase();

        if (!link.startsWith("http://") && !link.startsWith("https://")){
            link = "http://" + link;
        }


        String key = myRef.getKey();
        blog.setKeyPost(key);
        blog.setLink(link);
        blog.setAutor(user.getDisplayName());


        myRef.setValue(blog).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });





    }

    private void carregarPosts() {
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            blogs = new ArrayList<>();


            referenciaFirebase = FirebaseDatabase.getInstance().getReference();

            // Inverter Lista
            mLayoutManager.setStackFromEnd(true);
            mLayoutManager.setReverseLayout(true);

            referenciaFirebase.child("post").orderByChild("keyPost").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    blogs.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

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

        mRecyclerView.setItemViewCacheSize(30);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }


        @Override
        public void onStart() {
            super.onStart();
            auth = Conexao.getFirebaseAuth();
            user = Conexao.getFirebaseUser();

        }
    }


