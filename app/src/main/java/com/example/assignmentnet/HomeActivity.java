package com.example.assignmentnet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.assignmentnet.model.Fave;
import com.example.assignmentnet.model.Photo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArrayList<Photo> photoList;
    private PhotoAdapter photoAdapter;
    private int page = 1;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.rvPhotos);
        photoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(this, photoList);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setAdapter(photoAdapter);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(null);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        loadPhotos(page);

        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HomeActivity.this.page = 1;
                photoList.clear();
                loadPhotos(HomeActivity.this.page);

            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                HomeActivity.this.page++;
                loadPhotos(HomeActivity.this.page++);
            }
        });

    }


    public void loadPhotos(int page) {

        AndroidNetworking.post("https://www.flickr.com/services/rest/")
                .addBodyParameter("method", "flickr.favorites.getList")
                .addBodyParameter("api_key", "95a4593e57466dfe794fec79a9553adb")
                .addBodyParameter("user_id", "184846532@N03")
                .addBodyParameter("format", "json")
                .addBodyParameter("nojsoncallback", "1")
                .addBodyParameter("extras", "views,media,path_alias,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o")
                .addBodyParameter("per_page", "20")
                .addBodyParameter("page", String.valueOf(page))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(Fave.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        //tat su kien loading
                        swipeRefreshLayout.setRefreshing(false);
                        Fave fave = (Fave) response;
                        List<Photo> photos = fave.getPhotos().getPhoto();
                        photoList.addAll(photos);
                        photoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
