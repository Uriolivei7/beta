package com.facebook.react.modules.network;

import M2.C;
import M2.x;
import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import b3.AbstractC0320c;
import b3.F;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.GZIPOutputStream;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final n f7026a = new n();

    public static final class a extends C {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ x f7027b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ InputStream f7028c;

        a(x xVar, InputStream inputStream) {
            this.f7027b = xVar;
            this.f7028c = inputStream;
        }

        @Override // M2.C
        public long a() {
            try {
                return this.f7028c.available();
            } catch (IOException unused) {
                return 0L;
            }
        }

        @Override // M2.C
        public x b() {
            return this.f7027b;
        }

        @Override // M2.C
        public void h(b3.j jVar) {
            D2.h.f(jVar, "sink");
            F fC = null;
            try {
                fC = AbstractC0320c.a().c(this.f7028c);
                jVar.T(fC);
            } finally {
                if (fC != null) {
                    n.f7026a.b(fC);
                }
            }
        }
    }

    private n() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void b(F f3) {
        try {
            f3.close();
        } catch (RuntimeException e4) {
            throw e4;
        } catch (Exception unused) {
        }
    }

    public static final C c(x xVar, InputStream inputStream) {
        D2.h.f(inputStream, "inputStream");
        return new a(xVar, inputStream);
    }

    public static final C d(x xVar, String str) {
        D2.h.f(str, "body");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            byte[] bytes = str.getBytes(K2.d.f816b);
            D2.h.e(bytes, "getBytes(...)");
            gZIPOutputStream.write(bytes);
            gZIPOutputStream.close();
            C.a aVar = C.f908a;
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            D2.h.e(byteArray, "toByteArray(...)");
            return C.a.g(aVar, xVar, byteArray, 0, 0, 12, null);
        } catch (IOException unused) {
            return null;
        }
    }

    public static final j e(C c4, i iVar) {
        D2.h.f(c4, "requestBody");
        D2.h.f(iVar, "listener");
        return new j(c4, iVar);
    }

    private final InputStream f(Context context, Uri uri) throws IOException {
        File fileCreateTempFile = File.createTempFile("RequestBodyUtil", "temp", context.getApplicationContext().getCacheDir());
        fileCreateTempFile.deleteOnExit();
        URL url = new URL(uri.toString());
        FileOutputStream fileOutputStream = new FileOutputStream(fileCreateTempFile);
        try {
            InputStream inputStreamOpenStream = url.openStream();
            try {
                ReadableByteChannel readableByteChannelNewChannel = Channels.newChannel(inputStreamOpenStream);
                try {
                    fileOutputStream.getChannel().transferFrom(readableByteChannelNewChannel, 0L, Long.MAX_VALUE);
                    FileInputStream fileInputStream = new FileInputStream(fileCreateTempFile);
                    A2.a.a(readableByteChannelNewChannel, null);
                    A2.a.a(inputStreamOpenStream, null);
                    A2.a.a(fileOutputStream, null);
                    return fileInputStream;
                } finally {
                }
            } finally {
            }
        } finally {
        }
    }

    public static final C g(String str) {
        D2.h.f(str, "method");
        int iHashCode = str.hashCode();
        if (iHashCode != 79599) {
            if (iHashCode != 2461856) {
                if (iHashCode != 75900968 || !str.equals("PATCH")) {
                    return null;
                }
            } else if (!str.equals("POST")) {
                return null;
            }
        } else if (!str.equals("PUT")) {
            return null;
        }
        return C.f908a.a(null, b3.l.f5638e);
    }

    public static final InputStream h(Context context, String str) {
        List listG;
        D2.h.f(context, "context");
        D2.h.f(str, "fileContentUriStr");
        try {
            Uri uri = Uri.parse(str);
            String scheme = uri.getScheme();
            if (scheme != null && K2.o.z(scheme, "http", false, 2, null)) {
                n nVar = f7026a;
                D2.h.c(uri);
                return nVar.f(context, uri);
            }
            if (!K2.o.z(str, "data:", false, 2, null)) {
                return context.getContentResolver().openInputStream(uri);
            }
            List listD = new K2.k(",").d(str, 0);
            if (listD.isEmpty()) {
                listG = AbstractC0717n.g();
            } else {
                ListIterator listIterator = listD.listIterator(listD.size());
                while (listIterator.hasPrevious()) {
                    if (((String) listIterator.previous()).length() != 0) {
                        listG = AbstractC0717n.b0(listD, listIterator.nextIndex() + 1);
                        break;
                    }
                }
                listG = AbstractC0717n.g();
            }
            return new ByteArrayInputStream(Base64.decode(((String[]) listG.toArray(new String[0]))[1], 0));
        } catch (Exception e4) {
            Y.a.n("ReactNative", "Could not retrieve file for contentUri " + str, e4);
            return null;
        }
    }

    public static final boolean i(String str) {
        return K2.o.n("gzip", str, true);
    }
}
