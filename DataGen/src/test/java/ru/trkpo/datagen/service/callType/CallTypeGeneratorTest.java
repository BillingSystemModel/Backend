package ru.trkpo.datagen.service.callType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class CallTypeGeneratorTest {

    private static final double incomingCallChance = 0.5;
    private static final String incomingCallCode = "02";
    private static final String outcomingCallCode = "01";
    private static final CallTypeGenerator callTypeGenerator = new CallTypeGeneratorImpl();

    @BeforeAll
    static void setUpClass() {
        setField(callTypeGenerator, "incomingCallChance", incomingCallChance);
        setField(callTypeGenerator, "incomingCallCode", incomingCallCode);
        setField(callTypeGenerator, "outcomingCallCode", outcomingCallCode);
    }

    @Test
    void testGenerateCallTypeShouldReturnCallTypeCodeString() {
        // Arrange
        String callTypeCode;
        // Act
        callTypeCode = callTypeGenerator.generateCallType();
        // Assert
        assertTrue(
                callTypeCode.equalsIgnoreCase(incomingCallCode) ||
                        callTypeCode.equalsIgnoreCase(outcomingCallCode)
        );
    }

    @Test
    void testGenerateCallTypeDoesNotReturnNullOrEmptyString() {
        // Arrange
        String callTypeCode;
        // Act
        callTypeCode = callTypeGenerator.generateCallType();
        // Assert
        assertTrue(callTypeCode != null && !callTypeCode.isEmpty());
    }
}
