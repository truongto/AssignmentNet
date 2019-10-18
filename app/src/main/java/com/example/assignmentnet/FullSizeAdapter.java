package com.example.assignmentnet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.assignmentnet.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class FullSizeAdapter extends PagerAdapter {
    Context context;
    List<Photo> photoList;
    LayoutInflater layoutInflater;

    public FullSizeAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.full_image_item,null);
        PhotoView image = view.findViewById(R.id.imgFullSize);
        Picasso.get().load(photoList.get(position).getUrlL()).into(image);
        ViewPager viewPager = (ViewPager)container;
        viewPager.addView(view,0);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager)container;
        View view = (View)object;
        viewPager.removeView(view);
    }


}
