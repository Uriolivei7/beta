package com.facebook.react.views.text;

import java.text.BreakIterator;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public abstract class v {

    public /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f8061a;

        static {
            int[] iArr = new int[u.values().length];
            try {
                iArr[u.f8055d.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[u.f8056e.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[u.f8057f.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            f8061a = iArr;
        }
    }

    public static final String a(String str, u uVar) {
        D2.h.f(str, "<this>");
        int i3 = uVar == null ? -1 : a.f8061a[uVar.ordinal()];
        if (i3 == 1) {
            Locale locale = Locale.getDefault();
            D2.h.e(locale, "getDefault(...)");
            String upperCase = str.toUpperCase(locale);
            D2.h.e(upperCase, "toUpperCase(...)");
            return upperCase;
        }
        if (i3 == 2) {
            Locale locale2 = Locale.getDefault();
            D2.h.e(locale2, "getDefault(...)");
            String lowerCase = str.toLowerCase(locale2);
            D2.h.e(lowerCase, "toLowerCase(...)");
            return lowerCase;
        }
        if (i3 != 3) {
            return str;
        }
        BreakIterator wordInstance = BreakIterator.getWordInstance();
        wordInstance.setText(str);
        StringBuilder sb = new StringBuilder(str.length());
        int iFirst = wordInstance.first();
        int next = wordInstance.next();
        while (true) {
            int i4 = next;
            int i5 = iFirst;
            iFirst = i4;
            if (iFirst == -1) {
                String string = sb.toString();
                D2.h.c(string);
                return string;
            }
            String strSubstring = str.substring(i5, iFirst);
            D2.h.e(strSubstring, "substring(...)");
            if (strSubstring.length() > 0) {
                char upperCase2 = Character.toUpperCase(strSubstring.charAt(0));
                String strSubstring2 = strSubstring.substring(1);
                D2.h.e(strSubstring2, "substring(...)");
                strSubstring = upperCase2 + strSubstring2;
            }
            sb.append(strSubstring);
            next = wordInstance.next();
        }
    }
}
