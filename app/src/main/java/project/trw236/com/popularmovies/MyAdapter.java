package project.trw236.com.popularmovies;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String jsonInfos;
    Context mContext;
    int screenW;

    private int numItems;
    public String[] urlsPics;

    public ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

//    private static final String str = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";

    public MyAdapter(Context context, String data, ListItemClickListener listener){
        this.mContext = context;
        this.jsonInfos = data;
        this.mOnClickListener = listener;
        this.screenW = Resources.getSystem().getDisplayMetrics().widthPixels;
        try {
            getInfoFromJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getInfoFromJson(String data) throws JSONException {
        JSONObject rawJson = new JSONObject(data);
        JSONArray movies = rawJson.getJSONArray("results");
        numItems = movies.length();
        urlsPics = new String[numItems];
        for (int i = 0; i < movies.length(); i++){
            JSONObject movie = movies.getJSONObject(i);
            String url = "http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path");
            urlsPics[i] = url;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
//        myViewHolder.mImageView.setImageResource(R.drawable.dog);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.mImageView.setImageResource(R.drawable.dog);
//        holder.mImageView.setImageResource(R.drawable.cat);
        Picasso.with(mContext).load(urlsPics[position]).resize(screenW/2,0).into(holder.mImageView);
    }


    @Override
    public int getItemCount() {
        return numItems;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img_movie);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
