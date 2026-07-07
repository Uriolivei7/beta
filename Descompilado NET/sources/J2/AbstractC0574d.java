package j2;

import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import k2.EnumC0587a;
import k2.EnumC0588b;

/* JADX INFO: renamed from: j2.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractC0574d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ConnectivityManager f9550a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final WifiManager f9551b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final TelephonyManager f9552c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final ReactApplicationContext f9553d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public boolean f9554e = false;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private EnumC0588b f9555f = EnumC0588b.UNKNOWN;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private EnumC0587a f9556g = null;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private boolean f9557h = false;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private Boolean f9558i;

    AbstractC0574d(ReactApplicationContext reactApplicationContext) {
        this.f9553d = reactApplicationContext;
        this.f9550a = (ConnectivityManager) reactApplicationContext.getSystemService("connectivity");
        this.f9551b = (WifiManager) reactApplicationContext.getApplicationContext().getSystemService("wifi");
        this.f9552c = (TelephonyManager) reactApplicationContext.getSystemService("phone");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private WritableMap b(String str) {
        WritableMap writableMapCreateMap;
        Enumeration<NetworkInterface> networkInterfaces;
        WifiManager wifiManager;
        WifiInfo connectionInfo;
        writableMapCreateMap = Arguments.createMap();
        str.hashCode();
        switch (str) {
            case "ethernet":
                try {
                    networkInterfaces = NetworkInterface.getNetworkInterfaces();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
                while (networkInterfaces.hasMoreElements()) {
                    Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddressNextElement = inetAddresses.nextElement();
                        if (!inetAddressNextElement.isLoopbackAddress() && (inetAddressNextElement instanceof Inet4Address)) {
                            writableMapCreateMap.putString("ipAddress", inetAddressNextElement.getHostAddress());
                            writableMapCreateMap.putString("subnet", f(inetAddressNextElement));
                            return writableMapCreateMap;
                        }
                        return writableMapCreateMap;
                    }
                }
                return writableMapCreateMap;
            case "cellular":
                EnumC0587a enumC0587a = this.f9556g;
                if (enumC0587a != null) {
                    writableMapCreateMap.putString("cellularGeneration", enumC0587a.f9598b);
                }
                String networkOperatorName = this.f9552c.getNetworkOperatorName();
                if (networkOperatorName != null) {
                    writableMapCreateMap.putString("carrier", networkOperatorName);
                }
                return writableMapCreateMap;
            case "wifi":
                if (C0576f.b(e()) && (wifiManager = this.f9551b) != null && (connectionInfo = wifiManager.getConnectionInfo()) != null) {
                    try {
                        String ssid = connectionInfo.getSSID();
                        if (ssid != null && !ssid.contains("<unknown ssid>")) {
                            writableMapCreateMap.putString("ssid", ssid.replace("\"", ""));
                        }
                        break;
                    } catch (Exception unused) {
                    }
                    try {
                        String bssid = connectionInfo.getBSSID();
                        if (bssid != null) {
                            writableMapCreateMap.putString("bssid", bssid);
                        }
                        break;
                    } catch (Exception unused2) {
                    }
                    try {
                        writableMapCreateMap.putInt("strength", WifiManager.calculateSignalLevel(connectionInfo.getRssi(), 100));
                        break;
                    } catch (Exception unused3) {
                    }
                    try {
                        writableMapCreateMap.putInt("frequency", connectionInfo.getFrequency());
                        break;
                    } catch (Exception unused4) {
                    }
                    try {
                        byte[] byteArray = BigInteger.valueOf(connectionInfo.getIpAddress()).toByteArray();
                        C0576f.c(byteArray);
                        writableMapCreateMap.putString("ipAddress", InetAddress.getByAddress(byteArray).getHostAddress());
                        break;
                    } catch (Exception unused5) {
                    }
                    try {
                        byte[] byteArray2 = BigInteger.valueOf(connectionInfo.getIpAddress()).toByteArray();
                        C0576f.c(byteArray2);
                        writableMapCreateMap.putString("subnet", f(InetAddress.getByAddress(byteArray2)));
                        break;
                    } catch (Exception unused6) {
                    }
                    try {
                        writableMapCreateMap.putInt("linkSpeed", connectionInfo.getLinkSpeed());
                        break;
                    } catch (Exception unused7) {
                    }
                    try {
                        if (Build.VERSION.SDK_INT >= 29) {
                            writableMapCreateMap.putInt("rxLinkSpeed", connectionInfo.getRxLinkSpeedMbps());
                        }
                        break;
                    } catch (Exception unused8) {
                    }
                    try {
                        if (Build.VERSION.SDK_INT >= 29) {
                            writableMapCreateMap.putInt("txLinkSpeed", connectionInfo.getTxLinkSpeedMbps());
                        }
                        break;
                    } catch (Exception unused9) {
                    }
                }
                return writableMapCreateMap;
            default:
                return writableMapCreateMap;
        }
    }

    private static String f(InetAddress inetAddress) {
        short networkPrefixLength;
        Iterator<InterfaceAddress> it = NetworkInterface.getByInetAddress(inetAddress).getInterfaceAddresses().iterator();
        while (true) {
            if (!it.hasNext()) {
                networkPrefixLength = 0;
                break;
            }
            InterfaceAddress next = it.next();
            if (next.getAddress().getAddress().length == 4) {
                networkPrefixLength = next.getNetworkPrefixLength();
                break;
            }
        }
        int i3 = (-1) << (32 - networkPrefixLength);
        return String.format(Locale.US, "%d.%d.%d.%d", Integer.valueOf((i3 >> 24) & 255), Integer.valueOf((i3 >> 16) & 255), Integer.valueOf((i3 >> 8) & 255), Integer.valueOf(i3 & 255));
    }

    protected WritableMap a(String str) {
        WritableMap writableMapCreateMap = Arguments.createMap();
        boolean z3 = false;
        if (C0576f.b(e())) {
            WifiManager wifiManager = this.f9551b;
            writableMapCreateMap.putBoolean("isWifiEnabled", wifiManager != null ? wifiManager.isWifiEnabled() : false);
        }
        writableMapCreateMap.putString("type", str != null ? str : this.f9555f.f9608b);
        boolean z4 = (this.f9555f.equals(EnumC0588b.NONE) || this.f9555f.equals(EnumC0588b.UNKNOWN)) ? false : true;
        writableMapCreateMap.putBoolean("isConnected", z4);
        if (this.f9557h && (str == null || str.equals(this.f9555f.f9608b))) {
            z3 = true;
        }
        writableMapCreateMap.putBoolean("isInternetReachable", z3);
        if (str == null) {
            str = this.f9555f.f9608b;
        }
        WritableMap writableMapB = b(str);
        if (z4) {
            writableMapB.putBoolean("isConnectionExpensive", c() != null ? c().isActiveNetworkMetered() : true);
        }
        writableMapCreateMap.putMap("details", writableMapB);
        return writableMapCreateMap;
    }

    ConnectivityManager c() {
        return this.f9550a;
    }

    public void d(String str, Promise promise) {
        promise.resolve(a(str));
    }

    ReactApplicationContext e() {
        return this.f9553d;
    }

    public abstract void g();

    protected void h() {
        ((DeviceEventManagerModule.RCTDeviceEventEmitter) e().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)).emit("netInfo.networkStatusDidChange", a(null));
    }

    public void i(boolean z3) {
        this.f9558i = Boolean.valueOf(z3);
        k(this.f9555f, this.f9556g, this.f9557h);
    }

    public abstract void j();

    void k(EnumC0588b enumC0588b, EnumC0587a enumC0587a, boolean z3) {
        Boolean bool = this.f9558i;
        if (bool != null) {
            z3 = bool.booleanValue();
        }
        boolean z4 = enumC0588b != this.f9555f;
        boolean z5 = enumC0587a != this.f9556g;
        boolean z6 = z3 != this.f9557h;
        if (z4 || z5 || z6) {
            this.f9555f = enumC0588b;
            this.f9556g = enumC0587a;
            this.f9557h = z3;
            if (this.f9554e) {
                h();
            }
        }
    }
}
