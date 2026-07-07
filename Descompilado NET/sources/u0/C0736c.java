package u0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;
import t0.r;

/* JADX INFO: renamed from: u0.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0736c {
    public static Drawable a(Context context, TypedArray typedArray, int i3) {
        int resourceId = typedArray.getResourceId(i3, 0);
        if (resourceId == 0) {
            return null;
        }
        return context.getDrawable(resourceId);
    }

    private static e b(C0735b c0735b) {
        if (c0735b.s() == null) {
            c0735b.J(new e());
        }
        return c0735b.s();
    }

    public static r c(TypedArray typedArray, int i3) {
        switch (typedArray.getInt(i3, -2)) {
            case -1:
                return null;
            case WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY /* 0 */:
                return r.f10750a;
            case 1:
                return r.f10753d;
            case 2:
                return r.f10754e;
            case 3:
                return r.f10755f;
            case 4:
                return r.f10756g;
            case 5:
                return r.f10757h;
            case 6:
                return r.f10758i;
            case 7:
                return r.f10759j;
            case 8:
                return r.f10760k;
            default:
                throw new RuntimeException("XML attribute not specified!");
        }
    }

    public static C0735b d(Context context, AttributeSet attributeSet) throws Throwable {
        if (V0.b.d()) {
            V0.b.a("GenericDraweeHierarchyBuilder#inflateBuilder");
        }
        C0735b c0735bE = e(new C0735b(context.getResources()), context, attributeSet);
        if (V0.b.d()) {
            V0.b.b();
        }
        return c0735bE;
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x01c6 A[PHI: r1 r2 r3
      0x01c6: PHI (r1v18 boolean) = (r1v14 boolean), (r1v20 boolean) binds: [B:131:0x01e4, B:115:0x01c4] A[DONT_GENERATE, DONT_INLINE]
      0x01c6: PHI (r2v13 boolean) = (r2v10 boolean), (r2v15 boolean) binds: [B:131:0x01e4, B:115:0x01c4] A[DONT_GENERATE, DONT_INLINE]
      0x01c6: PHI (r3v9 boolean) = (r3v6 boolean), (r3v11 boolean) binds: [B:131:0x01e4, B:115:0x01c4] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static u0.C0735b e(u0.C0735b r17, android.content.Context r18, android.util.AttributeSet r19) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 569
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: u0.C0736c.e(u0.b, android.content.Context, android.util.AttributeSet):u0.b");
    }
}
