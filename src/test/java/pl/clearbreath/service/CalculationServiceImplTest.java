package pl.clearbreath.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.clearbreath.service.impl.CalculationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculationServiceImplTest {

    private CalculationService calculationService;

    @BeforeEach
    public void setUp() {
        calculationService = new CalculationServiceImpl();
    }

    @Test
    public void testHaversine_SameLocation() {
        double lat = 52.2297;
        double lng = 21.0122;
        double distance = calculationService.haversine(lat, lng, lat, lng);
        assertEquals(0.0, distance, "Distance between the same points should be 0.");
    }

    @Test
    public void testHaversine_DifferentLocations() {
        double warsawLat = 52.2297;
        double warsawLng = 21.0122;
        double berlinLat = 52.5200;
        double berlinLng = 13.4050;

        double distance = calculationService.haversine(warsawLat, warsawLng, berlinLat, berlinLng);

        assertEquals(518, distance, 1.0, "Distance between Warsaw and Berlin should be around 518 km.");
    }

    @Test
    public void testHaversine_LongDistance() {
        double nyLat = 40.7128;
        double nyLng = -74.0060;
        double londonLat = 51.5074;
        double londonLng = -0.1278;

        double distance = calculationService.haversine(nyLat, nyLng, londonLat, londonLng);

        assertEquals(5570, distance, 50.0, "Distance between New York and London should be around 5570 km.");
    }
}
