package anshul.org.quintypetask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;

import anshul.org.quintypetask.Model.MovieDetails;
import anshul.org.quintypetask.Model.MovieList;
import anshul.org.quintypetask.Network.ApiService;
import anshul.org.quintypetask.Network.RestClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends BlundellActivity {

    public ApiService prestigeApi = RestClient.getApiService();
    public Call<MovieDetails> call;
    @Bind(R.id.yearValue)
    TextView yearValue;
    @Bind(R.id.generValue)
    TextView generValue;
    @Bind(R.id.directorValue)
    TextView directorValue;
    @Bind(R.id.actorValue)
    TextView actorValue;
    @Bind(R.id.languageValue)
    TextView languageValue;
    @Bind(R.id.countryValue)
    TextView countryValue;
    @Bind(R.id.ratingValue)
    TextView ratingValue;
    @Bind(R.id.plotDescription)
    TextView plotDescription;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.posterImage)
    ImageView posterImage;
    @Bind(R.id.history)
    ImageView history;
    private MovieList.SearchEntity feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        history.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            feed = (MovieList.SearchEntity) extras.getSerializable("feed");
            Picasso.with(getApplicationContext()).load(feed.getPoster()).into(posterImage);
            title.setText(feed.getTitle());
            yearValue.setText(feed.getYear());
            fetchMovieList(feed.getImdbID());
        }

    }

    @OnClick(R.id.backButton)
    void setBackButton() {
        onBackPressed();
    }

    public void fetchMovieList(String id) {

        call = prestigeApi.getMovieDetails(id, "full");
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Response<MovieDetails> response) {

                if (response.isSuccess()) {
                    Log.e("stories", "--->" + response.raw());
                    MovieDetails movieDetails = response.body();
                    generValue.setText(movieDetails.getGenre());
                    directorValue.setText(movieDetails.getDirector());
                    actorValue.setText(movieDetails.getActors());
                    languageValue.setText(movieDetails.getLanguage());
                    countryValue.setText(movieDetails.getCountry());
                    ratingValue.setText(movieDetails.getImdbRating());
                    plotDescription.setText(movieDetails.getPlot());


                } else {
                    popToast("Failed");
                }


            }

            @Override
            public void onFailure(Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    popToast("Internet is slow.");
                }

            }
        });
    }
}
