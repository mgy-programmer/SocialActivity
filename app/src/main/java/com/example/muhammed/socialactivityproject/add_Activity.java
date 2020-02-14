package com.example.muhammed.socialactivityproject;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class add_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{

    ImageView image_select;
    EditText edit_baslik;
    EditText edit_aciklama;
    Button btn_tarihSec;
    Button btn_kaydet;
    TextView txt_tarih;
    TextView txt_tercih;
    Spinner spinner_tercih;
    String preference;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    String currentDate;
    Uri selected;


    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_);

        image_select=findViewById(R.id.image_select);
        edit_baslik=findViewById(R.id.edit_baslik);
        edit_aciklama=findViewById(R.id.edit_aciklama);
        btn_kaydet=findViewById(R.id.btn_kaydet);
        btn_tarihSec=findViewById(R.id.btn_tarihSec);
        txt_tarih=findViewById(R.id.txt_tarih);
        txt_tercih=findViewById(R.id.txt_tercih);
        spinner_tercih=findViewById(R.id.spinner_tercih);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        ArrayAdapter arrayAdapter=ArrayAdapter.createFromResource(this, R.array.preferences,R.layout.support_simple_spinner_dropdown_item);



        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tercih.setAdapter(arrayAdapter);

        spinner_tercih.setOnItemSelectedListener(this);

        btn_tarihSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");

            }
        });

        btn_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UUID uuidResim=UUID.randomUUID();
                String resim_name="activityImages/" + uuidResim + ".jpg";
                StorageReference storageReference=mStorageRef.child(resim_name);
                storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl=taskSnapshot.getDownloadUrl().toString();
                        String baslik=edit_baslik.getText().toString();
                        String aciklama=edit_aciklama.getText().toString();
                        String izin=txt_tercih.getText().toString();
                        String tarih=currentDate;

                        UUID uuid=UUID.randomUUID();
                        String uuid_string=uuid.toString();
                        myRef.child("Activity").child(uuid_string).child("Baslik").setValue(baslik);
                        myRef.child("Activity").child(uuid_string).child("Aciklama").setValue(aciklama);
                        myRef.child("Activity").child(uuid_string).child("DownloadUrl").setValue(downloadUrl);
                        myRef.child("Activity").child(uuid_string).child("Preference").setValue(izin);
                        myRef.child("Activity").child(uuid_string).child("Tarih").setValue(tarih);

                        Toast.makeText(getApplicationContext(),"Başarılı",Toast.LENGTH_LONG).show();
                        setNotification(baslik);

                        Intent intent=new Intent(getApplicationContext(),User_preference.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }
    public void setNotification(String Baslik){
        String CHANNEL_ID = "channel_id";
        String CHANNEL_NAME = "channel_activity";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(getBaseContext(),CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentText("SocialActivity Yeni Bildirim");
        mBuilder.setContentTitle(Baslik);

        Intent intent=new Intent(getApplicationContext(),User_preference.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(getBaseContext());
        stackBuilder.addParentStack(User_preference.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random=new Random();
        notificationManager.notify(random.nextInt(1300000),mBuilder.build());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        txt_tarih.setText(currentDate);
    }

    public  void select_image(View view){
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
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
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
                image_select.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        preference=spinner_tercih.getSelectedItem().toString();
        txt_tercih.setText(preference);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
