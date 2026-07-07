package Z0;

import androidx.exifinterface.media.ExifInterface;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final f f2806a = new f();

    private f() {
    }

    public static final int a(InputStream inputStream) {
        if (inputStream == null) {
            Y.a.b("HeifExifUtil", "Trying to read Heif Exif from null inputStream -> ignoring");
            return 0;
        }
        try {
            return new ExifInterface(inputStream).getAttributeInt("Orientation", 1);
        } catch (IOException e4) {
            Y.a.g("HeifExifUtil", "Failed reading Heif Exif orientation -> ignoring", e4);
            return 0;
        }
    }
}
