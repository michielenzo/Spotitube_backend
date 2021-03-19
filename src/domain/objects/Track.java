package domain.objects;

import com.couchbase.client.java.json.JsonObject;

public abstract class Track {

    private int id;
    private String performer;
    private String title;
    private int playCount;
    private int duration;
    private boolean offlineAvailable;

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOfflineAvailable() { return offlineAvailable; }

    public void setOfflineAvailable(boolean offlineAvailable) { this.offlineAvailable = offlineAvailable; }

    public int getPlayCount() { return playCount; }

    public void setPlayCount(int playCount) { this.playCount = playCount; }

    public static Track fromJson(JsonObject json){
        Track track;

        if(json.getString("album") != null){
            track = new Song();
            ((Song)track).setAlbum(json.getString("album"));
        }else{
            track = new Video();
            ((Video)track).setPublicationDate(json.getString("publicationdate"));
            ((Video)track).setDescription(json.getString("description"));
        }

        track.setDuration(json.getInt("duration"));
        track.setId(json.getInt("id"));
        track.setPerformer(json.getString("performer"));
        track.setTitle(json.getString("title"));
        track.setDuration(json.getInt("duration"));
        track.setOfflineAvailable(json.getBoolean("offlineavailable"));
        track.setPlayCount(json.getInt("playcount"));

        return track;
    }
}
