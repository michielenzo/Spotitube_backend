package domain.objects;

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
}
