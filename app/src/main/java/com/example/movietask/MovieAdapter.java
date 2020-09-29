package com.example.movietask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String TAG = "MovieListAdapter";

    private Context context;
    private int resource;

    private static class ViewHolder{
        TextView title;
        TextView date;
        TextView description;
        ImageView poster;
    }

    public MovieAdapter(Context cContext, int cResource, ArrayList<Movie> cMovies){
        super(cContext, cResource, cMovies);
        context = cContext;
        resource = cResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        setupImageLoader();

        String poster = getItem(position).getPoster();
        String title = getItem(position).getTitle();
        String date = getItem(position).getDate();
        String description = getItem(position).getDescription();

        Movie movie = new Movie(poster, title, date, description);

        final View result;
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder= new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.poster = (ImageView) convertView.findViewById(R.id.ivPoster);
            holder.date = (TextView) convertView.findViewById(R.id.tvDate);
            holder.description = (TextView) convertView.findViewById(R.id.tvDescription);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = context.getResources().getIdentifier("@drawable/image_failed", null, context.getPackageName());


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();


        imageLoader.displayImage(poster, holder.poster, options);

        holder.title.setText(title);
        holder.date.setText(date);
        holder.description.setText(description);


        return convertView;
    }
    private void setupImageLoader(){
        //3rd party image loader I found in some tutorials
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100*1024*1024).build();

        ImageLoader.getInstance().init(config);
    }
}
