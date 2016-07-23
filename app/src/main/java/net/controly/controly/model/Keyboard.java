package net.controly.controly.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * This class represents a keyboard entity.
 */
public class Keyboard {

    @SerializedName("KeyboardID")
    private long id;

    @SerializedName("KeyboardName")
    private String name;

    @SerializedName("HashTag")
    private String hashTag;

    @SerializedName("KeyboardPic")
    private String pictureUrl;

    @SerializedName("BackgroundImage")
    private String backgroundImage;

    @SerializedName("Maker")
    private long maker;

    @SerializedName("Portrait")
    private int portrait;

    @SerializedName("Color")
    private String color;

    @SerializedName("ScreenSize")
    private String screenSize;

    @SerializedName("PublishDate")
    private Date publishDate;

    @SerializedName("LastUpdated")
    private Date lastUpdated;

    //@SerializedName("BasedOn") - TODO Don't know what to do here, since in all the examples this field is null

    @SerializedName("Public")
    private int pub;

    @SerializedName("Up")
    private int up;

    public Keyboard() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public long getMaker() {
        return maker;
    }

    public void setMaker(long maker) {
        this.maker = maker;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getPub() {
        return pub;
    }

    public void setPub(int pub) {
        this.pub = pub;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }
}
