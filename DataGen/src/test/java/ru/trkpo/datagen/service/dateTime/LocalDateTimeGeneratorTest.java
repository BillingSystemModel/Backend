package ru.trkpo.datagen.service.dateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.trkpo.common.data.Pair;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class LocalDateTimeGeneratorTest {

    private static final LocalDateTime lowerBound = LocalDateTime.of(2024, 1, 1, 0, 0);
    private static final LocalDateTime upperBound = LocalDateTime.of(2024, 2, 1, 0, 0);
    private static final Duration maxDuration = Duration.ofHours(1);
    private static final LocalDateTimeGenerator underTestService = new LocalDateTimeGeneratorImpl();

    @BeforeAll
    static void setUpClass() {
        setField(underTestService, "lowerBound", lowerBound);
        setField(underTestService, "upperBound", upperBound);
        setField(underTestService, "maxDuration", maxDuration);
    }

    @Test
    void testGenerateDateTimeShouldReturnDateTimeBetweenLowerAndUpperBound() {
        // Arrange
        Pair<LocalDateTime, LocalDateTime> resultPair;
        // Act
        resultPair = underTestService.generateDateTime();
        // Assert
        assertTrue(resultPair.getFirst().isAfter(lowerBound) && resultPair.getSecond().isBefore(upperBound));
    }

    @Test
    void testGenerateDateTimeShouldReturnResultDurationLessThanMaxDuration() {
        // Arrange
        Pair<LocalDateTime, LocalDateTime> resultPair;
        Duration totalDuration;
        // Act
        resultPair = underTestService.generateDateTime();
        totalDuration = Duration.between(resultPair.getFirst(), resultPair.getSecond());
        // Assert
        assertThat(totalDuration).isLessThan(maxDuration);
    }

    @Test
    void testUpdateDateTimeBoundsShouldIncreaseDateTimeBoundsByOneMonth() {
        // Arrange
        LocalDateTime newLowerBound;
        LocalDateTime newUpperBound;
        // Act
        underTestService.updateDateTimeBoubds();
        newLowerBound = (LocalDateTime) getField(underTestService, "lowerBound");
        newUpperBound = (LocalDateTime) getField(underTestService, "upperBound");
        // Assert
        assertThat(newLowerBound).hasMonth(lowerBound.getMonth().plus(1));
        assertThat(newUpperBound).hasMonth(upperBound.getMonth().plus(1));
    }
}