package com.example.dgunda.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dgunda on 10/11/15.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotoAdapter(Context context,  List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InstagramPhoto photo = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);
        }
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikes = (TextView)convertView.findViewById(R.id.tvLikes);
        TextView tvComment1 = (TextView)convertView.findViewById(R.id.tvComment1);
        TextView tvComment2 = (TextView)convertView.findViewById(R.id.tvComment2);
        TextView tvProfileName = (TextView)convertView.findViewById(R.id.tvProfileName);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);


        tvCaption.setText(photo.caption +"  Caption");
        tvLikes.setText(String.valueOf(photo.likesCount+" Likes"));
        tvComment1.setText(photo.comment1);
        tvComment2.setText(photo.comment2);
        tvProfileName.setText(photo.username);
        ivPhoto.setImageResource(0);
        ivProfileImage.setImageResource(0);

        Picasso.with(getContext()).load(photo.profileimageUrl).placeholder(R.drawable.profile).into(ivProfileImage);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.placeholder).into(ivPhoto);
        return convertView;
    }
}
