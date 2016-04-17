package anshul.org.quintypetask.Network;


import anshul.org.quintypetask.Model.MovieDetails;
import anshul.org.quintypetask.Model.MovieList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/")
    Call<MovieList> getMovieList(@Query("page") int page, @Query("s") String title);


    @GET("/")
    Call<MovieDetails> getMovieDetails(@Query("i") String imdbId, @Query("plot") String plot);


}
