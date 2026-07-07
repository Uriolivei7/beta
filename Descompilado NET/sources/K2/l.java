package K2;

import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

/* JADX INFO: loaded from: classes.dex */
public abstract class l {
    /* JADX INFO: Access modifiers changed from: private */
    public static final i d(Matcher matcher, CharSequence charSequence) {
        if (matcher.matches()) {
            return new j(matcher, charSequence);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final H2.c e(MatchResult matchResult, int i3) {
        return H2.d.i(matchResult.start(i3), matchResult.end(i3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int f(Iterable iterable) {
        Iterator it = iterable.iterator();
        int value = 0;
        while (it.hasNext()) {
            value |= ((f) it.next()).getValue();
        }
        return value;
    }
}
