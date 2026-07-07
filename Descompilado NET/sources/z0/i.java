package Z0;

import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final i f2810a = new i();

    private i() {
    }

    public static final int a(InputStream inputStream, int i3, boolean z3) throws IOException {
        int i4;
        D2.h.f(inputStream, "stream");
        int i5 = 0;
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = inputStream.read();
            if (i7 == -1) {
                throw new IOException("no more bytes");
            }
            if (z3) {
                i4 = (i7 & 255) << (i6 * 8);
            } else {
                i5 <<= 8;
                i4 = i7 & 255;
            }
            i5 |= i4;
        }
        return i5;
    }
}
