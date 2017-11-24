package me.jaspr.rssclient;

public class Feed
{
    public String url;
    public String title;
    public String link;
    public String author;
    public String description;
    public String image;

    public Feed(String url, String title, String link, String author, String description, String image) {
        this.url = url;
        this.title = title;
        this.link = link;
        this.author = author;
        this.description = description;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
