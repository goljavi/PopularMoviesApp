package com.example.android.popularmoviesapp;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.Settings.System.DATE_FORMAT;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.vote_average)
    TextView voteAverage;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.release_date)
    TextView releaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        //We get the extra JSON serialized String from the intent and make it a Movie object again
        Movie movie = new Gson().fromJson(getIntent().getStringExtra("obj"), Movie.class);

        //Populate the Activity View
        //ImageView imageView = (ImageView) findViewById(R.id.image_view);
        Picasso.with(this).load(movie.getPosterPath()).into(imageView);

        //TextView title = (TextView) findViewById(R.id.title);
        title.setText(movie.getTitle());
        setTitle(movie.getTitle());

        //TextView description = (TextView) findViewById(R.id.description);
        description.setText(movie.getOverview());

        //Got some trouble with this one so i took an extra check
        if (movie.getReleaseDate() != null) {
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = originalFormat.parse(movie.getReleaseDate());
            } catch (ParseException ex) {
                // Handle Exception.
            }
            if (date != null) {
                releaseDate.setText(targetFormat.format(date));
            }
        }

        //TextView voteAverage = (TextView) findViewById(R.id.vote_average);
        voteAverage.setText(getString(R.string.vote_average) + " " + String.valueOf(movie.getVoteAverage()));


    }
}
