package com.iceteck.hivote.data;

/**
 * Project Hi-Vote
 * Created by Larry Akah on 11/18/15 3:16 PM.
 */
public class Nominees {

    private long id;
    private String name;
    private String portfolio;
    private String url;
    private String bitmap;
    private long votes;
    private String categoryId;

    public Nominees(long nid, String nname, String nport, String nurl, String nbitmap, long nvotes, String cid){
        this.id = nid;
        this.name = nname;
        this.portfolio = nport;
        this.url = nurl;
        this.bitmap = nbitmap;
        this.votes = nvotes;
        this.categoryId = cid;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public long getVotes() {
        return this.votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }

    public String getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPortfolio() {
        return this.portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
