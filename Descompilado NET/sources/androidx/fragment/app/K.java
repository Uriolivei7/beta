package androidx.fragment.app;

import android.util.Log;
import java.io.Writer;

/* JADX INFO: loaded from: classes.dex */
final class K extends Writer {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f5025b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private StringBuilder f5026c = new StringBuilder(128);

    K(String str) {
        this.f5025b = str;
    }

    private void a() {
        if (this.f5026c.length() > 0) {
            Log.d(this.f5025b, this.f5026c.toString());
            StringBuilder sb = this.f5026c;
            sb.delete(0, sb.length());
        }
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        a();
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
        a();
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i3, int i4) {
        for (int i5 = 0; i5 < i4; i5++) {
            char c4 = cArr[i3 + i5];
            if (c4 == '\n') {
                a();
            } else {
                this.f5026c.append(c4);
            }
        }
    }
}
