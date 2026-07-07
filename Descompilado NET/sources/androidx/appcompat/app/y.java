package androidx.appcompat.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import java.util.Calendar;

/* JADX INFO: loaded from: classes.dex */
class y {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static y f3312d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f3313a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final LocationManager f3314b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final a f3315c = new a();

    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        boolean f3316a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        long f3317b;

        a() {
        }
    }

    y(Context context, LocationManager locationManager) {
        this.f3313a = context;
        this.f3314b = locationManager;
    }

    static y a(Context context) {
        if (f3312d == null) {
            Context applicationContext = context.getApplicationContext();
            f3312d = new y(applicationContext, (LocationManager) applicationContext.getSystemService("location"));
        }
        return f3312d;
    }

    private Location b() {
        Location locationC = androidx.core.content.e.b(this.f3313a, "android.permission.ACCESS_COARSE_LOCATION") == 0 ? c("network") : null;
        Location locationC2 = androidx.core.content.e.b(this.f3313a, "android.permission.ACCESS_FINE_LOCATION") == 0 ? c("gps") : null;
        return (locationC2 == null || locationC == null) ? locationC2 != null ? locationC2 : locationC : locationC2.getTime() > locationC.getTime() ? locationC2 : locationC;
    }

    private Location c(String str) {
        try {
            if (this.f3314b.isProviderEnabled(str)) {
                return this.f3314b.getLastKnownLocation(str);
            }
            return null;
        } catch (Exception e4) {
            Log.d("TwilightManager", "Failed to get last known location", e4);
            return null;
        }
    }

    private boolean e() {
        return this.f3315c.f3317b > System.currentTimeMillis();
    }

    private void f(Location location) {
        long j3;
        a aVar = this.f3315c;
        long jCurrentTimeMillis = System.currentTimeMillis();
        x xVarB = x.b();
        xVarB.a(jCurrentTimeMillis - 86400000, location.getLatitude(), location.getLongitude());
        xVarB.a(jCurrentTimeMillis, location.getLatitude(), location.getLongitude());
        boolean z3 = xVarB.f3311c == 1;
        long j4 = xVarB.f3310b;
        long j5 = xVarB.f3309a;
        xVarB.a(jCurrentTimeMillis + 86400000, location.getLatitude(), location.getLongitude());
        long j6 = xVarB.f3310b;
        if (j4 == -1 || j5 == -1) {
            j3 = jCurrentTimeMillis + 43200000;
        } else {
            if (jCurrentTimeMillis <= j5) {
                j6 = jCurrentTimeMillis > j4 ? j5 : j4;
            }
            j3 = j6 + 60000;
        }
        aVar.f3316a = z3;
        aVar.f3317b = j3;
    }

    boolean d() {
        a aVar = this.f3315c;
        if (e()) {
            return aVar.f3316a;
        }
        Location locationB = b();
        if (locationB != null) {
            f(locationB);
            return aVar.f3316a;
        }
        Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
        int i3 = Calendar.getInstance().get(11);
        return i3 < 6 || i3 >= 22;
    }
}
