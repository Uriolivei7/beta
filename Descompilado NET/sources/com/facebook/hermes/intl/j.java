package com.facebook.hermes.intl;

import android.icu.text.CompactDecimalFormat;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.icu.text.MeasureFormat;
import android.icu.text.NumberFormat;
import android.icu.text.NumberingSystem;
import android.icu.util.Currency;
import android.icu.util.Measure;
import android.icu.util.MeasureUnit;
import android.icu.util.ULocale;
import android.os.Build;
import com.facebook.hermes.intl.c;
import java.text.AttributedCharacterIterator;
import java.text.Format;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class j implements c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Format f5906a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private android.icu.text.NumberFormat f5907b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private B0.g f5908c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private c.h f5909d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private MeasureUnit f5910e;

    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f5911a;

        static {
            int[] iArr = new int[c.g.values().length];
            f5911a = iArr;
            try {
                iArr[c.g.NEVER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5911a[c.g.ALWAYS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5911a[c.g.EXCEPTZERO.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    j() {
    }

    public static int n(String str) throws B0.e {
        try {
            return Currency.getInstance(str).getDefaultFractionDigits();
        } catch (IllegalArgumentException unused) {
            throw new B0.e("Invalid currency code !");
        }
    }

    private void o(android.icu.text.NumberFormat numberFormat, B0.b bVar, c.h hVar) {
        this.f5907b = numberFormat;
        this.f5906a = numberFormat;
        this.f5908c = (B0.g) bVar;
        this.f5909d = hVar;
        numberFormat.setRoundingMode(4);
    }

    private static MeasureUnit p(String str) throws B0.e {
        for (MeasureUnit measureUnit : MeasureUnit.getAvailable()) {
            if (!measureUnit.getSubtype().equals(str)) {
                if (measureUnit.getSubtype().equals(measureUnit.getType() + "-" + str)) {
                }
            }
            return measureUnit;
        }
        throw new B0.e("Unknown unit: " + str);
    }

    @Override // com.facebook.hermes.intl.c
    public AttributedCharacterIterator a(double d4) {
        try {
            try {
                Format format = this.f5906a;
                return (!(format instanceof MeasureFormat) || this.f5910e == null) ? format.formatToCharacterIterator(Double.valueOf(d4)) : format.formatToCharacterIterator(new Measure(Double.valueOf(d4), this.f5910e));
            } catch (RuntimeException unused) {
                return android.icu.text.NumberFormat.getInstance(ULocale.forLanguageTag("en")).formatToCharacterIterator(Double.valueOf(d4));
            }
        } catch (NumberFormatException unused2) {
            return android.icu.text.NumberFormat.getInstance(ULocale.getDefault()).formatToCharacterIterator(Double.valueOf(d4));
        } catch (Exception unused3) {
            return android.icu.text.NumberFormat.getInstance(ULocale.forLanguageTag("en")).formatToCharacterIterator(Double.valueOf(d4));
        }
    }

    @Override // com.facebook.hermes.intl.c
    public String b(double d4) {
        try {
            try {
                Format format = this.f5906a;
                return (!(format instanceof MeasureFormat) || this.f5910e == null) ? format.format(Double.valueOf(d4)) : format.format(new Measure(Double.valueOf(d4), this.f5910e));
            } catch (NumberFormatException unused) {
                return android.icu.text.NumberFormat.getInstance(ULocale.getDefault()).format(d4);
            }
        } catch (RuntimeException unused2) {
            return android.icu.text.NumberFormat.getInstance(ULocale.forLanguageTag("en")).format(d4);
        }
    }

    @Override // com.facebook.hermes.intl.c
    public String c(B0.b bVar) {
        return NumberingSystem.getInstance((ULocale) bVar.h()).getName();
    }

    @Override // com.facebook.hermes.intl.c
    public String e(AttributedCharacterIterator.Attribute attribute, double d4) {
        return attribute == NumberFormat.Field.SIGN ? Double.compare(d4, 0.0d) >= 0 ? "plusSign" : "minusSign" : attribute == NumberFormat.Field.INTEGER ? Double.isNaN(d4) ? "nan" : Double.isInfinite(d4) ? "infinity" : "integer" : attribute == NumberFormat.Field.FRACTION ? "fraction" : attribute == NumberFormat.Field.EXPONENT ? "exponentInteger" : attribute == NumberFormat.Field.EXPONENT_SIGN ? "exponentMinusSign" : attribute == NumberFormat.Field.EXPONENT_SYMBOL ? "exponentSeparator" : attribute == NumberFormat.Field.DECIMAL_SEPARATOR ? "decimal" : attribute == NumberFormat.Field.GROUPING_SEPARATOR ? "group" : attribute == NumberFormat.Field.PERCENT ? "percentSign" : attribute == NumberFormat.Field.PERMILLE ? "permilleSign" : attribute == NumberFormat.Field.CURRENCY ? "currency" : attribute.toString().equals("android.icu.text.NumberFormat$Field(compact)") ? "compact" : "literal";
    }

    @Override // com.facebook.hermes.intl.c
    /* JADX INFO: renamed from: m, reason: merged with bridge method [inline-methods] */
    public j g(B0.b bVar, String str, c.h hVar, c.d dVar, c.e eVar, c.b bVar2) throws B0.e {
        if (!str.isEmpty()) {
            try {
                if (NumberingSystem.getInstanceByName(B0.d.h(str)) == null) {
                    throw new B0.e("Invalid numbering system: " + str);
                }
                ArrayList arrayList = new ArrayList();
                arrayList.add(B0.d.h(str));
                bVar.g("nu", arrayList);
            } catch (RuntimeException unused) {
                throw new B0.e("Invalid numbering system: " + str);
            }
        }
        if (eVar == c.e.COMPACT && (hVar == c.h.DECIMAL || hVar == c.h.UNIT)) {
            o(CompactDecimalFormat.getInstance((ULocale) bVar.h(), bVar2 == c.b.SHORT ? CompactDecimalFormat.CompactStyle.SHORT : CompactDecimalFormat.CompactStyle.LONG), bVar, hVar);
        } else {
            android.icu.text.NumberFormat numberFormat = android.icu.text.NumberFormat.getInstance((ULocale) bVar.h(), hVar.b(eVar, dVar));
            if (eVar == c.e.ENGINEERING) {
                numberFormat.setMaximumIntegerDigits(3);
            }
            o(numberFormat, bVar, hVar);
        }
        return this;
    }

    @Override // com.facebook.hermes.intl.c
    /* JADX INFO: renamed from: q, reason: merged with bridge method [inline-methods] */
    public j j(String str, c.EnumC0091c enumC0091c) {
        if (this.f5909d == c.h.CURRENCY) {
            Currency currency = Currency.getInstance(str);
            this.f5907b.setCurrency(currency);
            if (enumC0091c != c.EnumC0091c.CODE) {
                str = currency.getName(this.f5908c.h(), enumC0091c.b(), (boolean[]) null);
            }
            android.icu.text.NumberFormat numberFormat = this.f5907b;
            if (numberFormat instanceof DecimalFormat) {
                DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
                DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
                decimalFormatSymbols.setCurrencySymbol(str);
                decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
            }
        }
        return this;
    }

    @Override // com.facebook.hermes.intl.c
    /* JADX INFO: renamed from: r, reason: merged with bridge method [inline-methods] */
    public j l(c.f fVar, int i3, int i4) {
        if (fVar == c.f.FRACTION_DIGITS) {
            if (i3 >= 0) {
                this.f5907b.setMinimumFractionDigits(i3);
            }
            if (i4 >= 0) {
                this.f5907b.setMaximumFractionDigits(i4);
            }
            android.icu.text.NumberFormat numberFormat = this.f5907b;
            if (numberFormat instanceof DecimalFormat) {
                ((DecimalFormat) numberFormat).setSignificantDigitsUsed(false);
            }
        }
        return this;
    }

    @Override // com.facebook.hermes.intl.c
    /* JADX INFO: renamed from: s, reason: merged with bridge method [inline-methods] */
    public j k(boolean z3) {
        this.f5907b.setGroupingUsed(z3);
        return this;
    }

    @Override // com.facebook.hermes.intl.c
    /* JADX INFO: renamed from: t, reason: merged with bridge method [inline-methods] */
    public j i(int i3) {
        if (i3 != -1) {
            this.f5907b.setMinimumIntegerDigits(i3);
        }
        return this;
    }

    @Override // com.facebook.hermes.intl.c
    /* JADX INFO: renamed from: u, reason: merged with bridge method [inline-methods] */
    public j h(c.g gVar) {
        android.icu.text.NumberFormat numberFormat = this.f5907b;
        if (numberFormat instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
            if (Build.VERSION.SDK_INT >= 31) {
                int i3 = a.f5911a[gVar.ordinal()];
                if (i3 == 1) {
                    decimalFormat.setSignAlwaysShown(false);
                } else if (i3 == 2 || i3 == 3) {
                    decimalFormat.setSignAlwaysShown(true);
                }
            } else {
                int i4 = a.f5911a[gVar.ordinal()];
                if (i4 == 1) {
                    decimalFormat.setPositivePrefix("");
                    decimalFormat.setPositiveSuffix("");
                    decimalFormat.setNegativePrefix("");
                    decimalFormat.setNegativeSuffix("");
                } else if (i4 == 2 || i4 == 3) {
                    if (!decimalFormat.getNegativePrefix().isEmpty()) {
                        decimalFormat.setPositivePrefix(new String(new char[]{decimalFormatSymbols.getPlusSign()}));
                    }
                    if (!decimalFormat.getNegativeSuffix().isEmpty()) {
                        decimalFormat.setPositiveSuffix(new String(new char[]{decimalFormatSymbols.getPlusSign()}));
                    }
                }
            }
        }
        return this;
    }

    @Override // com.facebook.hermes.intl.c
    /* JADX INFO: renamed from: v, reason: merged with bridge method [inline-methods] */
    public j d(c.f fVar, int i3, int i4) throws B0.e {
        android.icu.text.NumberFormat numberFormat = this.f5907b;
        if ((numberFormat instanceof DecimalFormat) && fVar == c.f.SIGNIFICANT_DIGITS) {
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            if (i3 >= 0) {
                decimalFormat.setMinimumSignificantDigits(i3);
            }
            if (i4 >= 0) {
                if (i4 < decimalFormat.getMinimumSignificantDigits()) {
                    throw new B0.e("maximumSignificantDigits should be at least equal to minimumSignificantDigits");
                }
                decimalFormat.setMaximumSignificantDigits(i4);
            }
            decimalFormat.setSignificantDigitsUsed(true);
        }
        return this;
    }

    @Override // com.facebook.hermes.intl.c
    /* JADX INFO: renamed from: w, reason: merged with bridge method [inline-methods] */
    public j f(String str, c.i iVar) {
        if (this.f5909d == c.h.UNIT) {
            this.f5910e = p(str);
            this.f5906a = MeasureFormat.getInstance(this.f5908c.h(), iVar.b(), this.f5907b);
        }
        return this;
    }
}
