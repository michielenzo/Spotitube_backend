package rest.dto;

import java.util.ArrayList;
import java.util.List;

public class PlayList {

    private int id;
    private boolean owner;
    private String name;
    private List<Track> tracks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Track> getTracks() { return tracks; }

    public void setTracks(ArrayList<Track> trackList) {
        this.tracks = trackList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getOwner() {
        return owner;
    }

    public void setOwner(boolean isOwner) {
        this.owner = isOwner;
    }
}
