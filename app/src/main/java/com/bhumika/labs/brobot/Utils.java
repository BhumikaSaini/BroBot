package com.bhumika.labs.brobot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String formatDateTime(long createdAt) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.US);
        return sdf.format(new Date(createdAt));
    }
}
