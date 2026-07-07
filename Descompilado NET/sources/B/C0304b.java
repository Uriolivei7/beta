package b;

import android.content.Intent;
import java.util.ArrayList;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0696D;
import s2.AbstractC0711h;
import s2.AbstractC0717n;

/* JADX INFO: renamed from: b.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0304b extends AbstractC0303a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f5570a = new a(null);

    /* JADX INFO: renamed from: b.b$a */
    public static final class a {
        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private a() {
        }
    }

    @Override // b.AbstractC0303a
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public Map a(int i3, Intent intent) {
        if (i3 != -1) {
            return AbstractC0696D.f();
        }
        if (intent == null) {
            return AbstractC0696D.f();
        }
        String[] stringArrayExtra = intent.getStringArrayExtra("androidx.activity.result.contract.extra.PERMISSIONS");
        int[] intArrayExtra = intent.getIntArrayExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS");
        if (intArrayExtra == null || stringArrayExtra == null) {
            return AbstractC0696D.f();
        }
        ArrayList arrayList = new ArrayList(intArrayExtra.length);
        for (int i4 : intArrayExtra) {
            arrayList.add(Boolean.valueOf(i4 == 0));
        }
        return AbstractC0696D.m(AbstractC0717n.i0(AbstractC0711h.m(stringArrayExtra), arrayList));
    }
}
