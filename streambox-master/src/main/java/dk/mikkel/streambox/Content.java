package dk.mikkel.streambox;

public class Content {

    private final int id;
    private final String title;
    private final Genre genre;
    private final int lengthMinutes;
    private final int ageRating;

    private int views;

    public Content(int id, String title, Genre genre, int lengthMinutes, int ageRating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.lengthMinutes = lengthMinutes;
        this.ageRating = ageRating;
        this.views = 0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getLengthMinutes() {
        return lengthMinutes;
    }

    public int getAgeRating() {
        return ageRating;
    }

    public int getViews() {
        return views;
    }

    public void incrementViews() {
        // bevidst tomt: så tests kan fejle
    }
}
