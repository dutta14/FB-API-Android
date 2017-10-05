
package anindya.fb.detailmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photos {

    @SerializedName("data")
    @Expose
    private List<PhotosData> data = null;

    public List<PhotosData> getData() {
        return data;
    }

    public void setData(List<PhotosData> data) {
        this.data = data;
    }

}
