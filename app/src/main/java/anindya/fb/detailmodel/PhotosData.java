
package anindya.fb.detailmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotosData {

    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
