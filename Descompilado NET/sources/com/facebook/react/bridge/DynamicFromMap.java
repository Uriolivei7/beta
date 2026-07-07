package com.facebook.react.bridge;

import androidx.core.util.Pools$SimplePool;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
class DynamicFromMap implements Dynamic {
    private static final ThreadLocal<Pools$SimplePool> sPool = new ThreadLocal<Pools$SimplePool>() { // from class: com.facebook.react.bridge.DynamicFromMap.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Pools$SimplePool initialValue() {
            return new Pools$SimplePool(10);
        }
    };
    private ReadableMap mMap;
    private String mName;

    private DynamicFromMap() {
    }

    public static DynamicFromMap create(ReadableMap readableMap, String str) {
        DynamicFromMap dynamicFromMap = (DynamicFromMap) sPool.get().b();
        if (dynamicFromMap == null) {
            dynamicFromMap = new DynamicFromMap();
        }
        dynamicFromMap.mMap = readableMap;
        dynamicFromMap.mName = str;
        return dynamicFromMap;
    }

    @Override // com.facebook.react.bridge.Dynamic
    public ReadableArray asArray() {
        String str;
        ReadableMap readableMap = this.mMap;
        if (readableMap == null || (str = this.mName) == null) {
            throw new IllegalStateException("This dynamic value has been recycled");
        }
        return readableMap.getArray(str);
    }

    @Override // com.facebook.react.bridge.Dynamic
    public boolean asBoolean() {
        String str;
        ReadableMap readableMap = this.mMap;
        if (readableMap == null || (str = this.mName) == null) {
            throw new IllegalStateException("This dynamic value has been recycled");
        }
        return readableMap.getBoolean(str);
    }

    @Override // com.facebook.react.bridge.Dynamic
    public double asDouble() {
        String str;
        ReadableMap readableMap = this.mMap;
        if (readableMap == null || (str = this.mName) == null) {
            throw new IllegalStateException("This dynamic value has been recycled");
        }
        return readableMap.getDouble(str);
    }

    @Override // com.facebook.react.bridge.Dynamic
    public int asInt() {
        String str;
        ReadableMap readableMap = this.mMap;
        if (readableMap == null || (str = this.mName) == null) {
            throw new IllegalStateException("This dynamic value has been recycled");
        }
        return readableMap.getInt(str);
    }

    @Override // com.facebook.react.bridge.Dynamic
    public ReadableMap asMap() {
        String str;
        ReadableMap readableMap = this.mMap;
        if (readableMap == null || (str = this.mName) == null) {
            throw new IllegalStateException("This dynamic value has been recycled");
        }
        ReadableMap map = readableMap.getMap(str);
        Objects.requireNonNull(map);
        return map;
    }

    @Override // com.facebook.react.bridge.Dynamic
    public String asString() {
        String str;
        ReadableMap readableMap = this.mMap;
        if (readableMap == null || (str = this.mName) == null) {
            throw new IllegalStateException("This dynamic value has been recycled");
        }
        return readableMap.getString(str);
    }

    @Override // com.facebook.react.bridge.Dynamic
    public ReadableType getType() {
        String str;
        ReadableMap readableMap = this.mMap;
        if (readableMap == null || (str = this.mName) == null) {
            throw new IllegalStateException("This dynamic value has been recycled");
        }
        return readableMap.getType(str);
    }

    @Override // com.facebook.react.bridge.Dynamic
    public boolean isNull() {
        String str;
        ReadableMap readableMap = this.mMap;
        if (readableMap == null || (str = this.mName) == null) {
            throw new IllegalStateException("This dynamic value has been recycled");
        }
        return readableMap.isNull(str);
    }

    @Override // com.facebook.react.bridge.Dynamic
    public void recycle() {
        this.mMap = null;
        this.mName = null;
        sPool.get().a(this);
    }
}
