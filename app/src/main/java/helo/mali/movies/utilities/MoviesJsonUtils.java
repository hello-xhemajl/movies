package helo.mali.movies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import helo.mali.movies.model.Movie;

public class MoviesJsonUtils {

    /**
     * Extracts Json movies into a list {@link Movie} objects
     *
     * @param responseJson response from themoviedb.org
     * @return List of {@link Movie} objects
     * @throws JSONException
     */
    public static List<Movie> extractMovies(String responseJson) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        JSONObject responseObject = new JSONObject(responseJson);

        JSONArray movieArray = responseObject.getJSONArray("results");
        for(int i = 0; i < movieArray.length(); i++){
            JSONObject movieObject = movieArray.getJSONObject(i);
            Movie movie = extractMovie(movieObject);
            movies.add(movie);
        }

        return movies;
    }

    public static Movie extractMovie(JSONObject movieObject) throws JSONException {

        String title = movieObject.getString("title");
        String originalTitle = movieObject.getString("original_title");
        String overview = movieObject.getString("overview");
        String posterPath = movieObject.getString("poster_path");
        double rating = movieObject.getInt("vote_average");
        String releaseDate = movieObject.getString("release_date");
        long id = movieObject.getLong("id");

        Movie movie = new Movie(id, title, originalTitle, overview, posterPath, rating, releaseDate);

        return movie;
    }
}
