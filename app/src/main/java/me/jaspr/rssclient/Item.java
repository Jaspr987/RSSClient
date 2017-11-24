package me.jaspr.rssclient;

import java.util.List;

public class Item
{
    public String title;
    public String pubDate;
    public String link;
    public String guid;
    public String author;
    public String thumbnail;
    public String description;
    public String content;
    public List<String> enclosure;
    public List<String> categories;

    public Item(String title, String pubDate, String link, String guid, String author, String thumbnail, String description, String content, List<String> enclosure, List<String> categories) {
        this.title = title;
        this.pubDate = pubDate;
        this.link = link;
        this.guid = guid;
        this.author = author;
        this.thumbnail = thumbnail;
        this.description = description;
        this.content = content;
        this.enclosure = enclosure;
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }

    public String getGuid() {
        return guid;
    }

    public String getAuthor() {
        return author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public List<String> getEnclosure() {
        return enclosure;
    }

    public List<String> getCategories() {
        return categories;
    }
}