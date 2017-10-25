package pl.cubesoft.tigerspiketest.provider.flickr;

import java.util.Random;

/**
 * Created by CUBESOFT on 21.09.2017.
 */

class RandomStringUtils {
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    public static String randomAlphanumeric(int size) {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(size);
        for(int i=0;i<size;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
