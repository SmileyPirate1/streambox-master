package dk.mikkel.streambox;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamBoxService {
    private static final AtomicInteger streamId = new AtomicInteger(0);
    private final List<Content> catalog = new ArrayList<>();

    public Content addContent(String title, Genre genre, int lengthMinutes, int ageRating) {
        Content newContent = new Content(streamId.incrementAndGet(), title, genre, lengthMinutes, ageRating);

        //Vi har lavet exceptions til potentielle fejl i input
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Movie has to have a name");
        }
        if (genre == null) {
            throw new IllegalArgumentException("Movie has to have a genre");
        }
        if (lengthMinutes <= 0) {
            throw new IllegalArgumentException("Movie has to be at least 1 minute long");
        }
        if (lengthMinutes > 600) {
            throw new IllegalArgumentException("Movie cant be more than 600 minutes long");
        }
        if (ageRating != 7 && ageRating != 0 && ageRating != 11 && ageRating != 15 && ageRating != 18) {
            throw new IllegalArgumentException("Age rating has to be 0, 7, 11, 15, or 18");
        }
        // Lavet en AtomicInteger der automatisk øger talet vær gang den bliver brugt og sat ind på id af Content
        // Tilføjer newContent til catalog
        catalog.add(newContent);

        return newContent;
    }

    public List<Content> getCatalog() {
        // Returner catalog fra private List<Content>
        return catalog;
    }

    //Find by id kører alle pladser igennem imens den tjekker om content id matcher id. Hvis den gør retunere vi det vi finder på pladsen.
    public Optional<Content> findById(int id) {
        for (Content content : catalog) {
            if (content.getId() == id) {
                return Optional.of(content);
            }
        }
        return Optional.empty();
    }

    // Play kører ved at finde id på filmen og tjekker efter alder hvorefter den tilføjer 1 til views hvis alder er høj nok for filmen
    public boolean play(int contentId, int userAge) {
        Optional<Content> maybeContent = findById(contentId);
        if (maybeContent.isEmpty()) {
            return false;
        }

        Content content = maybeContent.get();
        // En exception bliver kaldt hvis alder på user er under 1
        if (userAge <= 0) {
            throw new IllegalArgumentException("U cant be less than 0 years old");
        }
        if (userAge >= content.getAgeRating()) {
            content.setViews(content.getViews() + 1);
            return true;
        }
        return false;
    }

    // FindByGenre finder en genre i cataloget som matcher den genre vi tjekker på. Tilføjer genren og derefter sortere efter title.
    public List<Content> findByGenre(Genre genre) {
        ArrayList<Content> result = new ArrayList<>();
        for (Content content : catalog) {
            if (content.getGenre() == genre) {
                result.add(content);
            }
        }
        Collections.sort(result, Comparator.comparing(
                Content::getTitle,
                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)
        ));

        return result;
    }

    // Total run time by genre tjekker efter genre og ligger minutelength sammen for alle film i genren sammen
    public int totalRuntimeByGenre(Genre genre) {
        List<Content> list = findByGenre(genre);
        int sum = 0;
        for (Content content : list) {
            sum += content.getLengthMinutes();
        }
        return sum;
    }

    // topTrending sortere først efter views og derefter efter title. Den retunere værdierne n samt fejlhåndtere at der -
    // skal være minimum 1 trending film
    public List<Content> topTrending(int n) {
        ArrayList<Content> result = new ArrayList<>(catalog);
        if (n <= 0){
            throw new IllegalArgumentException("Top trending cant show less than 1 movie");
        }
        result.sort(
                Comparator.comparingInt(Content::getViews).reversed()
                        .thenComparing(Content::getTitle, String.CASE_INSENSITIVE_ORDER)
        );
        return result.stream().limit(n).toList();
    }

    // Most viewed in genre tjekker efter den mest viewed film inden for den valgte genre, sorter efter navn hvis der er samme antal views
    public Optional<Content> mostViewedInGenre(Genre genre) {
        ArrayList<Content> result = new ArrayList<>();
        for (Content content : catalog) {
            if (content.getGenre() == genre) {
                result.add(content);
            }
        }

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Collections.sort(result, Comparator.comparing
                        (Content::getViews).reversed()
                                .thenComparing(Content::getTitle, String.CASE_INSENSITIVE_ORDER)
        );
        return Optional.of(result.get(0));
    }

    // removeById finder en film ud fra id og hvis der er en film fjerner vi den fra cataloget med remove.
    public boolean removeById(int id) {
        Optional<Content> maybeContent = findById(id);
        if (maybeContent.isPresent()) {
            return catalog.remove(maybeContent.get());
        }

        return false;
    }
}
