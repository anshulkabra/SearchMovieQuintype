package anshul.org.quintypetask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import anshul.org.quintypetask.Adapter.MovieListAdapter;
import anshul.org.quintypetask.DataBase.SqliteHelper;
import anshul.org.quintypetask.Model.MovieList;
import anshul.org.quintypetask.Utils.LoadMoreListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryViewMovies extends AppCompatActivity {

    public MovieListAdapter movieListAdapter;
    public ArrayList<MovieList.SearchEntity> itemsEntities = new ArrayList<>();
    @Bind(R.id.feedList)
    LoadMoreListView moviewListView;
    @Bind(R.id.noResultFound)
    TextView noResultFound;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.history)
    ImageView history;
    private SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_view_movies);
        ButterKnife.bind(this);

        title.setText("History Viewed");
        sqliteHelper = SqliteHelper.getInstance(this);

        itemsEntities = sqliteHelper.getSearch();

        if (itemsEntities.isEmpty()) {
            noResultFound.setVisibility(View.VISIBLE);
        }
        movieListAdapter = new MovieListAdapter(this, itemsEntities);
        moviewListView.setAdapter(movieListAdapter);
        moviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                MovieList.SearchEntity data = (MovieList.SearchEntity) parent.getItemAtPosition(position);

                Intent bundle = new Intent(getApplicationContext(), MovieDetailsActivity.class);

                bundle.putExtra("feed", data);
                startActivity(bundle);

            }
        });

    }

    @OnClick(R.id.backButton)
    void setBackButton() {
        onBackPressed();
    }
}
