package anshul.org.quintypetask.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import anshul.org.quintypetask.Model.MovieList;
import anshul.org.quintypetask.R;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MovieListAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private ArrayList<MovieList.SearchEntity> mItems;
    private Activity mContext;


    public MovieListAdapter(Activity context, ArrayList<MovieList.SearchEntity> items) {
        this.mInflater = LayoutInflater.from(context.getApplicationContext());
        this.mItems = items;
        this.mContext = context;

    }

    @Override
    public int getCount() {

        return mItems.size();


    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.movie_list_row, parent, false);

            holder = new Holder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        MovieList.SearchEntity data = mItems.get(position);

        holder.moviewTitle.setText(data.getTitle());
        holder.moviewyear.setText(data.getYear());


        if (data.getPoster() != null) {
            Picasso.with(mContext).load(data.getPoster()).placeholder(R.drawable.icn_placeholder).error(R.drawable.icn_placeholder).into(holder.imagePoster);
        }

        return convertView;
    }


    static class Holder {
        @Bind(R.id.moviewyear)
        TextView moviewyear;

        @Bind(R.id.moviewTitle)
        TextView moviewTitle;

        @Bind(R.id.imagePoster)
        ImageView imagePoster;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}

