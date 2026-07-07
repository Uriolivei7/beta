package i2;

/* JADX INFO: renamed from: i2.a, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public enum EnumC0561a {
    UNKNOW("", 0),
    WIFI("android.settings.WIFI_SETTINGS", 1),
    LOCATION("android.settings.LOCATION_SOURCE_SETTINGS", 2),
    BLUETOOTH("android.settings.BLUETOOTH_SETTINGS", 3),
    WRITESETTINGS("android.settings.action.MANAGE_WRITE_SETTINGS", 4),
    AIRPLANE("android.settings.AIRPLANE_MODE_SETTINGS", 5);


    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f9532b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f9533c;

    EnumC0561a(String str, int i3) {
        this.f9532b = str;
        this.f9533c = i3;
    }

    public static EnumC0561a b(int i3) {
        for (EnumC0561a enumC0561a : values()) {
            if (enumC0561a.f9533c == i3) {
                return enumC0561a;
            }
        }
        return UNKNOW;
    }
}
