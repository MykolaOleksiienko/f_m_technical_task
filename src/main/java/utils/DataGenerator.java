package utils;

import org.apache.commons.lang3.RandomStringUtils;

public class DataGenerator {

    /**
     * For example, generateAlphaNumericStringWithLength(6)
     *
     * @param size value of the length
     * @return aSEs9W
     */
    public static String generateAlphaNumericStringWithLength(int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    /**
     * For example, generateAlphaStringWithLength(6)
     *
     * @param size value of the length
     * @return myJnyj
     */
    public static String generateAlphaStringWithLength(int size) {
        return RandomStringUtils.randomAlphabetic(size);
    }

    /**
     * For example, generateRandomNumericStringWithLength(6)
     *
     * @param size value of the length
     * @return 595524
     */
    public static String generateRandomNumericStringWithLength(int size) {
        return RandomStringUtils.randomNumeric(size);
    }

}
