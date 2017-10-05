
package anindya.fb.detailmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Posts {

    @SerializedName("data")
    @Expose
    private List<PostsData> data = null;

    public List<PostsData> getData() {
        return data;
    }

    public void setData(List<PostsData> data) {
        this.data = data;
    }
}
