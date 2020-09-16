package com.dp.ishare.util;

import com.dp.ishare.constants.CommonConstants;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

public class FileUtil {

    private static String[] CHARS = new String[] { "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    public static String getFileId(MultipartFile file, String userId) {
        try {
            byte[] fileBytes = file.getBytes();
            System.out.println(fileBytes.length);
            String hex = DigestUtils.md5DigestAsHex(fileBytes);
            hex = DigestUtils.md5DigestAsHex((hex + userId).getBytes());
            System.out.println(hex);

            StringBuilder outChars = new StringBuilder();
            String subHex = hex.substring(16, 24);
            long idx = Long.valueOf("3FFFFFFF", 16) & Long.valueOf(subHex, 16);
            for (int k = 0; k < 6; k++) {
                int index = (int) (Long.valueOf("0000003D", 16) & idx);
                outChars.append(CHARS[index]); idx = idx >> 5;
            }

            return outChars.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String generateExtractCode() {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i=0; i<4; i++) {
            int next = random.nextInt(CHARS.length);
            result.append(CHARS[next].toLowerCase());
        }
        return result.toString();
    }

    public static Date getExpireDate (Integer effectiveDays) {
        Date now = new Date();
        if (effectiveDays == null) {
            effectiveDays = CommonConstants.DEFAULT_EFFECTIVE_TIME;
        }

        return DateUtils.addDays(now, effectiveDays);
    }

}
