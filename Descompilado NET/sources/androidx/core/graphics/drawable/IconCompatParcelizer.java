package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class IconCompatParcelizer {
    public static IconCompat read(androidx.versionedparcelable.a aVar) {
        IconCompat iconCompat = new IconCompat();
        iconCompat.f4481a = aVar.p(iconCompat.f4481a, 1);
        iconCompat.f4483c = aVar.j(iconCompat.f4483c, 2);
        iconCompat.f4484d = aVar.r(iconCompat.f4484d, 3);
        iconCompat.f4485e = aVar.p(iconCompat.f4485e, 4);
        iconCompat.f4486f = aVar.p(iconCompat.f4486f, 5);
        iconCompat.f4487g = (ColorStateList) aVar.r(iconCompat.f4487g, 6);
        iconCompat.f4489i = aVar.t(iconCompat.f4489i, 7);
        iconCompat.f4490j = aVar.t(iconCompat.f4490j, 8);
        iconCompat.f();
        return iconCompat;
    }

    public static void write(IconCompat iconCompat, androidx.versionedparcelable.a aVar) {
        aVar.x(true, true);
        iconCompat.g(aVar.f());
        int i3 = iconCompat.f4481a;
        if (-1 != i3) {
            aVar.F(i3, 1);
        }
        byte[] bArr = iconCompat.f4483c;
        if (bArr != null) {
            aVar.B(bArr, 2);
        }
        Parcelable parcelable = iconCompat.f4484d;
        if (parcelable != null) {
            aVar.H(parcelable, 3);
        }
        int i4 = iconCompat.f4485e;
        if (i4 != 0) {
            aVar.F(i4, 4);
        }
        int i5 = iconCompat.f4486f;
        if (i5 != 0) {
            aVar.F(i5, 5);
        }
        ColorStateList colorStateList = iconCompat.f4487g;
        if (colorStateList != null) {
            aVar.H(colorStateList, 6);
        }
        String str = iconCompat.f4489i;
        if (str != null) {
            aVar.J(str, 7);
        }
        String str2 = iconCompat.f4490j;
        if (str2 != null) {
            aVar.J(str2, 8);
        }
    }
}
