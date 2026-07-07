package com.facebook.react.modules.camera;

import D2.h;
import android.net.Uri;
import android.util.Base64OutputStream;
import com.facebook.fbreact.specs.NativeImageStoreAndroidSpec;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.camera.ImageStoreManager;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import kotlin.jvm.internal.DefaultConstructorMarker;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = "ImageStoreManager")
public final class ImageStoreManager extends NativeImageStoreAndroidSpec {
    private static final int BUFFER_SIZE = 8192;
    public static final a Companion = new a(null);
    public static final String NAME = "ImageStoreManager";

    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void b(Closeable closeable) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }

        public final String c(InputStream inputStream) {
            h.f(inputStream, "inputStream");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, 2);
            byte[] bArr = new byte[ImageStoreManager.BUFFER_SIZE];
            while (true) {
                try {
                    int i3 = inputStream.read(bArr);
                    if (i3 <= -1) {
                        b(base64OutputStream);
                        String string = byteArrayOutputStream.toString();
                        h.e(string, "toString(...)");
                        return string;
                    }
                    base64OutputStream.write(bArr, 0, i3);
                } catch (Throwable th) {
                    b(base64OutputStream);
                    throw th;
                }
            }
        }

        private a() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ImageStoreManager(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        h.f(reactApplicationContext, "reactContext");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void getBase64ForTag$lambda$0(ImageStoreManager imageStoreManager, String str, Callback callback, Callback callback2) {
        a aVar;
        try {
            InputStream inputStreamOpenInputStream = imageStoreManager.getReactApplicationContext().getContentResolver().openInputStream(Uri.parse(str));
            h.d(inputStreamOpenInputStream, "null cannot be cast to non-null type java.io.InputStream");
            try {
                try {
                    aVar = Companion;
                    callback.invoke(aVar.c(inputStreamOpenInputStream));
                } catch (IOException e4) {
                    callback2.invoke(e4.getMessage());
                    aVar = Companion;
                }
                aVar.b(inputStreamOpenInputStream);
            } catch (Throwable th) {
                Companion.b(inputStreamOpenInputStream);
                throw th;
            }
        } catch (FileNotFoundException e5) {
            callback2.invoke(e5.getMessage());
        }
    }

    @Override // com.facebook.fbreact.specs.NativeImageStoreAndroidSpec
    public void getBase64ForTag(final String str, final Callback callback, final Callback callback2) {
        h.f(str, "uri");
        h.f(callback, "success");
        h.f(callback2, "error");
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: A1.a
            @Override // java.lang.Runnable
            public final void run() {
                ImageStoreManager.getBase64ForTag$lambda$0(this.f32b, str, callback, callback2);
            }
        });
    }
}
