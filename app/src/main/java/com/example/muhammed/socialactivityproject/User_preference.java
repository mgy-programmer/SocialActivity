package com.example.muhammed.socialactivityproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class User_preference extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    GridView grid_preferences;
    FirebaseDatabase db;
    TextView textView;

    ArrayList<String> userIDFromFB;
    ArrayList<String> userBaslikFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> userTarihFromFB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    PostClass adapter;
    String Anahtar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_activity){
            Intent intent=new Intent(getApplicationContext(),add_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preference);

        spinner =findViewById(R.id.preference_spinner);
        grid_preferences=findViewById(R.id.grid_preferences);

        ArrayAdapter arrayAdapter=ArrayAdapter.createFromResource(this, R.array.preferences,R.layout.support_simple_spinner_dropdown_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

        userBaslikFromFB= new ArrayList<String>();
        userTarihFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();


        adapter = new PostClass(userBaslikFromFB,userimageFromFB,userTarihFromFB,this);


        grid_preferences.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Anahtar="f38a5701-154f-4feb-a5cb-747daca7f216";
                Intent intent=new Intent(getApplicationContext(),AktivityAciklama.class);
                Toast.makeText(getApplicationContext(),Anahtar, Toast.LENGTH_LONG).show();
                intent.putExtra("Anahtar",Anahtar);
                startActivity(intent);
            }
        });

        grid_preferences.setAdapter(adapter);
        getDataFromFirebase();

    }

    protected void getDataFromFirebase() {

        final DatabaseReference newReference = firebaseDatabase.getReference("Activity");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    userBaslikFromFB.add(hashMap.get("Baslik"));
                    userimageFromFB.add(hashMap.get("DownloadUrl"));
                    userTarihFromFB.add(hashMap.get("Tarih"));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        textView=(TextView) view;
        Toast.makeText(getApplicationContext(),textView.getText(),Toast.LENGTH_LONG).show();
        //fetchProducts();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    DatabaseReference reference;
//    private void fetchProducts() {
//        String kategori=textView.getText().toString();
//        reference.child("Activity").orderByChild("Preference").equalTo(kategori).addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                            HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
//                            userBaslikFromFB.add(hashMap.get("Baslik"));
//                            userimageFromFB.add(hashMap.get("DownloadUrl"));
//                            userTarihFromFB.add(hashMap.get("Tarih"));
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                }
//        );
//    }
}
