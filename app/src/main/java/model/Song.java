package model;

public class Song {
    private String _id;
    private String title;
    private String singer;
    private String author;
    private String category;
    private String poster;
    private String song;

    public Song() {
    }

    public Song(String _id, String title, String singer, String author, String category, String poster, String song) {
        this._id = _id;
        this.title = title;
        this.singer = singer;
        this.author = author;
        this.category = category;
        this.poster = poster;
        this.song = song;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }
}
