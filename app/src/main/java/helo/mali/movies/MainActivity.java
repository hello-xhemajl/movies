package helo.mali.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import helo.mali.movies.model.Movie;
import helo.mali.movies.utilities.MoviesJsonUtils;
import helo.mali.movies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    /** Search tag could be 'most popular' or 'top rated' */
    private String moviePreference;
    private URL serchUrl;

    @BindView(R.id.movies_recycler_view)
    RecyclerView moviesRecyclerView;

    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movieAdapter = new MovieAdapter(this);

        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setAdapter(movieAdapter);

        setupSharedPreferences();
    }

    @Override
    public void onClick(long movieId) {
        // Show details of the selected movie
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, movieId);
        startActivity(intent);

        Log.v(TAG, "Id is " + movieId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings){
            // Start settings so user can change her preferences on weather to show
            // 'top rated' or 'most popular" moview
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadMovieBySettings(sharedPreferences);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    // Take the value from settings
    private void loadMovieBySettings(SharedPreferences sharedPreferences) {
        moviePreference = sharedPreferences.getString(getString(R.string.pref_tag_key),
                getString(R.string.pref_tag_most_popular_value));

        serchUrl = NetworkUtils.buildMoviesUrl(moviePreference);
        new GetMoviesTask().execute(serchUrl);

    }

    // The value of settings is changed ( search Tag)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_tag_key))) {
            loadMovieBySettings(sharedPreferences);
        }
    }

    private class GetMoviesTask extends AsyncTask<URL, Void, String>{
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

            if(response == null)
                Toast.makeText(MainActivity.this, R.string.api_error, Toast.LENGTH_LONG).show();

            try {
                List<Movie> movies = MoviesJsonUtils.extractMovies(response);
                movieAdapter.setMovies(movies);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }


            Log.v(TAG, "Response " + response);
        }
    }
}
