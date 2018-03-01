package model;

import java.io.Serializable;

/**
 * Created by AnhThao on 02/02/2018.
 */

public class SongItem implements Serializable {

    private String imgUrl;
    private String titleSong;
    private String singer;
    private String songUrl;

    public SongItem(String imgRes, String titleSong, String singer, String songUrl) {
        this.imgUrl = imgRes;
        this.titleSong = titleSong;
        this.singer = singer;
        this.songUrl = songUrl;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getImgRes() {
        return imgUrl;
    }

    public void setImgRes(String imgRes) {
        this.imgUrl = imgRes;
    }

    public String getTitleSong() {
        return titleSong;
    }

    public void setTitleSong(String titleSong) {
        this.titleSong = titleSong;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
