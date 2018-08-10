package helo.mali.movies.model;

public class Movie {
    private long id;
    private String title;
    private String originalTitle;
    private String overview;
    private String posterPath;
    private double rating;
    private String releaseDate;

    public Movie(long id, String title, String originalTitle, String overview, String posterPath, double rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }
}
