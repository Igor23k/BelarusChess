package bobrchess.of.by.belaruschess.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Igor on 03.05.2018.
 */

public class Util {
    public static String getEncodedPassword(String password) {
        return DigestUtils.md5Hex(password);
    }
}
