package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class n implements Iterable {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ArrayList f4407b = new ArrayList();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Context f4408c;

    public interface a {
        Intent p();
    }

    private n(Context context) {
        this.f4408c = context;
    }

    public static n e(Context context) {
        return new n(context);
    }

    public n a(Intent intent) {
        this.f4407b.add(intent);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public n b(Activity activity) {
        Intent intentP = activity instanceof a ? ((a) activity).p() : null;
        if (intentP == null) {
            intentP = h.a(activity);
        }
        if (intentP != null) {
            ComponentName component = intentP.getComponent();
            if (component == null) {
                component = intentP.resolveActivity(this.f4408c.getPackageManager());
            }
            c(component);
            a(intentP);
        }
        return this;
    }

    public n c(ComponentName componentName) {
        int size = this.f4407b.size();
        try {
            Intent intentB = h.b(this.f4408c, componentName);
            while (intentB != null) {
                this.f4407b.add(size, intentB);
                intentB = h.b(this.f4408c, intentB.getComponent());
            }
            return this;
        } catch (PackageManager.NameNotFoundException e4) {
            Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
            throw new IllegalArgumentException(e4);
        }
    }

    public void f() {
        h(null);
    }

    public void h(Bundle bundle) {
        if (this.f4407b.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
        }
        Intent[] intentArr = (Intent[]) this.f4407b.toArray(new Intent[0]);
        intentArr[0] = new Intent(intentArr[0]).addFlags(268484608);
        if (androidx.core.content.a.g(this.f4408c, intentArr, bundle)) {
            return;
        }
        Intent intent = new Intent(intentArr[intentArr.length - 1]);
        intent.addFlags(268435456);
        this.f4408c.startActivity(intent);
    }

    @Override // java.lang.Iterable
    public Iterator iterator() {
        return this.f4407b.iterator();
    }
}
