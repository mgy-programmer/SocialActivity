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
public class PostClass extends ArrayAdapter<String> {

    private final ArrayList<String> userBaslik;
    private final ArrayList<String> userImage;
    private final ArrayList<String> userTarih;
    private final Activity context;


    public PostClass(ArrayList<String> userBaslik, ArrayList<String> userImage, ArrayList<String> userTarih, Activity context) {
        super(context, R.layout.costum_view,userBaslik);
        this.userBaslik = userBaslik;
        this.userImage = userImage;
        this.userTarih = userTarih;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.costum_view,null,true);

        TextView useremailText = customView.findViewById(R.id.txt_baslik);
        TextView commentText = customView.findViewById(R.id.txt_tarih);
        ImageView imageView = customView.findViewById(R.id.image_avatar);

        useremailText.setText(userBaslik.get(position));
        commentText.setText(userTarih.get(position));
        Picasso.get().load(userImage.get(position)).into(imageView);

        return customView;
    }
}