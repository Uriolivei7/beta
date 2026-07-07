package com.facebook.hermes.unicode;

import java.text.Collator;
import java.text.DateFormat;
import java.text.Normalizer;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class AndroidUnicodeUtils {
    public static String convertToCase(String str, int i3, boolean z3) {
        Locale locale = z3 ? Locale.getDefault() : Locale.ENGLISH;
        if (i3 == 0) {
            return str.toUpperCase(locale);
        }
        if (i3 == 1) {
            return str.toLowerCase(locale);
        }
        throw new RuntimeException("Invalid target case");
    }

    public static String dateFormat(double d4, boolean z3, boolean z4) {
        DateFormat timeInstance;
        if (z3 && z4) {
            timeInstance = DateFormat.getDateTimeInstance(2, 2);
        } else if (z3) {
            timeInstance = DateFormat.getDateInstance(2);
        } else {
            if (!z4) {
                throw new RuntimeException("Bad dateFormat configuration");
            }
            timeInstance = DateFormat.getTimeInstance(2);
        }
        return timeInstance.format(Long.valueOf((long) d4)).toString();
    }

    public static int localeCompare(String str, String str2) {
        return Collator.getInstance().compare(str, str2);
    }

    public static String normalize(String str, int i3) {
        if (i3 == 0) {
            return Normalizer.normalize(str, Normalizer.Form.NFC);
        }
        if (i3 == 1) {
            return Normalizer.normalize(str, Normalizer.Form.NFD);
        }
        if (i3 == 2) {
            return Normalizer.normalize(str, Normalizer.Form.NFKC);
        }
        if (i3 == 3) {
            return Normalizer.normalize(str, Normalizer.Form.NFKD);
        }
        throw new RuntimeException("Invalid form");
    }
}
