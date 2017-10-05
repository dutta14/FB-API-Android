
package anindya.fb.detailmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemDetail implements Serializable {

    @SerializedName("albums")
    @Expose
    private Albums albums;
    @SerializedName("posts")
    @Expose
    private Posts posts;
    @SerializedName("id")
    @Expose
    private String id;

    public Albums getAlbums() {
        return albums;
    }

    public void setAlbums(Albums albums) {
        this.albums = albums;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
