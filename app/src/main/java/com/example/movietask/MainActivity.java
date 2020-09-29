package com.example.movietask;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SuggestionAdapter.ItemClicked{
    Stack<String> stack = new Stack<>();
    ArrayList<String> suggestions = new ArrayList<>();
    ArrayList<Movie> movieHelper = new ArrayList<>();
    ArrayList<Movie> movieList = new ArrayList<>();
    SearchView searchView;
    RecyclerView rvList;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    int totalPages = 1, counter = 0;
    ListView lvList;
    String queryPersist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        lvList = (ListView) findViewById(R.id.lvList);
        rvList = findViewById(R.id.rvList);

        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);

        ArrayList<String> temp = dbGet();
        for(int i = 0 ; i < 10 ; i++){
            stack.push(temp.get(i));
        }
        suggestions.addAll(stack);

        myAdapter = new SuggestionAdapter(MainActivity.this, suggestions);
        rvList.setAdapter(myAdapter);




        /*lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() { //iz nekog razloga se svaki peti "klikne" kad se na prvi klikne
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) view.getLayoutParams();
                itemClickedPostion = position;
                Toast.makeText(MainActivity.this, "pos " + lvList.getPositionForView(view), Toast.LENGTH_SHORT).show();

                if (lvList.getAdapter().getView(lvList.getPositionForView(view), view, parent).getHeight() == 420) {

                    layoutParams.height = 650;
                    lvList.getChildAt(position).setLayoutParams(layoutParams);
                } else {

                    layoutParams.height = 420;
                    lvList.getChildAt(position).setLayoutParams(layoutParams);
                }
            }
        });*/

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvList.setVisibility(View.VISIBLE);
                myAdapter.notifyDataSetChanged();
            }
        });


        lvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            MovieAdapter adapter = new MovieAdapter(MainActivity.this, R.layout.row_layout, movieList); //importante senior


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                /*AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) lvList.getLayoutParams();
                layoutParams.height = 420;
                lvList.getChildAt(itemClickedPostion).setLayoutParams(layoutParams);*/
                rvList.setVisibility(View.GONE);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (lvList.getLastVisiblePosition() - lvList.getHeaderViewsCount() -
                        lvList.getFooterViewsCount()) >= (adapter.getCount() - 1)) {

                    //*************
                    if (totalPages != counter - 1) {
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://api.themoviedb.org/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

                        Call<MovieHelper> call = jsonPlaceHolderApi.getHelperMovies(queryPersist, counter);
                        call.enqueue(new Callback<MovieHelper>() {
                            @Override
                            public void onResponse(Call<MovieHelper> call, Response<MovieHelper> response) {//******
                                if (!response.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Error Code: " + response.code(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                movieHelper.clear();
                                counter++;
                                MovieHelper movies = response.body();
                                movieHelper = movies.getResults();
                                totalPages = movies.getTotal_pages();

                                if (movieHelper.size() != 0) {
                                    for (int j = 0; j < movieHelper.size(); j++) {
                                        Movie movie = new Movie("https://image.tmdb.org/t/p/w500/" + movieHelper.get(j).getPoster(),
                                                movieHelper.get(j).getTitle(),
                                                movieHelper.get(j).getDate(),
                                                movieHelper.get(j).getDescription());

                                        if (!movieList.contains(movie)) movieList.add(movie);
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "There are no more movies", Toast.LENGTH_SHORT).show();
                                }
                                Parcelable state = lvList.onSaveInstanceState();
                                MovieAdapter adapter = new MovieAdapter(MainActivity.this, R.layout.row_layout, movieList); //importante senior
                                lvList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                lvList.onRestoreInstanceState(state);

                            }//******

                            @Override
                            public void onFailure(Call<MovieHelper> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        //*************
                    } else
                        Toast.makeText(MainActivity.this, "There are no more movies", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        //---------------------------------------------


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                // do something on text submit
                if (!query.isEmpty()) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://api.themoviedb.org/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

                    Call<MovieHelper> call = jsonPlaceHolderApi.getHelperMovies(query, 1);
                    call.enqueue(new Callback<MovieHelper>() {
                        @Override
                        public void onResponse(Call<MovieHelper> call, Response<MovieHelper> response) {//******
                            if (!response.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Error Code: " + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                rvList.setVisibility(View.GONE);
                                queryPersist = query;
                                movieHelper.clear();
                                movieList.clear();
                                counter++;
                                MovieHelper movies = response.body();
                                movieHelper = movies.getResults();
                                totalPages = movies.getTotal_pages();


                            }
                            if (movieHelper.size() != 0) {
                                for (int j = 0; j < movieHelper.size(); j++) {
                                    Movie movie = new Movie("https://image.tmdb.org/t/p/w500/" + movieHelper.get(j).getPoster(),
                                            movieHelper.get(j).getTitle(),
                                            movieHelper.get(j).getDate(),
                                            movieHelper.get(j).getDescription());

                                    if (!movieList.contains(movie)) movieList.add(movie);
                                }
                                if(!suggestions.contains(query)) {
                                    stack.push(query);
                                    suggestions.add(0,query);
                                    suggestions.remove(10);
                                    dbAdd(stack.peek());
                                    myAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "There are no movies with this name", Toast.LENGTH_SHORT).show();
                            }

                            MovieAdapter adapter = new MovieAdapter(MainActivity.this, R.layout.row_layout, movieList); //importante senior
                            lvList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }//******

                        @Override
                        public void onFailure(Call<MovieHelper> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else
                    Toast.makeText(MainActivity.this, "No searchable text entered", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                rvList.setVisibility(View.VISIBLE);
                //ArrayList<String> queryHelper = new ArrayList<>();
                //queryHelper.addAll(suggestions);

                return false;
            }
        });

    }

    @Override
    public void onItemClicked(int Index) {
        searchView.setQuery(suggestions.get(Index), true);
    }
    public void dbAdd(String sugg){
        try{
            SuggestionsDB db = new SuggestionsDB(MainActivity.this);
            db.open();
            db.createEntry(sugg);
        }catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public ArrayList<String> dbGet(){
        SuggestionsDB db = new SuggestionsDB(MainActivity.this);
        db.open();
        ArrayList<String> suggs = new ArrayList<>();
        for (String i : db.getSuggestions()) {
            suggs.add(i);
        }

        db.close();
        return suggs;
    }
}
