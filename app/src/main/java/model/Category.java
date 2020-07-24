package model;

import java.io.Serializable;

public class Category implements Serializable {
    private String url;

    public Category() {
    }

    public Category(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
