package helo.mali.movies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import helo.mali.movies.model.Movie;
import helo.mali.movies.utilities.MoviesJsonUtils;
import helo.mali.movies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView moviesRecyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieAdapter = new MovieAdapter();

        moviesRecyclerView = findViewById(R.id.movies_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setAdapter(movieAdapter);

        URL url = NetworkUtils.buildUrl("popularity.desc");

        new GetMoviesTask().execute(url);
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

            try {
                List<Movie> movies = MoviesJsonUtils.extractMovies(response);
                movieAdapter.setMovies(movies);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.v(TAG, "Response " + response);
        }
    }
}
