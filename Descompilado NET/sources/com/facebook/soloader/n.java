package com.facebook.soloader;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

/* JADX INFO: loaded from: classes.dex */
public final class n implements Closeable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final FileOutputStream f8252b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final FileLock f8253c;

    private n(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        this.f8252b = fileOutputStream;
        try {
            FileLock fileLockLock = fileOutputStream.getChannel().lock();
            if (fileLockLock == null) {
                fileOutputStream.close();
            }
            this.f8253c = fileLockLock;
        } catch (Throwable th) {
            this.f8252b.close();
            throw th;
        }
    }

    public static n a(File file) {
        return new n(file);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            FileLock fileLock = this.f8253c;
            if (fileLock != null) {
                fileLock.release();
            }
        } finally {
            this.f8252b.close();
        }
    }
}
