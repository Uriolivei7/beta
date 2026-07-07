package K2;

import java.io.IOException;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class p {
    public static void a(Appendable appendable, Object obj, C2.l lVar) throws IOException {
        D2.h.f(appendable, "<this>");
        if (lVar != null) {
            appendable.append((CharSequence) lVar.d(obj));
            return;
        }
        if (obj == null ? true : obj instanceof CharSequence) {
            appendable.append((CharSequence) obj);
        } else if (obj instanceof Character) {
            appendable.append(((Character) obj).charValue());
        } else {
            appendable.append(String.valueOf(obj));
        }
    }
}
