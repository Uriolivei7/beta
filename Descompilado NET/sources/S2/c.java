package S2;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import r2.r;

/* JADX INFO: loaded from: classes.dex */
public abstract class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final a f2317a = new a();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String[] f2318b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final DateFormat[] f2319c;

    public static final class a extends ThreadLocal {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public DateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            simpleDateFormat.setLenient(false);
            simpleDateFormat.setTimeZone(N2.c.f1407f);
            return simpleDateFormat;
        }
    }

    static {
        String[] strArr = {"EEE, dd MMM yyyy HH:mm:ss zzz", "EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE MMM d yyyy HH:mm:ss z"};
        f2318b = strArr;
        f2319c = new DateFormat[strArr.length];
    }

    public static final Date a(String str) {
        D2.h.f(str, "$this$toHttpDateOrNull");
        if (str.length() == 0) {
            return null;
        }
        ParsePosition parsePosition = new ParsePosition(0);
        Date date = ((DateFormat) f2317a.get()).parse(str, parsePosition);
        if (parsePosition.getIndex() == str.length()) {
            return date;
        }
        String[] strArr = f2318b;
        synchronized (strArr) {
            try {
                int length = strArr.length;
                for (int i3 = 0; i3 < length; i3++) {
                    DateFormat[] dateFormatArr = f2319c;
                    DateFormat simpleDateFormat = dateFormatArr[i3];
                    if (simpleDateFormat == null) {
                        simpleDateFormat = new SimpleDateFormat(f2318b[i3], Locale.US);
                        simpleDateFormat.setTimeZone(N2.c.f1407f);
                        dateFormatArr[i3] = simpleDateFormat;
                    }
                    parsePosition.setIndex(0);
                    Date date2 = simpleDateFormat.parse(str, parsePosition);
                    if (parsePosition.getIndex() != 0) {
                        return date2;
                    }
                }
                r rVar = r.f10584a;
                return null;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static final String b(Date date) {
        D2.h.f(date, "$this$toHttpDateString");
        String str = ((DateFormat) f2317a.get()).format(date);
        D2.h.e(str, "STANDARD_DATE_FORMAT.get().format(this)");
        return str;
    }
}
