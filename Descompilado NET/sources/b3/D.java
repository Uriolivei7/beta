package b3;

import java.io.Closeable;
import java.io.Flushable;

/* JADX INFO: loaded from: classes.dex */
public interface D extends Closeable, Flushable {
    void Q(i iVar, long j3);

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();

    G f();

    void flush();
}
