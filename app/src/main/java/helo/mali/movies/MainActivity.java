package helo.mali.movies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import helo.mali.movies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL url = NetworkUtils.buildUrl("popularity.desc");

        new GetMoviesTask().execute(url);
    }

    private static class GetMoviesTask extends AsyncTask<URL, Void, String>{
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG, "Response " + s);
        }
    }

    //https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=1afe9082c06f2e12037f4958f570ee76
    //https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=1afe9082c06f2e12037f4958f570ee76
}
