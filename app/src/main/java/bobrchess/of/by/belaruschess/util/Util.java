package bobrchess.of.by.belaruschess.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by Igor on 03.05.2018.
 */

public class Util {
    public static String getEncodedPassword(String password) {
        return new String(Hex.encodeHex(DigestUtils.md5(password)));
    }
}
