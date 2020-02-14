package com.example.muhammed.socialactivityproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edit_Email;
    EditText edit_pass;
    Button btn_giris;
    TextView txt_kaydol;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        edit_Email=findViewById(R.id.edit_Email);
        edit_pass=findViewById(R.id.edit_pass);
        btn_giris=findViewById(R.id.btn_giris);
        txt_kaydol=findViewById(R.id.txt_kaydol);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            edit_Email.setText(extras.getString("email"));
            edit_pass.setText(extras.getString("sifre"));
        }

        btn_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Giris();
            }
        });

        txt_kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),signUp.class);
                startActivity(intent);
            }
        });
    }
    public void Giris(){
        mAuth.signInWithEmailAndPassword(edit_Email.getText().toString(),edit_pass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(getApplicationContext(),User_preference.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"Giriş Başarılı",Toast.LENGTH_LONG);
                            FirebaseUser user=mAuth.getCurrentUser();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
