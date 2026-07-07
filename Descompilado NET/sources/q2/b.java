package Q2;

import D2.h;
import D2.u;
import java.util.Arrays;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
public abstract class b {
    public static final String b(long j3) {
        String str;
        if (j3 <= -999500000) {
            str = ((j3 - ((long) 500000000)) / ((long) 1000000000)) + " s ";
        } else if (j3 <= -999500) {
            str = ((j3 - ((long) 500000)) / ((long) 1000000)) + " ms";
        } else if (j3 <= 0) {
            str = ((j3 - ((long) 500)) / ((long) 1000)) + " µs";
        } else if (j3 < 999500) {
            str = ((j3 + ((long) 500)) / ((long) 1000)) + " µs";
        } else if (j3 < 999500000) {
            str = ((j3 + ((long) 500000)) / ((long) 1000000)) + " ms";
        } else {
            str = ((j3 + ((long) 500000000)) / ((long) 1000000000)) + " s ";
        }
        u uVar = u.f192a;
        String str2 = String.format("%6s", Arrays.copyOf(new Object[]{str}, 1));
        h.e(str2, "java.lang.String.format(format, *args)");
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void c(a aVar, d dVar, String str) {
        Logger loggerA = e.f1872j.a();
        StringBuilder sb = new StringBuilder();
        sb.append(dVar.f());
        sb.append(' ');
        u uVar = u.f192a;
        String str2 = String.format("%-22s", Arrays.copyOf(new Object[]{str}, 1));
        h.e(str2, "java.lang.String.format(format, *args)");
        sb.append(str2);
        sb.append(": ");
        sb.append(aVar.b());
        loggerA.fine(sb.toString());
    }
}
