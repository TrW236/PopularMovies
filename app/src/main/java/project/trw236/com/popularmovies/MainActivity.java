package project.trw236.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import project.trw236.com.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MyAdapter.ListItemClickListener {
    public static final String KEY_DATA = "JSON_DATA";
    public static final String IDX_MOVIE = "IDX_MOVIEW";
    //    private static final int NUM_ITEMS = 100;
    MyAdapter mAdapter;
    RecyclerView mRecyclerView;
    String rawStr;

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView.LayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView = (RecyclerView) findViewById(R.id.recy_view);
        mRecyclerView.setLayoutManager(layoutManager);
        if (isOnline()) {
            makeQueryMoviesInfo("popular?");
        } else {
            Toast.makeText(this, "No Internet!", Toast.LENGTH_LONG).show();
        }
    }

    private void makeQueryMoviesInfo(String query) {
        URL movieInfoUrl = NetworkUtils.buildUrl(query);
        new QueryMoviesInfoTask().execute(movieInfoUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (isOnline()) {
            switch (item.getItemId()) {
                case R.id.top_rated:
                    makeQueryMoviesInfo("top_rated?");
                    return true;
                case R.id.most_popular:
                    makeQueryMoviesInfo("popular?");
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            Toast.makeText(this, "No Internet!", Toast.LENGTH_LONG).show();
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(KEY_DATA, rawStr);
        intent.putExtra(IDX_MOVIE, clickedItemIndex);
        startActivity(intent);
    }

    public class QueryMoviesInfoTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideViews();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            rawStr = s;
            mAdapter = new MyAdapter(MainActivity.this, s, MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);
            showViews();
        }
    }

    public void hideViews(){
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public void showViews(){
        mRecyclerView.setVisibility(View.VISIBLE);
    }


}
