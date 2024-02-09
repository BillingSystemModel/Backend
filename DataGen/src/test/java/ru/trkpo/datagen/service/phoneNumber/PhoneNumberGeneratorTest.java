package ru.trkpo.datagen.service.phoneNumber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.trkpo.common.data.entity.PhoneNumber;
import ru.trkpo.common.service.phoneNumber.PhoneNumberService;

import java.util.Random;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberGeneratorTest {

    @Mock
    private PhoneNumberService phoneNumberServiceMock;

    @Mock
    private Random randomMock;

    @InjectMocks
    private PhoneNumberGeneratorImpl underTestGenerator;

    private static final double existingNumberChance = 0.7;
    private static final String PHONE_NUMBER_REGEX = "^7[0-9]{10}$";

    @BeforeEach
    void setUpClass() {
        setField(underTestGenerator, "existingNumberChance", existingNumberChance);
        setField(underTestGenerator, "random", randomMock);
    }

    @Test
    void testGenerateNumberShouldReturnRandomExistingPhoneNumberString() {
        // Arrange
        String phoneNumber = "71112223344";
        PhoneNumber phoneNumberMock = Mockito.mock(PhoneNumber.class);
        // Act
        when(randomMock.nextDouble()).thenReturn(existingNumberChance - 0.0001);
        when(phoneNumberServiceMock.findRandom()).thenReturn(phoneNumberMock);
        when(phoneNumberMock.getPhoneNumber()).thenReturn(phoneNumber);
        String resultPhoneNumber = underTestGenerator.generateNumber();
        // Assert
        assertThat(resultPhoneNumber).isNotNull().isNotEmpty();
        assertTrue(Pattern.matches(PHONE_NUMBER_REGEX, resultPhoneNumber));
    }

    @Test
    void testGenerateNumberShouldReturnNewPhoneNumberString() {
        // Arrange
        long randomLong = 1112223344L;
        // Act
        when(randomMock.nextDouble()).thenReturn(existingNumberChance + 0.0001);
        when(randomMock.nextLong(1000000000L, 9999999999L)).thenReturn(randomLong);
        String resultPhoneNumber = underTestGenerator.generateNumber();
        // Assert
        assertThat(resultPhoneNumber).isNotNull().isNotEmpty();
        assertTrue(Pattern.matches(PHONE_NUMBER_REGEX, resultPhoneNumber));
    }

    @Test
    void testGenerateNewNumberShouldReturnPhoneNumberString() {
        // Arrange
        long randomLong = 1112223344L;
        // Act
        when(randomMock.nextLong(1000000000L, 9999999999L)).thenReturn(randomLong);
        String resultPhoneNumber = underTestGenerator.generateNewNumber();
        // Assert
        assertThat(resultPhoneNumber).isNotNull().isNotEmpty();
        assertTrue(Pattern.matches(PHONE_NUMBER_REGEX, resultPhoneNumber));
    }
}
