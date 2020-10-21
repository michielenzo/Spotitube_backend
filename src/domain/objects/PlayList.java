package domain.objects;

import java.util.ArrayList;
import java.util.List;

public class PlayList {

    private int id;
    private Owner owner;
    private String name;
    private List<Track> trackList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(ArrayList<Track> trackList) {
        this.trackList = trackList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
