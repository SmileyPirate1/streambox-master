package streambox;

import dk.mikkel.streambox.Content;
import dk.mikkel.streambox.Genre;
import dk.mikkel.streambox.StreamBoxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StreamBoxServiceTest {

    private StreamBoxService service;

    @BeforeEach
    void setup() {
        service = new StreamBoxService();
    }

    @Test
    void newContent_shouldHaveIdAndZeroViews() {
        Content c = service.addContent("Arcane", Genre.SERIES, 42, 15);
        assertTrue(c.getId() > 0);
        assertEquals(0, c.getViews());
    }

    @Test
    void addContent_shouldStoreInCatalog() {
        service.addContent("Dune", Genre.MOVIE, 155, 11);
        service.addContent("CS2 Major", Genre.LIVE, 180, 0);

        List<Content> catalog = service.getCatalog();
        assertEquals(2, catalog.size());
        assertEquals("Dune", catalog.get(0).getTitle());
        assertEquals("CS2 Major", catalog.get(1).getTitle());
    }

    @Test
    void addContent_shouldRejectBlankTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addContent("   ", Genre.MOVIE, 100, 11));
    }

    @Test
    void addContent_shouldRejectNullGenre() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addContent("Something", null, 100, 11));
    }

    @Test
    void addContent_shouldRejectNonPositiveLength() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addContent("Short", Genre.MOVIE, 0, 11));

        assertThrows(IllegalArgumentException.class,
                () -> service.addContent("Short", Genre.MOVIE, -5, 11));
    }

    @Test
    void addContent_shouldRejectTooLongLength() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addContent("Endless", Genre.MOVIE, 601, 11));
    }

    @Test
    void addContent_shouldRejectInvalidAgeRating() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addContent("Weird", Genre.MOVIE, 100, 12));
    }

    @Test
    void play_shouldIncreaseViews_whenAgeIsEnough() {
        Content c = service.addContent("John Wick", Genre.MOVIE, 101, 15);

        boolean ok = service.play(c.getId(), 18);

        assertTrue(ok);
        assertEquals(1, service.findById(c.getId()).orElseThrow().getViews());
    }

    @Test
    void play_shouldNotIncreaseViews_whenUnderAge() {
        Content c = service.addContent("John Wick", Genre.MOVIE, 101, 15);

        boolean ok = service.play(c.getId(), 14);

        assertFalse(ok);
        assertEquals(0, service.findById(c.getId()).orElseThrow().getViews());
    }

    @Test
    void play_shouldReturnFalse_whenContentDoesNotExist() {
        assertFalse(service.play(9999, 18));
    }

    @Test
    void play_shouldRejectNegativeUserAge() {
        Content c = service.addContent("Arcane", Genre.SERIES, 42, 15);
        assertThrows(IllegalArgumentException.class, () -> service.play(c.getId(), -1));
    }

    @Test
    void findByGenre_shouldReturnOnlyThatGenre_sortedByTitleAsc() {
        service.addContent("Zeta Stream", Genre.LIVE, 60, 0);
        service.addContent("Alpha Stream", Genre.LIVE, 60, 0);
        service.addContent("Dune", Genre.MOVIE, 155, 11);

        List<Content> live = service.findByGenre(Genre.LIVE);

        assertEquals(2, live.size());
        assertEquals("Alpha Stream", live.get(0).getTitle());
        assertEquals("Zeta Stream", live.get(1).getTitle());
    }

    @Test
    void totalRuntimeByGenre_shouldSumLengths() {
        service.addContent("A", Genre.SERIES, 40, 11);
        service.addContent("B", Genre.SERIES, 50, 11);
        service.addContent("C", Genre.MOVIE, 100, 11);

        assertEquals(90, service.totalRuntimeByGenre(Genre.SERIES));
        assertEquals(100, service.totalRuntimeByGenre(Genre.MOVIE));
        assertEquals(0, service.totalRuntimeByGenre(Genre.DOCUMENTARY));
    }

    @Test
    void topTrending_shouldSortByViewsDesc_thenTitleAsc_andLimitN() {
        Content a = service.addContent("Alpha", Genre.MOVIE, 90, 0);
        Content b = service.addContent("Beta", Genre.MOVIE, 90, 0);
        Content c = service.addContent("Gamma", Genre.MOVIE, 90, 0);

        service.play(b.getId(), 20); // Beta: 1
        service.play(b.getId(), 20); // Beta: 2
        service.play(a.getId(), 20); // Alpha: 1
        service.play(c.getId(), 20); // Gamma: 1

        List<Content> top2 = service.topTrending(2);

        assertEquals(2, top2.size());
        assertEquals("Beta", top2.get(0).getTitle());   // 2 views
        assertEquals("Alpha", top2.get(1).getTitle());  // 1 view, title asc mellem (Alpha, Gamma)
    }

    @Test
    void topTrending_shouldRejectNonPositiveN() {
        assertThrows(IllegalArgumentException.class, () -> service.topTrending(0));
        assertThrows(IllegalArgumentException.class, () -> service.topTrending(-2));
    }

    @Test
    void mostViewedInGenre_shouldReturnEmptyIfNone() {
        Optional<Content> res = service.mostViewedInGenre(Genre.MOVIE);
        assertTrue(res.isEmpty());
    }

    @Test
    void mostViewedInGenre_shouldReturnHighestViews_andTieBreakByTitleAsc() {
        Content a = service.addContent("Alpha", Genre.MOVIE, 90, 0);
        Content b = service.addContent("Beta", Genre.MOVIE, 90, 0);

        service.play(a.getId(), 20); // 1
        service.play(b.getId(), 20); // 1

        // tie: Alpha skal vinde pga title asc
        Content best = service.mostViewedInGenre(Genre.MOVIE).orElseThrow();
        assertEquals("Alpha", best.getTitle());
    }

    @Test
    void remove_shouldRemoveContent_andThenFindByIdIsEmpty() {
        Content c = service.addContent("Dune", Genre.MOVIE, 155, 11);

        assertTrue(service.removeById(c.getId()));
        assertTrue(service.findById(c.getId()).isEmpty());
        assertEquals(0, service.getCatalog().size());
    }

    @Test
    void remove_shouldReturnFalse_whenNotFound() {
        assertFalse(service.removeById(12345));
    }
}
