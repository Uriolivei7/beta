package com.facebook.hermes.intl;

import android.icu.text.RuleBasedCollator;
import com.facebook.hermes.intl.a;

/* JADX INFO: loaded from: classes.dex */
public class h implements com.facebook.hermes.intl.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private RuleBasedCollator f5900a = null;

    static /* synthetic */ class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f5901a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f5902b;

        static {
            int[] iArr = new int[a.b.values().length];
            f5902b = iArr;
            try {
                iArr[a.b.UPPER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5902b[a.b.LOWER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5902b[a.b.FALSE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[a.c.values().length];
            f5901a = iArr2;
            try {
                iArr2[a.c.BASE.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5901a[a.c.ACCENT.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f5901a[a.c.CASE.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f5901a[a.c.VARIANT.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    h() {
    }

    @Override // com.facebook.hermes.intl.a
    public com.facebook.hermes.intl.a a(a.b bVar) {
        int i3 = a.f5902b[bVar.ordinal()];
        if (i3 == 1) {
            this.f5900a.setUpperCaseFirst(true);
        } else if (i3 != 2) {
            this.f5900a.setCaseFirstDefault();
        } else {
            this.f5900a.setLowerCaseFirst(true);
        }
        return this;
    }

    @Override // com.facebook.hermes.intl.a
    public com.facebook.hermes.intl.a b(B0.b bVar) {
        RuleBasedCollator ruleBasedCollator = (RuleBasedCollator) android.icu.text.Collator.getInstance(((B0.g) bVar).h());
        this.f5900a = ruleBasedCollator;
        ruleBasedCollator.setDecomposition(17);
        return this;
    }

    @Override // com.facebook.hermes.intl.a
    public com.facebook.hermes.intl.a c(boolean z3) {
        if (z3) {
            this.f5900a.setNumericCollation(B0.d.e(Boolean.TRUE));
        }
        return this;
    }

    @Override // com.facebook.hermes.intl.a
    public int d(String str, String str2) {
        return this.f5900a.compare(str, str2);
    }

    @Override // com.facebook.hermes.intl.a
    public com.facebook.hermes.intl.a e(a.c cVar) {
        int i3 = a.f5901a[cVar.ordinal()];
        if (i3 == 1) {
            this.f5900a.setStrength(0);
        } else if (i3 == 2) {
            this.f5900a.setStrength(1);
        } else if (i3 == 3) {
            this.f5900a.setStrength(0);
            this.f5900a.setCaseLevel(true);
        } else if (i3 == 4) {
            this.f5900a.setStrength(2);
        }
        return this;
    }

    @Override // com.facebook.hermes.intl.a
    public a.c f() {
        RuleBasedCollator ruleBasedCollator = this.f5900a;
        if (ruleBasedCollator == null) {
            return a.c.LOCALE;
        }
        int strength = ruleBasedCollator.getStrength();
        return strength == 0 ? this.f5900a.isCaseLevel() ? a.c.CASE : a.c.BASE : strength == 1 ? a.c.ACCENT : a.c.VARIANT;
    }

    @Override // com.facebook.hermes.intl.a
    public com.facebook.hermes.intl.a g(boolean z3) {
        if (z3) {
            this.f5900a.setAlternateHandlingShifted(true);
        }
        return this;
    }
}
