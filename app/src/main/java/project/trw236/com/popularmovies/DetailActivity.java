package project.trw236.com.popularmovies;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    TextView mTitle;
    ImageView mPoster;
    TextView mDate;
    TextView mVote;
    TextView mOverview;

    String title;
    String posterPath;
    String date;
    String vote;
    String overview;

    int screenW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.screenW = Resources.getSystem().getDisplayMetrics().widthPixels;

        mTitle = (TextView) findViewById(R.id.title);
        mPoster = (ImageView) findViewById(R.id.poster);
        mDate = (TextView) findViewById(R.id.release_date);
        mVote = (TextView) findViewById(R.id.vote);
        mOverview = (TextView) findViewById(R.id.overview);

        Intent intent = getIntent();
        int idx = intent.getIntExtra(MainActivity.IDX_MOVIE, -1);
        String rawData = intent.getStringExtra(MainActivity.KEY_DATA);
        if ((idx != -1) && (rawData!=null)){
            try {
                getInfoMovie(rawData, idx);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        populateViews();
    }

    private void populateViews() {
        mTitle.setText(title);
        Picasso.with(this).load(posterPath).resize(screenW/2,0).into(mPoster);
        mDate.setText("Release:\n "+date);
        mVote.setText("Vote:\n "+vote);
        mOverview.setText("Overview:\n\n"+overview);
    }

    private void getInfoMovie(String rawData, int idx) throws JSONException {
        JSONObject rawInfo = new JSONObject(rawData);
        JSONArray movies = rawInfo.getJSONArray("results");
        JSONObject movie = movies.getJSONObject(idx);
        title = movie.getString("title");
        posterPath = "http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path");
        date = movie.getString("release_date");
        vote = movie.getString("vote_average");
        overview = movie.getString("overview");
    }
}
