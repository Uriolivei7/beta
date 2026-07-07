package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class K extends M implements v0 {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final Class f6022d = K.class;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final String[] f6023e = {"_id", "_data"};

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final String[] f6024f = {"_data"};

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static final Rect f6025g = new Rect(0, 0, 512, 384);

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static final Rect f6026h = new Rect(0, 0, 96, 96);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ContentResolver f6027c;

    public K(Executor executor, a0.i iVar, ContentResolver contentResolver) {
        super(executor, iVar);
        this.f6027c = contentResolver;
    }

    private O0.j g(Uri uri, I0.g gVar) {
        Cursor cursorQuery;
        O0.j jVarJ;
        if (gVar == null || (cursorQuery = this.f6027c.query(uri, f6023e, null, null, null)) == null) {
            return null;
        }
        try {
            if (!cursorQuery.moveToFirst() || (jVarJ = j(gVar, cursorQuery.getLong(cursorQuery.getColumnIndex("_id")))) == null) {
                return null;
            }
            int columnIndex = cursorQuery.getColumnIndex("_data");
            if (columnIndex >= 0) {
                jVarJ.F0(i(cursorQuery.getString(columnIndex)));
            }
            return jVarJ;
        } finally {
            cursorQuery.close();
        }
    }

    private static int h(String str) {
        if (str == null) {
            return -1;
        }
        return (int) new File(str).length();
    }

    private static int i(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return Z0.h.a(new ExifInterface(str).getAttributeInt("Orientation", 1));
        } catch (IOException e4) {
            Y.a.l(f6022d, e4, "Unable to retrieve thumbnail rotation for %s", str);
            return 0;
        }
    }

    private O0.j j(I0.g gVar, long j3) {
        Cursor cursorQueryMiniThumbnail;
        int columnIndex;
        int iK = k(gVar);
        if (iK == 0 || (cursorQueryMiniThumbnail = MediaStore.Images.Thumbnails.queryMiniThumbnail(this.f6027c, j3, iK, f6024f)) == null) {
            return null;
        }
        try {
            if (cursorQueryMiniThumbnail.moveToFirst() && (columnIndex = cursorQueryMiniThumbnail.getColumnIndex("_data")) >= 0) {
                String str = (String) X.k.g(cursorQueryMiniThumbnail.getString(columnIndex));
                if (new File(str).exists()) {
                    return e(new FileInputStream(str), h(str));
                }
            }
            return null;
        } finally {
            cursorQueryMiniThumbnail.close();
        }
    }

    private static int k(I0.g gVar) {
        Rect rect = f6026h;
        if (w0.b(rect.width(), rect.height(), gVar)) {
            return 3;
        }
        Rect rect2 = f6025g;
        return w0.b(rect2.width(), rect2.height(), gVar) ? 1 : 0;
    }

    @Override // com.facebook.imagepipeline.producers.v0
    public boolean a(I0.g gVar) {
        Rect rect = f6025g;
        return w0.b(rect.width(), rect.height(), gVar);
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected O0.j d(U0.b bVar) {
        Uri uriV = bVar.v();
        if (f0.f.j(uriV)) {
            return g(uriV, bVar.r());
        }
        return null;
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected String f() {
        return "LocalContentUriThumbnailFetchProducer";
    }
}
