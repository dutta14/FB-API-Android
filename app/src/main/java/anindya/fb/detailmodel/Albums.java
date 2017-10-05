
package anindya.fb.detailmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Albums {

    @SerializedName("data")
    @Expose
    private List<AlbumsData> data = null;

    public List<AlbumsData> getData() {
        return data;
    }

    public void setData(List<AlbumsData> data) {
        this.data = data;
    }
}
