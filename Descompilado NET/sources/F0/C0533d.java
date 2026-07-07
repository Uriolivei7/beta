package f0;

import X.k;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: renamed from: f0.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0533d {
    public static long a(InputStream inputStream, long j3) throws IOException {
        k.g(inputStream);
        k.b(Boolean.valueOf(j3 >= 0));
        long j4 = j3;
        while (j4 > 0) {
            long jSkip = inputStream.skip(j4);
            if (jSkip <= 0) {
                if (inputStream.read() == -1) {
                    return j3 - j4;
                }
                jSkip = 1;
            }
            j4 -= jSkip;
        }
        return j3;
    }
}
