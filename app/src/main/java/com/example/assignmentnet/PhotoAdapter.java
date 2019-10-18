package com.example.assignmentnet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmentnet.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
    Context context;
    ArrayList<Photo> photoList;

    public PhotoAdapter(Context context, ArrayList<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }



    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoHolder photoHolder, final int position) {
        photoHolder.photo = photoList.get(position);
        photoHolder.tvView.setText(photoHolder.photo.getViews());
        ConstraintSet constraintSet = new ConstraintSet();
        String imageRatio = String.format("%d:%d",photoHolder.photo.getWidthL(),photoHolder.photo.getHeightL());
        photoHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(),ImageActivity.class);
                intent.putExtra("LIST",photoList);
                intent.putExtra("POSITION",position);
                intent.putExtra("UrlHigh",photoHolder.photo.getUrlO());
                intent.putExtra("UrlMedium",photoHolder.photo.getUrlL());
                intent.putExtra("UrlLow",photoHolder.photo.getUrlM());
                context.startActivity(intent);
            }
        });
        String link = photoHolder.photo.getUrlL();
        Picasso.get().load(link).into(photoHolder.imageView);

        constraintSet.clone(photoHolder.mConstraintLayout);
        constraintSet.setDimensionRatio(photoHolder.imageView.getId(),imageRatio);
        constraintSet.applyTo(photoHolder.mConstraintLayout);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private Photo photo;
        private ConstraintLayout mConstraintLayout;
        private TextView tvView;
        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            mConstraintLayout = itemView.findViewById(R.id.parentConstraint);
            imageView = itemView.findViewById(R.id.imgList);
            tvView = itemView.findViewById(R.id.tvView);
        }
    }
}
