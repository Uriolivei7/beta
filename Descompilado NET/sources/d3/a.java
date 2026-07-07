package d3;

import com.facebook.react.bridge.ReactApplicationContext;
import d1.O;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.capslock.RNDeviceBrightness.RNDeviceBrightnessModule;

/* JADX INFO: loaded from: classes.dex */
public class a implements O {
    @Override // d1.O
    public List e(ReactApplicationContext reactApplicationContext) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new RNDeviceBrightnessModule(reactApplicationContext));
        return arrayList;
    }

    @Override // d1.O
    public List f(ReactApplicationContext reactApplicationContext) {
        return Collections.emptyList();
    }
}
