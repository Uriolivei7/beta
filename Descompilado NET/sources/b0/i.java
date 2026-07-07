package B0;

import android.icu.text.Collator;
import android.icu.text.NumberingSystem;
import android.icu.util.Calendar;
import android.icu.util.ULocale;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f62a = "calendar";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static String f63b = "ca";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static String f64c = "numbers";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static String f65d = "nu";

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public static String f66e = "hours";

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public static String f67f = "hc";

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    public static String f68g = "collation";

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static String f69h = "co";

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    public static String f70i = "colnumeric";

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    public static String f71j = "kn";

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    public static String f72k = "colcasefirst";

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static String f73l = "kf";

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private static HashMap f74m = new a();

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private static HashMap f75n = new b();

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final Map f76o = new c();

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static Map f77p = new d();

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private static Map f78q = new e();

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private static Map f79r = new f();

    class a extends HashMap {
        a() {
            put(i.f63b, i.f62a);
            put(i.f65d, i.f64c);
            put(i.f67f, i.f66e);
            put(i.f69h, i.f68g);
            put(i.f71j, i.f70i);
            put(i.f73l, i.f72k);
        }
    }

    class b extends HashMap {
        b() {
            put(i.f62a, i.f63b);
            put(i.f64c, i.f65d);
            put(i.f66e, i.f67f);
            put(i.f68g, i.f69h);
            put(i.f70i, i.f71j);
            put(i.f72k, i.f73l);
        }
    }

    class c extends HashMap {
        c() {
            put("dictionary", "dict");
            put("phonebook", "phonebk");
            put("traditional", "trad");
            put("gb2312han", "gb2312");
        }
    }

    class d extends HashMap {
        d() {
            put("gregorian", "gregory");
        }
    }

    class e extends HashMap {
        e() {
            put("traditional", "traditio");
        }
    }

    class f extends HashMap {
        f() {
            put("nu", new String[]{"adlm", "ahom", "arab", "arabext", "bali", "beng", "bhks", "brah", "cakm", "cham", "deva", "diak", "fullwide", "gong", "gonm", "gujr", "guru", "hanidec", "hmng", "hmnp", "java", "kali", "khmr", "knda", "lana", "lanatham", "laoo", "latn", "lepc", "limb", "mathbold", "mathdbl", "mathmono", "mathsanb", "mathsans", "mlym", "modi", "mong", "mroo", "mtei", "mymr", "mymrshan", "mymrtlng", "newa", "nkoo", "olck", "orya", "osma", "rohg", "saur", "segment", "shrd", "sind", "sinh", "sora", "sund", "takr", "talu", "tamldec", "telu", "thai", "tibt", "tirh", "vaii", "wara", "wcho"});
            put("co", new String[]{"big5han", "compat", "dict", "direct", "ducet", "emoji", "eor", "gb2312", "phonebk", "phonetic", "pinyin", "reformed", "searchjl", "stroke", "trad", "unihan", "zhuyin"});
            put("ca", new String[]{"buddhist", "chinese", "coptic", "dangi", "ethioaa", "ethiopic", "gregory", "hebrew", "indian", "islamic", "islamic-umalqura", "islamic-tbla", "islamic-civil", "islamic-rgsa", "iso8601", "japanese", "persian", "roc"});
        }
    }

    public static String a(String str) {
        return f74m.containsKey(str) ? (String) f74m.get(str) : str;
    }

    public static String b(String str) {
        return f75n.containsKey(str) ? (String) f75n.get(str) : str;
    }

    public static boolean c(String str, String str2, B0.b bVar) {
        ULocale uLocale = (ULocale) bVar.h();
        String[] availableNames = new String[0];
        if (str.equals("co")) {
            if (str2.equals("standard") || str2.equals("search")) {
                return false;
            }
            availableNames = Collator.getKeywordValuesForLocale("co", uLocale, false);
        } else if (str.equals("ca")) {
            availableNames = Calendar.getKeywordValuesForLocale("ca", uLocale, false);
        } else if (str.equals("nu")) {
            availableNames = NumberingSystem.getAvailableNames();
        }
        if (availableNames.length == 0) {
            return true;
        }
        return Arrays.asList(availableNames).contains(str2);
    }

    public static String d(String str) {
        return !f77p.containsKey(str) ? str : (String) f77p.get(str);
    }

    public static String e(String str) {
        Map map = f76o;
        return !map.containsKey(str) ? str : (String) map.get(str);
    }

    public static Object f(String str, Object obj) {
        return (str.equals("ca") && B0.d.m(obj)) ? d((String) obj) : (str.equals("nu") && B0.d.m(obj)) ? g((String) obj) : (str.equals("co") && B0.d.m(obj)) ? e((String) obj) : (str.equals("kn") && B0.d.m(obj) && obj.equals("yes")) ? B0.d.r("true") : ((str.equals("kn") || str.equals("kf")) && B0.d.m(obj) && obj.equals("no")) ? B0.d.r("false") : obj;
    }

    public static String g(String str) {
        return !f78q.containsKey(str) ? str : (String) f78q.get(str);
    }
}
