package com.ybc.jesonopenweb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenReadActivity extends AppCompatActivity {


    String title = null;
    String imgurl = null;
    int id;
    String body = null;
    TextView fruitContentText;

    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String fruitContent = body;
                    fruitContentText.setText(fruitContent);
                    break;
                default:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_read);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        imgurl = intent.getStringExtra("imgurl");
        id = intent.getIntExtra("openid", 42);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_read);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView fruitImageView = (ImageView) findViewById(R.id.fruit_image_view);
        fruitContentText = (TextView) findViewById(R.id.fruit_content_text);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(title);
        Glide.with(this).load("http://www.bmbhome.org" + imgurl).into(fruitImageView);
        sendOkhttp("http://www.bmbhome.org/api/article_detail/" + id);

    }

    private String generateFruitContent(String fruitName) {
        StringBuilder fruitContent = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            fruitContent.append(fruitName);
        }
        return fruitContent.toString();
    }

    private void sendOkhttp(final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    body = response.body().string();
                    hander.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
