package a0;

import java.io.Closeable;

/* JADX INFO: loaded from: classes.dex */
public interface h extends Closeable {

    public static class a extends RuntimeException {
        public a() {
            super("Invalid bytebuf. Already closed");
        }
    }

    boolean b();

    int c(int i3, byte[] bArr, int i4, int i5);

    byte g(int i3);

    int size();
}
