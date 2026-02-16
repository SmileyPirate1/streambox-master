package dk.mikkel.streambox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StreamBoxService {

    public Content addContent(String title, Genre genre, int lengthMinutes, int ageRating) {
        // bevidst “forkert” og uden validering: så tests fejler
        return new Content(0, title, genre, lengthMinutes, ageRating);
    }

    public List<Content> getCatalog() {
        // bevidst tomt katalog: så tests fejler
        return Collections.emptyList();
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
