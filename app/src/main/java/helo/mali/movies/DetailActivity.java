package helo.mali.movies;

import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import helo.mali.movies.model.Movie;
import helo.mali.movies.utilities.MoviesJsonUtils;
import helo.mali.movies.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE_ID = "movieId";

    @BindView(R.id.backdrop) ImageView backdropImageView;
    @BindView(R.id.overview_text_view) TextView overviewTextView;
    @BindView(R.id.user_rating_text_view) TextView userRatingTextView;
    @BindView(R.id.release_date_text_view) TextView releaseDateTextView;
    @BindView(R.id.original_title_text_view) TextView originalTitleTextView;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Long movieId = getIntent().getLongExtra(EXTRA_MOVIE_ID, 0);
        URL movieUrl = NetworkUtils.buildMovieDetailsUrl(movieId);

        new GetMovieTask().execute(movieUrl);

    }

    private class GetMovieTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                Movie movie = MoviesJsonUtils.extractMovie(new JSONObject(response));

                collapsingToolbar.setTitle(movie.getTitle());

                Picasso.get().load(NetworkUtils.buildPosterUrl(movie.getPosterPath()).toString())
                        .into(backdropImageView);

                overviewTextView.setText(movie.getOverview());
                originalTitleTextView.setText(movie.getOriginalTitle());
                userRatingTextView.setText(String.valueOf(movie.getRating()));
                releaseDateTextView.setText(movie.getReleaseDate());


            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.v(TAG, "Response " + response);
        }
    }
}
