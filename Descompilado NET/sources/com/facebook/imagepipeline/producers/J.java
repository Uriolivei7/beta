package com.facebook.imagepipeline.producers;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: loaded from: classes.dex */
public final class J extends M {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f6019d = new a(null);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final String[] f6020e = {"_id", "_data"};

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ContentResolver f6021c;

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public J(Executor executor, a0.i iVar, ContentResolver contentResolver) {
        super(executor, iVar);
        D2.h.f(executor, "executor");
        D2.h.f(iVar, "pooledByteBufferFactory");
        D2.h.f(contentResolver, "contentResolver");
        this.f6021c = contentResolver;
    }

    private final O0.j g(Uri uri) throws IOException {
        try {
            ParcelFileDescriptor parcelFileDescriptorOpenFileDescriptor = this.f6021c.openFileDescriptor(uri, "r");
            if (parcelFileDescriptorOpenFileDescriptor == null) {
                throw new IllegalStateException("Required value was null.");
            }
            O0.j jVarE = e(new FileInputStream(parcelFileDescriptorOpenFileDescriptor.getFileDescriptor()), (int) parcelFileDescriptorOpenFileDescriptor.getStatSize());
            D2.h.e(jVarE, "getEncodedImage(...)");
            parcelFileDescriptorOpenFileDescriptor.close();
            return jVarE;
        } catch (FileNotFoundException unused) {
            return null;
        }
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected O0.j d(U0.b bVar) throws IOException {
        O0.j jVarG;
        InputStream inputStreamCreateInputStream;
        D2.h.f(bVar, "imageRequest");
        Uri uriV = bVar.v();
        D2.h.e(uriV, "getSourceUri(...)");
        if (!f0.f.k(uriV)) {
            if (f0.f.j(uriV) && (jVarG = g(uriV)) != null) {
                return jVarG;
            }
            InputStream inputStreamOpenInputStream = this.f6021c.openInputStream(uriV);
            if (inputStreamOpenInputStream != null) {
                return e(inputStreamOpenInputStream, -1);
            }
            throw new IllegalStateException("Required value was null.");
        }
        String string = uriV.toString();
        D2.h.e(string, "toString(...)");
        if (K2.o.m(string, "/photo", false, 2, null)) {
            inputStreamCreateInputStream = this.f6021c.openInputStream(uriV);
        } else {
            String string2 = uriV.toString();
            D2.h.e(string2, "toString(...)");
            if (K2.o.m(string2, "/display_photo", false, 2, null)) {
                try {
                    AssetFileDescriptor assetFileDescriptorOpenAssetFileDescriptor = this.f6021c.openAssetFileDescriptor(uriV, "r");
                    if (assetFileDescriptorOpenAssetFileDescriptor == null) {
                        throw new IllegalStateException("Required value was null.");
                    }
                    inputStreamCreateInputStream = assetFileDescriptorOpenAssetFileDescriptor.createInputStream();
                } catch (IOException unused) {
                    throw new IOException("Contact photo does not exist: " + uriV);
                }
            } else {
                InputStream inputStreamOpenContactPhotoInputStream = ContactsContract.Contacts.openContactPhotoInputStream(this.f6021c, uriV);
                if (inputStreamOpenContactPhotoInputStream == null) {
                    throw new IOException("Contact photo does not exist: " + uriV);
                }
                inputStreamCreateInputStream = inputStreamOpenContactPhotoInputStream;
            }
        }
        if (inputStreamCreateInputStream != null) {
            return e(inputStreamCreateInputStream, -1);
        }
        throw new IllegalStateException("Required value was null.");
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected String f() {
        return "LocalContentUriFetchProducer";
    }
}
