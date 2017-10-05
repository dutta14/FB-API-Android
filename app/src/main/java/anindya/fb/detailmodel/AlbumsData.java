
package anindya.fb.detailmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumsData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("id")
    @Expose
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
