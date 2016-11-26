package com.example.android.popularmoviesapp;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by javie on 28/09/2016.
 */

public class AppAdapter extends ArrayAdapter<Movie> {

    public AppAdapter(Activity context, ArrayList<Movie> movies) {
        // Initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // The adapter is not going to use this second argument, so it can be any value. Here, I used 0.
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        // Get the {@link Movie} object located at this position in the list
        Movie currentMovie = getItem(position);

        //ViewHolder Pattern
        ViewHolder holder = new ViewHolder();

        if (holder.poster == null) {
            //Set the poster imageView
            holder.poster = (ImageView) listItemView.findViewById(R.id.photo_image_view);
        }

        Picasso.with(getContext()).load(currentMovie.getPosterPath()).into(holder.poster);

        //Set the OnTouch listener for visual feedback on the poster
        holder.poster.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        if (view.getDrawable() != null) {
                            view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            view.invalidate();
                        }
                        return false;
                    }
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay

                        view.getDrawable().clearColorFilter();
                        view.invalidate();

                        return true;
                    }
                }

                return false;
            }
        });


        // Return the whole list item layout
        // so that it can be shown in the ListView or GridView
        return listItemView;
    }

    private static class ViewHolder {
        ImageView poster;
    }
}
