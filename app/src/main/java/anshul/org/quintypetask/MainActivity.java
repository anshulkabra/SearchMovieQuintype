package anshul.org.quintypetask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import anshul.org.quintypetask.Adapter.MovieListAdapter;
import anshul.org.quintypetask.DataBase.SqliteHelper;
import anshul.org.quintypetask.Model.MovieList;
import anshul.org.quintypetask.Network.ApiService;
import anshul.org.quintypetask.Network.RestClient;
import anshul.org.quintypetask.Utils.LoadMoreListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BlundellActivity {

    public MovieListAdapter movieListAdapter;
    public ArrayList<MovieList.SearchEntity> itemsEntities = new ArrayList<>();
    public ApiService prestigeApi = RestClient.getApiService();
    public Call<MovieList> call;
    @Bind(R.id.feedList)
    LoadMoreListView moviewListView;
    @Bind(R.id.noResultFound)
    TextView noResultFound;
    @Bind(R.id.showingResultFor)
    TextView showingResultFor;
    @Bind(R.id.searchMovies)
    SearchView searchMovies;
    @Bind(R.id.backButton)
    ImageView backButton;
    private SqliteHelper sqliteHelper;
    private int page = 1;
    private String searchKey = "";
    private boolean hashMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        backButton.setVisibility(View.INVISIBLE);
        moviewListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            public void onLoadMore() {
                if (hashMore) {
                    fetchMovieList(searchKey);
                }
            }
        });
        sqliteHelper = SqliteHelper.getInstance(this);

        movieListAdapter = new MovieListAdapter(this, itemsEntities);
        moviewListView.setAdapter(movieListAdapter);

        moviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                MovieList.SearchEntity data = (MovieList.SearchEntity) parent.getItemAtPosition(position);

                sqliteHelper.saveSearch(data);

                Intent bundle = new Intent(getApplicationContext(), MovieDetailsActivity.class);

                bundle.putExtra("feed", data);
                startActivity(bundle);

            }
        });

        searchMovies.requestFocusFromTouch();
        searchMovies.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemsEntities.clear();
                movieListAdapter.notifyDataSetChanged();
                hashMore = true;
                page = 1;
                searchKey = query;
                fetchMovieList(query);
                showingResulstForTag(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @OnClick(R.id.history)
    void setBackButton() {
        startActivity(new Intent(this, HistoryViewMovies.class));
    }

    public void showingResulstForTag(String tagValueV) {
        showingResultFor.setVisibility(View.VISIBLE);
        showingResultFor.setText("Showing result for '" + tagValueV + "' tag");
    }

    public void fetchMovieList(String tagType) {

        call = prestigeApi.getMovieList(page, tagType);
        page++;
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Response<MovieList> response) {

                if (response.isSuccess()) {
                    Log.e("stories", "--->" + response.raw());

                    if (response.body().getResponse().equalsIgnoreCase("False")) {
                        noResultFound.setVisibility(View.VISIBLE);
                    } else {
                        itemsEntities.addAll(response.body().getSearch());
                        if (itemsEntities.size() == 0) {
                            noResultFound.setVisibility(View.VISIBLE);
                        } else {
                            noResultFound.setVisibility(View.GONE);
                        }
                        if (response.body().getSearch().size() < 10) {
                            hashMore = false;
                        }
                        movieListAdapter.notifyDataSetChanged();
                    }

                } else {
                    popToast("Failed");
                }

                moviewListView.onLoadMoreComplete();

            }

            @Override
            public void onFailure(Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    popToast("Internet is slow.");
                }
                moviewListView.onLoadMoreComplete();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (call != null) {
            if (!call.isExecuted())
                call.cancel();
        }

    }
}
