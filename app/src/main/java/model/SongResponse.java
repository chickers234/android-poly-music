package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SongResponse {
    private boolean status;
    @SerializedName("data")
    @Expose
    private List<Song> songList;

    public SongResponse() {
    }

    public SongResponse(boolean status, List<Song> songList) {
        this.status = status;
        this.songList = songList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
}
