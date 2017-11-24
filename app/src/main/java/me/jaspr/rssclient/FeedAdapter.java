package me.jaspr.rssclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.jaspr.rssclient.DetailActivity;

/**
 * Created by Jaspr on 17-10-31.
 */


class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {

    TextView txtTitle, txtPubDate, txtContent;
    private ItemClickListener itemClickListener;

    FeedViewHolder(View itemView) {
        super(itemView);

        txtTitle = itemView.findViewById(me.jaspr.rssclient.R.id.txtTitle);
        txtPubDate = itemView.findViewById(me.jaspr.rssclient.R.id.txtPubDate);
        txtContent = itemView.findViewById(me.jaspr.rssclient.R.id.txtContent);

        // Set Event
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), true);
        return true;
    }
}

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder>{

    private RSSObject rssObject;
    private Context mContext;
    private LayoutInflater inflater;

    FeedAdapter(RSSObject rssObject, Context mContext) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(me.jaspr.rssclient.R.layout.row, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.txtTitle.setText(Html.fromHtml(rssObject.getItems().get(position).getTitle(), Html.FROM_HTML_MODE_COMPACT,null, null));
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.txtContent.setText(Html.fromHtml(rssObject.getItems().get(position).getDescription()));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongCilck) {
                if (!isLongCilck) {
                    Bundle urlBundle = new Bundle();
                    urlBundle.putString("link", rssObject.getItems().get(position).getLink());
                    urlBundle.putString("title", rssObject.getItems().get(position).getTitle());
                    urlBundle.putString("pubdate", rssObject.getItems().get(position).getPubDate());
                    urlBundle.putString("content", rssObject.getItems().get(position).getContent());
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtras(urlBundle);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssObject.items.size();
    }
}
