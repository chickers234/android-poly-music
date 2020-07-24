package model;

public class MV {
    private String _id;
    private String title;
    private String singer;
    private String poster;
    private String mv;

    public MV() {
    }

    public MV(String _id, String title, String singer, String poster, String mv) {
        this._id = _id;
        this.title = title;
        this.singer = singer;
        this.poster = poster;
        this.mv = mv;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getMv() {
        return mv;
    }

    public void setMv(String mv) {
        this.mv = mv;
    }
}
