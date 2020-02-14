package com.example.muhammed.socialactivityproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AktivityAciklama extends AppCompatActivity {

    TextView txt_TarihA;
    TextView txt_BaslikA;
    TextView txt_AciklamaA;
    TextView txt_KategoriA;
    ImageView image_activity;
    Button btn_katil;
    String Anahter;
    FirebaseDatabase db;
    DatabaseReference reference;
    PostAciklama adapter;
    private Query productQuery;

    ArrayList<String> userBaslikFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> userAciklamaFB;
    ArrayList<String> userTarihFromFB;
    ArrayList<String> userKategoriFromFB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivity_aciklama);

        txt_TarihA= findViewById(R.id.txt_tarihA);
        txt_BaslikA=findViewById(R.id.txt_BaslikA);
        txt_AciklamaA=findViewById(R.id.txt_AciklamaA);
        txt_KategoriA=findViewById(R.id.txt_kategoriA);
        image_activity=findViewById(R.id.image_activity);
        btn_katil=findViewById(R.id.btn_katil);

        final Bundle anahtar=getIntent().getExtras();
        Anahter=anahtar.getString("Anahtar");

        adapter = new PostAciklama(userBaslikFromFB,userimageFromFB,userAciklamaFB,userTarihFromFB,userKategoriFromFB,this);

        btn_katil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Katıldın",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),User_preference.class);
                startActivity(intent);
            }
        });



    }

}
//    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
//    userBaslikFromFB.add(hashMap.get("Baslik"));
//    userimageFromFB.add(hashMap.get("DownloadUrl"));
//    userAciklamaFB.add(hashMap.get("Aciklama"));
//    userTarihFromFB.add(hashMap.get("Tarih"));
//    userKategoriFromFB.add(hashMap.get("Preference"));
//    adapter.notifyDataSetChanged();