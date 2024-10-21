package pl.clearbreath.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.clearbreath.TestUtils;
import pl.clearbreath.model.Marker;
import pl.clearbreath.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MarkerRepositoryTest {

    @Mock
    private MarkerRepository markerRepository;

    private User testUser;
    private Marker testMarker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = TestUtils.createUser();
        testMarker = TestUtils.createMarker();
    }

    @Test
    public void testFindByUser() {
        when(markerRepository.findByUser(testUser)).thenReturn(Arrays.asList(testMarker));

        List<Marker> markers = markerRepository.findByUser(testUser);

        assertEquals(1, markers.size());
        assertEquals(testMarker.getLat(), markers.get(0).getLat());
        assertEquals(testMarker.getLng(), markers.get(0).getLng());
    }

    @Test
    public void testFindByLatAndLngAndUser() {
        when(markerRepository.findByLatAndLngAndUser(testMarker.getLat(), testMarker.getLng(), testUser))
                .thenReturn(Optional.of(testMarker));

        Optional<Marker> foundMarker = markerRepository.findByLatAndLngAndUser(testMarker.getLat(), testMarker.getLng(), testUser);

        assertTrue(foundMarker.isPresent());
        assertEquals(testMarker.getLat(), foundMarker.get().getLat());
        assertEquals(testMarker.getLng(), foundMarker.get().getLng());
    }

    @Test
    public void testDeleteByUser() {
        doNothing().when(markerRepository).deleteByUser(testUser);

        markerRepository.deleteByUser(testUser);

        verify(markerRepository, times(1)).deleteByUser(testUser);
    }
}
