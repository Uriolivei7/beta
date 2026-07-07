package com.facebook.hermes.intl;

import android.icu.text.DateFormat;
import android.icu.text.NumberingSystem;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.icu.util.ULocale;
import com.facebook.hermes.intl.b;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class i implements com.facebook.hermes.intl.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private DateFormat f5903a = null;

    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f5904a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f5905b;

        static {
            int[] iArr = new int[b.k.values().length];
            f5905b = iArr;
            try {
                iArr[b.k.FULL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5905b[b.k.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5905b[b.k.MEDIUM.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5905b[b.k.SHORT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5905b[b.k.UNDEFINED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            int[] iArr2 = new int[b.EnumC0090b.values().length];
            f5904a = iArr2;
            try {
                iArr2[b.EnumC0090b.FULL.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f5904a[b.EnumC0090b.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f5904a[b.EnumC0090b.MEDIUM.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f5904a[b.EnumC0090b.SHORT.ordinal()] = 4;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f5904a[b.EnumC0090b.UNDEFINED.ordinal()] = 5;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    private static class b {
        public static String a(String str) {
            StringBuilder sb = new StringBuilder();
            boolean z3 = false;
            for (int i3 = 0; i3 < str.length(); i3++) {
                char cCharAt = str.charAt(i3);
                if (cCharAt == '\'') {
                    z3 = !z3;
                } else if (!z3 && ((cCharAt >= 'A' && cCharAt <= 'Z') || (cCharAt >= 'a' && cCharAt <= 'z'))) {
                    sb.append(str.charAt(i3));
                }
            }
            return sb.toString();
        }
    }

    i() {
    }

    private static String i(B0.b bVar, b.EnumC0090b enumC0090b, b.k kVar) {
        return enumC0090b == b.EnumC0090b.UNDEFINED ? ((SimpleDateFormat) DateFormat.getTimeInstance(m(kVar), (ULocale) bVar.h())).toLocalizedPattern() : kVar == b.k.UNDEFINED ? ((SimpleDateFormat) DateFormat.getDateInstance(l(enumC0090b), (ULocale) bVar.h())).toLocalizedPattern() : ((SimpleDateFormat) DateFormat.getDateTimeInstance(l(enumC0090b), m(kVar), (ULocale) bVar.h())).toLocalizedPattern();
    }

    private static String j(B0.b bVar, b.m mVar, b.d dVar, b.n nVar, b.i iVar, b.c cVar, b.f fVar, b.h hVar, b.j jVar, b.l lVar, b.g gVar, b.EnumC0090b enumC0090b, b.k kVar, Object obj) {
        StringBuilder sb = new StringBuilder();
        if (enumC0090b == b.EnumC0090b.UNDEFINED && kVar == b.k.UNDEFINED) {
            sb.append(mVar.b());
            sb.append(dVar.b());
            sb.append(nVar.b());
            sb.append(iVar.b());
            sb.append(cVar.b());
            if (gVar == b.g.H11 || gVar == b.g.H12) {
                sb.append(fVar.b());
            } else {
                sb.append(fVar.c());
            }
            sb.append(hVar.b());
            sb.append(jVar.b());
            sb.append(lVar.b());
        } else {
            sb.append(i(bVar, enumC0090b, kVar));
            HashMap mapB = bVar.b();
            if (mapB.containsKey("hc")) {
                String str = (String) mapB.get("hc");
                if (str == "h11" || str == "h12") {
                    k(sb, new char[]{'H', 'K', 'k'}, 'h');
                } else if (str == "h23" || str == "h24") {
                    k(sb, new char[]{'h', 'H', 'K'}, 'k');
                }
            }
            if (gVar == b.g.H11 || gVar == b.g.H12) {
                k(sb, new char[]{'H', 'K', 'k'}, 'h');
            } else if (gVar == b.g.H23 || gVar == b.g.H24) {
                k(sb, new char[]{'h', 'H', 'K'}, 'k');
            }
            if (!B0.d.n(obj) && !B0.d.j(obj)) {
                if (B0.d.e(obj)) {
                    k(sb, new char[]{'H', 'K', 'k'}, 'h');
                } else {
                    k(sb, new char[]{'h', 'H', 'K'}, 'k');
                }
            }
        }
        return sb.toString();
    }

    private static void k(StringBuilder sb, char[] cArr, char c4) {
        for (int i3 = 0; i3 < sb.length(); i3++) {
            int length = cArr.length;
            int i4 = 0;
            while (true) {
                if (i4 < length) {
                    if (sb.charAt(i3) == cArr[i4]) {
                        sb.setCharAt(i3, c4);
                        break;
                    }
                    i4++;
                }
            }
        }
    }

    static int l(b.EnumC0090b enumC0090b) throws B0.e {
        int i3 = a.f5904a[enumC0090b.ordinal()];
        if (i3 == 1) {
            return 0;
        }
        if (i3 == 2) {
            return 1;
        }
        if (i3 == 3) {
            return 2;
        }
        if (i3 == 4) {
            return 3;
        }
        throw new B0.e("Invalid DateStyle: " + enumC0090b.toString());
    }

    static int m(b.k kVar) throws B0.e {
        int i3 = a.f5905b[kVar.ordinal()];
        if (i3 == 1) {
            return 0;
        }
        if (i3 == 2) {
            return 1;
        }
        if (i3 == 3) {
            return 2;
        }
        if (i3 == 4) {
            return 3;
        }
        throw new B0.e("Invalid DateStyle: " + kVar.toString());
    }

    @Override // com.facebook.hermes.intl.b
    public AttributedCharacterIterator a(double d4) {
        return this.f5903a.formatToCharacterIterator(Double.valueOf(d4));
    }

    @Override // com.facebook.hermes.intl.b
    public String b(double d4) {
        return this.f5903a.format(new Date((long) d4));
    }

    @Override // com.facebook.hermes.intl.b
    public String c(B0.b bVar) {
        return NumberingSystem.getInstance((ULocale) bVar.h()).getName();
    }

    @Override // com.facebook.hermes.intl.b
    public String d(B0.b bVar) {
        return B0.i.d(DateFormat.getDateInstance(3, (ULocale) bVar.h()).getCalendar().getType());
    }

    @Override // com.facebook.hermes.intl.b
    public String e(AttributedCharacterIterator.Attribute attribute, String str) {
        if (attribute == DateFormat.Field.DAY_OF_WEEK) {
            return "weekday";
        }
        if (attribute == DateFormat.Field.ERA) {
            return "era";
        }
        if (attribute != DateFormat.Field.YEAR) {
            return attribute == DateFormat.Field.MONTH ? "month" : attribute == DateFormat.Field.DAY_OF_MONTH ? "day" : (attribute == DateFormat.Field.HOUR0 || attribute == DateFormat.Field.HOUR1 || attribute == DateFormat.Field.HOUR_OF_DAY0 || attribute == DateFormat.Field.HOUR_OF_DAY1) ? "hour" : attribute == DateFormat.Field.MINUTE ? "minute" : attribute == DateFormat.Field.SECOND ? "second" : attribute == DateFormat.Field.TIME_ZONE ? "timeZoneName" : attribute == DateFormat.Field.AM_PM ? "dayPeriod" : attribute.toString().equals("android.icu.text.DateFormat$Field(related year)") ? "relatedYear" : "literal";
        }
        try {
            Double.parseDouble(str);
            return "year";
        } catch (NumberFormatException unused) {
            return "yearName";
        }
    }

    @Override // com.facebook.hermes.intl.b
    public String f(B0.b bVar) {
        return Calendar.getInstance((ULocale) bVar.h()).getTimeZone().getID();
    }

    @Override // com.facebook.hermes.intl.b
    public b.g g(B0.b bVar) {
        try {
            String strA = b.a(((SimpleDateFormat) DateFormat.getTimeInstance(0, (ULocale) bVar.h())).toPattern());
            return strA.contains(String.valueOf('h')) ? b.g.H12 : strA.contains(String.valueOf('K')) ? b.g.H11 : strA.contains(String.valueOf('H')) ? b.g.H23 : b.g.H24;
        } catch (ClassCastException unused) {
            return b.g.H24;
        }
    }

    @Override // com.facebook.hermes.intl.b
    public void h(B0.b bVar, String str, String str2, b.e eVar, b.m mVar, b.d dVar, b.n nVar, b.i iVar, b.c cVar, b.f fVar, b.h hVar, b.j jVar, b.l lVar, b.g gVar, Object obj, b.EnumC0090b enumC0090b, b.k kVar, Object obj2) throws B0.e {
        Calendar calendar;
        String strJ = j(bVar, mVar, dVar, nVar, iVar, cVar, fVar, hVar, jVar, lVar, gVar, enumC0090b, kVar, obj2);
        if (str.isEmpty()) {
            calendar = null;
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.add(B0.d.h(str));
            B0.b bVarE = bVar.e();
            bVarE.g("ca", arrayList);
            calendar = Calendar.getInstance((ULocale) bVarE.h());
        }
        if (!str2.isEmpty()) {
            try {
                if (NumberingSystem.getInstanceByName(B0.d.h(str2)) == null) {
                    throw new B0.e("Invalid numbering system: " + str2);
                }
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(B0.d.h(str2));
                bVar.g("nu", arrayList2);
            } catch (RuntimeException unused) {
                throw new B0.e("Invalid numbering system: " + str2);
            }
        }
        if (calendar != null) {
            this.f5903a = DateFormat.getPatternInstance(calendar, strJ, (ULocale) bVar.h());
        } else {
            this.f5903a = DateFormat.getPatternInstance(strJ, (ULocale) bVar.h());
        }
        if (B0.d.n(obj) || B0.d.j(obj)) {
            return;
        }
        this.f5903a.setTimeZone(TimeZone.getTimeZone(B0.d.h(obj)));
    }
}
