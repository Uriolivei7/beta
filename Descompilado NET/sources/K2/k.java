package K2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class k implements Serializable {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final a f841c = new a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Pattern f842b;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int b(int i3) {
            return (i3 & 2) != 0 ? i3 | 64 : i3;
        }

        private a() {
        }
    }

    public k(Pattern pattern) {
        D2.h.f(pattern, "nativePattern");
        this.f842b = pattern;
    }

    public final i a(CharSequence charSequence) {
        D2.h.f(charSequence, "input");
        Matcher matcher = this.f842b.matcher(charSequence);
        D2.h.e(matcher, "matcher(...)");
        return l.d(matcher, charSequence);
    }

    public final boolean b(CharSequence charSequence) {
        D2.h.f(charSequence, "input");
        return this.f842b.matcher(charSequence).matches();
    }

    public final String c(CharSequence charSequence, String str) {
        D2.h.f(charSequence, "input");
        D2.h.f(str, "replacement");
        String strReplaceAll = this.f842b.matcher(charSequence).replaceAll(str);
        D2.h.e(strReplaceAll, "replaceAll(...)");
        return strReplaceAll;
    }

    public final List d(CharSequence charSequence, int i3) {
        D2.h.f(charSequence, "input");
        y.h0(i3);
        Matcher matcher = this.f842b.matcher(charSequence);
        if (i3 == 1 || !matcher.find()) {
            return AbstractC0717n.b(charSequence.toString());
        }
        ArrayList arrayList = new ArrayList(i3 > 0 ? H2.d.e(i3, 10) : 10);
        int i4 = i3 - 1;
        int iEnd = 0;
        do {
            arrayList.add(charSequence.subSequence(iEnd, matcher.start()).toString());
            iEnd = matcher.end();
            if (i4 >= 0 && arrayList.size() == i4) {
                break;
            }
        } while (matcher.find());
        arrayList.add(charSequence.subSequence(iEnd, charSequence.length()).toString());
        return arrayList;
    }

    public String toString() {
        String string = this.f842b.toString();
        D2.h.e(string, "toString(...)");
        return string;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public k(String str) {
        D2.h.f(str, "pattern");
        Pattern patternCompile = Pattern.compile(str);
        D2.h.e(patternCompile, "compile(...)");
        this(patternCompile);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public k(String str, m mVar) {
        D2.h.f(str, "pattern");
        D2.h.f(mVar, "option");
        Pattern patternCompile = Pattern.compile(str, f841c.b(mVar.getValue()));
        D2.h.e(patternCompile, "compile(...)");
        this(patternCompile);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public k(String str, Set<? extends m> set) {
        D2.h.f(str, "pattern");
        D2.h.f(set, "options");
        Pattern patternCompile = Pattern.compile(str, f841c.b(l.f(set)));
        D2.h.e(patternCompile, "compile(...)");
        this(patternCompile);
    }
}
