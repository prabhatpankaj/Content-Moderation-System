package com.techbellys.utils;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public final class CommonUtil {

    private static final String COUNTRY_CODE = "+91";
    private static final String TEN_DIGIT_REGEX = "\\d{10}";

    // Private constructor to prevent instantiation
    private CommonUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static String processMobileNumber(String mobileNumber) {
        Objects.requireNonNull(mobileNumber, "Mobile number cannot be null");

        // Remove whitespace if any
        mobileNumber = mobileNumber.trim();

        // Check if the number starts with +91
        if (mobileNumber.startsWith(COUNTRY_CODE)) {
            log.debug("Mobile number already starts with country code: {}", mobileNumber);
            return mobileNumber;
        }

        // Check if the number is of length 10
        if (mobileNumber.length() == 10 && mobileNumber.matches(TEN_DIGIT_REGEX)) {
            String formattedNumber = COUNTRY_CODE + mobileNumber;
            log.debug("Formatted mobile number: {}", formattedNumber);
            return formattedNumber;
        }

        // Invalid number
        log.error("Invalid mobile number format: {}", mobileNumber);
        throw new IllegalArgumentException("Invalid mobile number format: " + mobileNumber);
    }
}
