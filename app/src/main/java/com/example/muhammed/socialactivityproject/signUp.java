package com.example.muhammed.socialactivityproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class signUp extends AppCompatActivity {

    EditText edit_adSoyad;
    EditText edit_kullanici;
    EditText edit_email;
    EditText edit_sifre;
    Button btn_geri;
    Button btn_kaydol;
    ImageView image_avatar;
    private FirebaseAuth mAuth;
    Uri selected;
    private StorageReference mStorageRef;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        edit_adSoyad=findViewById(R.id.edit_adSoyad);
        edit_kullanici=findViewById(R.id.edit_kullanici);
        edit_email=findViewById(R.id.edit_email);
        edit_sifre=findViewById(R.id.edit_sifre);
        btn_geri=findViewById(R.id.btn_geri);
        btn_kaydol=findViewById(R.id.btn_kaydol);
        image_avatar =findViewById(R.id.image_avatar);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        btn_kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kayit();
                UserInfo();
            }
        });

        btn_geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void selectImage(View view){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else {
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            selected=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selected);
                image_avatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Kayit(){
        mAuth.createUserWithEmailAndPassword(edit_email.getText().toString(),edit_sifre.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Kullanıcı Oluşturuldu",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("email",edit_email.getText().toString());
                            intent.putExtra("sifre",edit_sifre.getText().toString());
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void UserInfo(){
        UUID uuidAvatar=UUID.randomUUID();
        String avatar_name="avatar/" + uuidAvatar + ".jpg";
        StorageReference storageReference=mStorageRef.child(avatar_name);
        storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloadUrl=taskSnapshot.getDownloadUrl().toString();
                //FirebaseUser user=mAuth.getCurrentUser();
                //String email=user.getEmail().toString();

                String adi=edit_adSoyad.getText().toString();
                String kullanici=edit_kullanici.getText().toString();
                String email=edit_email.getText().toString();
                String sifre=edit_sifre.getText().toString();

                UUID uuid=UUID.randomUUID();
                String uuid_string=uuid.toString();
                myRef.child("User").child(uuid_string).child("Adi").setValue(adi);
                myRef.child("User").child(uuid_string).child("Kullanici").setValue(kullanici);
                myRef.child("User").child(uuid_string).child("Email").setValue(email);
                myRef.child("User").child(uuid_string).child("Sifre").setValue(sifre);
                myRef.child("User").child(uuid_string).child("DownloadUrl").setValue(downloadUrl);

                Toast.makeText(getApplicationContext(),"Başarılı",Toast.LENGTH_LONG).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
