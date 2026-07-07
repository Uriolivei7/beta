package i2;

import com.facebook.react.bridge.ReactApplicationContext;
import com.ninty.system.setting.SystemSetting;
import d1.O;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: renamed from: i2.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0563c implements O {
    @Override // d1.O
    public List e(ReactApplicationContext reactApplicationContext) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SystemSetting(reactApplicationContext));
        return arrayList;
    }

    @Override // d1.O
    public List f(ReactApplicationContext reactApplicationContext) {
        return Collections.emptyList();
    }
}
