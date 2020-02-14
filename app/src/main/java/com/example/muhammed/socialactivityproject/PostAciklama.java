package com.example.muhammed.socialactivityproject;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;
public class PostAciklama extends ArrayAdapter<String> {

    private final ArrayList<String> userBaslik;
    private final ArrayList<String> userImage;
    private final ArrayList<String> userAciklama;
    private final ArrayList<String> userTarih;
    private final ArrayList<String> userKategori;
    private final Activity context;


    public PostAciklama(ArrayList<String> userBaslik, ArrayList<String> userImage, ArrayList<String> userAciklama, ArrayList<String> userTarih, ArrayList<String> userKategori, Activity context) {
        super(context, R.layout.costum_view,userBaslik);
        this.userBaslik = userBaslik;
        this.userImage = userImage;
        this.userAciklama = userAciklama;
        this.userTarih = userTarih;
        this.userKategori = userKategori;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.activity_aktivity_aciklama,null,true);

        TextView userBaslikText = customView.findViewById(R.id.txt_BaslikA);
        TextView AciklamaText = customView.findViewById(R.id.txt_AciklamaA);
        ImageView imageView = customView.findViewById(R.id.image_activity);
        TextView Tarih=customView.findViewById(R.id.txt_tarihA);
        TextView Kategori=customView.findViewById(R.id.txt_kategoriA);

        userBaslikText.setText(userBaslik.get(position));
        AciklamaText.setText(userTarih.get(position));
        Tarih.setText(userTarih.get(position));
        Kategori.setText(userKategori.get(position));
        Picasso.get().load(userImage.get(position)).into(imageView);

        return customView;
    }
}
