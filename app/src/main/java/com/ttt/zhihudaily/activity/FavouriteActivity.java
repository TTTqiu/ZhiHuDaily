package com.ttt.zhihudaily.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.db.DBUtil;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.adapter.MyListAdapter;
import com.ttt.zhihudaily.util.HttpUtil;

import java.util.List;

public class FavouriteActivity extends BaseActivity {

    private MyListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_fav);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Title> list = DBUtil.getInstance(this).loadFavourite();
        listView = (ListView) findViewById(R.id.list_view_fav);
        adapter = new MyListAdapter(this, R.layout.list_view_item, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (HttpUtil.isNetworkConnected(FavouriteActivity.this)) {
                    NewsActivity.startNewsActivity(FavouriteActivity.this, adapter.getItem(position));
                } else {
                    Snackbar.make(listView, "没有网络", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
