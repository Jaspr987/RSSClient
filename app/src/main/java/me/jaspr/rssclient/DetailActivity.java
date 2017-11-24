package me.jaspr.rssclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import moe.shizuku.fontprovider.FontProviderClient;

public class DetailActivity extends AppCompatActivity {

    private static boolean sFontInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int code = FontProviderClient.checkAvailability(getApplicationContext());
        if (code != FontProviderClient.FontProviderAvailability.OK) {
            // 根据具体的 code 提示用户
            Log.i("TEST", "onCreate: FONT PROVIDER 不可用");
        }

        if (!sFontInitialized) {
            FontProviderClient client = FontProviderClient.create(this);
            //不可用时会返回 null
            if (client != null) {
                client.setNextRequestReplaceFallbackFonts(true);
                client.replace("Noto Sans CJK",
                        "sans-serif", "sans-serif-medium");
            }
            sFontInitialized = true;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_v2);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setTitle(Html.fromHtml(getIntent().getStringExtra("title"), Html.FROM_HTML_MODE_LEGACY, null, null));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // 设置返回按钮
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView pubdate = findViewById(R.id.detail_pubdate);
        final TextView content = findViewById(R.id.detail_content);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final FloatingActionButton fab = findViewById(R.id.fab);

        Intent intent = getIntent();
        collapsingToolbar.setTitle(Html.fromHtml(getIntent().getStringExtra("title"), Html.FROM_HTML_MODE_LEGACY, null, null));
        pubdate.setText(intent.getStringExtra("pubdate"));

        WindowManager wm1 = this.getWindowManager();
        final int windows_width = wm1.getDefaultDisplay().getWidth();

        new Thread(new Runnable() {
            @Override
            public void run() {
                content.setVisibility(View.GONE);
                fab.hide();
                final Spanned content_text = Html.fromHtml(getIntent().getStringExtra("content"),
                        Html.FROM_HTML_MODE_LEGACY,
                        new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String source) {
                                InputStream is = null;
                                try {
                                    is = (InputStream) new URL(source).getContent();
                                    Drawable d = Drawable.createFromStream(is, "src");
                                    double height = d.getIntrinsicHeight();
                                    double width = d.getIntrinsicWidth();
                                    double scale = (windows_width - 60) / width;
                                    width = windows_width - 60;
                                    height = scale * height;
                                    d.setBounds(0, 0, (int) width, (int) height);
                                    is.close();
                                    return d;
                                } catch (Exception e) {
                                    return null;
                                }
                            }
                        }, null);

                content.post(new Runnable() {
                    @Override
                    public void run() {
                        content.setText(content_text);
                        content.setMovementMethod(LinkMovementMethod.getInstance());
                        progressBar.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                        fab.show();
                    }
                });
            }
        }).start();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("title") +
                        "\n" + getIntent().getStringExtra("link"));
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_export:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getIntent().getStringExtra("link")));
                this.startActivity(browserIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }
}
