package me.jaspr.rssclient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;

import moe.shizuku.fontprovider.FontProviderClient;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;
    SwipeRefreshLayout swipeRefreshLayout;

    // RSS link
    private final String RSS_link = "http://www.pingwest.com/feed/";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    private static boolean sFontInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!sFontInitialized) {
            FontProviderClient client = FontProviderClient.create(this);
            /**
             * 不可用时会返回 null
             */
            if (client != null) {
                Log.i("TEST", "onCreate: FontProvider 可用");
                /**
                 * 设置下次请求会替换默认的回退列表
                 * 这样在使用自己提供字体时也能同时使用 Font Provider 的字体
                 */
                client.setNextRequestReplaceFallbackFonts(true);

                /**
                 * 将 "sans-serif" 和 "sans-serif-medium" 替换为 "Noto Sans CJK" 的对应字体
                 * 字重将根据名称自动解析。
                 * 在 sample 项目中还可以看到如何设定默认字体（如替换 emoji 字体）。
                 *
                 * 会返回对应个数的 Typeface，本别是包含全部字体的 Typeface 及由此 Typeface
                 * 创建的有对应字重别名的 Typeface。
                 */
                client.replace("Noto Sans CJK",
                        "sans-serif", "sans-serif-medium");
            }
            sFontInitialized = true;
        }

        super.onCreate(savedInstanceState);
        setContentView(me.jaspr.rssclient.R.layout.activity_main);

        toolbar = findViewById(me.jaspr.rssclient.R.id.toolbar);
        toolbar.setTitle("PingWest News Feed");
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.srl);
        recyclerView = findViewById(me.jaspr.rssclient.R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadRSS();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRSS();
            }
        });
    }

    private void loadRSS() {
        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected String doInBackground(String... params) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result = http.GetHTTPData(params[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                rssObject = new Gson().fromJson(s, RSSObject.class);
                FeedAdapter adapter = new FeedAdapter(rssObject, getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        loadRSSAsync.execute(RSS_to_Json_API + RSS_link);
    }
}
