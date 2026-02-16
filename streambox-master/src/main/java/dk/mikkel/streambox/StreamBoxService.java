package dk.mikkel.streambox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamBoxService {
    private static final AtomicInteger streamId = new AtomicInteger(0);
    private final List<Content> catalog = new ArrayList<>();

    public Content addContent(String title, Genre genre, int lengthMinutes, int ageRating) {
        // Lavet en AtomicInteger der automatisk øger talet vær gang den bliver brugt og sat ind på id af Content
        Content newContent = new Content(streamId.incrementAndGet(), title, genre, lengthMinutes, ageRating);
        // Tilføjer newContent til catalog
        catalog.add(newContent);
        return newContent;
    }

    public List<Content> getCatalog() {
        // returner catalog fra private List<Content>
        return catalog;
    }

    public Optional<Content> findById(int id) {
        // bevidst: finder aldrig noget
        return Optional.empty();
    }

    public boolean play(int contentId, int userAge) {
        // bevidst: spiller aldrig noget
        return false;
    }

    public List<Content> findByGenre(Genre genre) {
        // bevidst: returnerer altid tom
        return new ArrayList<>();
    }

    public int totalRuntimeByGenre(Genre genre) {
        // bevidst: 0
        return 0;
    }

    public List<Content> topTrending(int n) {
        // bevidst: tom liste (og ingen validering)
        return new ArrayList<>();
    }

    public Optional<Content> mostViewedInGenre(Genre genre) {
        // bevidst: ingen resultater
        return Optional.empty();
    }

    public boolean removeById(int id) {
        // bevidst: fjerner aldrig noget
        return false;
    }
}
