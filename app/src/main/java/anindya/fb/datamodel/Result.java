package anindya.fb.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Result implements Serializable{

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("page")
    @Expose
    private Page page;
    @SerializedName("event")
    @Expose
    private Event event;
    @SerializedName("place")
    @Expose
    private Place place;
    @SerializedName("group")
    @Expose
    private Group group;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
