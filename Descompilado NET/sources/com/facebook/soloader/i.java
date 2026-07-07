package com.facebook.soloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/* JADX INFO: loaded from: classes.dex */
public class i implements h {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private File f8232b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private FileInputStream f8233c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private FileChannel f8234d;

    public i(File file) {
        this.f8232b = file;
        a();
    }

    @Override // com.facebook.soloader.h
    public int Z(ByteBuffer byteBuffer, long j3) {
        return this.f8234d.read(byteBuffer, j3);
    }

    public void a() {
        FileInputStream fileInputStream = new FileInputStream(this.f8232b);
        this.f8233c = fileInputStream;
        this.f8234d = fileInputStream.getChannel();
    }

    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.f8233c.close();
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return this.f8234d.isOpen();
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) {
        return this.f8234d.read(byteBuffer);
    }

    @Override // java.nio.channels.WritableByteChannel
    public int write(ByteBuffer byteBuffer) {
        return this.f8234d.write(byteBuffer);
    }
}
