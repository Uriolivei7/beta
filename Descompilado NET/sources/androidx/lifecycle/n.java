package androidx.lifecycle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final n f5341a = new n();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Map f5342b = new HashMap();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final Map f5343c = new HashMap();

    private n() {
    }

    private final InterfaceC0297e a(Constructor constructor, Object obj) {
        try {
            Object objNewInstance = constructor.newInstance(obj);
            D2.h.e(objNewInstance, "{\n            constructo…tance(`object`)\n        }");
            androidx.activity.result.d.a(objNewInstance);
            return null;
        } catch (IllegalAccessException e4) {
            throw new RuntimeException(e4);
        } catch (InstantiationException e5) {
            throw new RuntimeException(e5);
        } catch (InvocationTargetException e6) {
            throw new RuntimeException(e6);
        }
    }

    private final Constructor b(Class cls) {
        try {
            Package r02 = cls.getPackage();
            String canonicalName = cls.getCanonicalName();
            String name = r02 != null ? r02.getName() : "";
            D2.h.e(name, "fullPackage");
            if (name.length() != 0) {
                D2.h.e(canonicalName, "name");
                canonicalName = canonicalName.substring(name.length() + 1);
                D2.h.e(canonicalName, "this as java.lang.String).substring(startIndex)");
            }
            D2.h.e(canonicalName, "if (fullPackage.isEmpty(…g(fullPackage.length + 1)");
            String strC = c(canonicalName);
            if (name.length() != 0) {
                strC = name + '.' + strC;
            }
            Class<?> cls2 = Class.forName(strC);
            D2.h.d(cls2, "null cannot be cast to non-null type java.lang.Class<out androidx.lifecycle.GeneratedAdapter>");
            Constructor<?> declaredConstructor = cls2.getDeclaredConstructor(cls);
            if (declaredConstructor.isAccessible()) {
                return declaredConstructor;
            }
            declaredConstructor.setAccessible(true);
            return declaredConstructor;
        } catch (ClassNotFoundException unused) {
            return null;
        } catch (NoSuchMethodException e4) {
            throw new RuntimeException(e4);
        }
    }

    public static final String c(String str) {
        D2.h.f(str, "className");
        return K2.o.v(str, ".", "_", false, 4, null) + "_LifecycleAdapter";
    }

    private final int d(Class cls) {
        Map map = f5342b;
        Integer num = (Integer) map.get(cls);
        if (num != null) {
            return num.intValue();
        }
        int iG = g(cls);
        map.put(cls, Integer.valueOf(iG));
        return iG;
    }

    private final boolean e(Class cls) {
        return cls != null && k.class.isAssignableFrom(cls);
    }

    public static final InterfaceC0302j f(Object obj) {
        D2.h.f(obj, "object");
        boolean z3 = obj instanceof InterfaceC0302j;
        boolean z4 = obj instanceof InterfaceC0295c;
        if (z3 && z4) {
            return new DefaultLifecycleObserverAdapter((InterfaceC0295c) obj, (InterfaceC0302j) obj);
        }
        if (z4) {
            return new DefaultLifecycleObserverAdapter((InterfaceC0295c) obj, null);
        }
        if (z3) {
            return (InterfaceC0302j) obj;
        }
        Class<?> cls = obj.getClass();
        n nVar = f5341a;
        if (nVar.d(cls) != 2) {
            return new ReflectiveGenericLifecycleObserver(obj);
        }
        Object obj2 = f5343c.get(cls);
        D2.h.c(obj2);
        List list = (List) obj2;
        if (list.size() == 1) {
            nVar.a((Constructor) list.get(0), obj);
            return new SingleGeneratedAdapterObserver(null);
        }
        int size = list.size();
        InterfaceC0297e[] interfaceC0297eArr = new InterfaceC0297e[size];
        for (int i3 = 0; i3 < size; i3++) {
            f5341a.a((Constructor) list.get(i3), obj);
            interfaceC0297eArr[i3] = null;
        }
        return new CompositeGeneratedAdaptersObserver(interfaceC0297eArr);
    }

    private final int g(Class cls) {
        ArrayList arrayList;
        if (cls.getCanonicalName() == null) {
            return 1;
        }
        Constructor constructorB = b(cls);
        if (constructorB != null) {
            f5343c.put(cls, AbstractC0717n.b(constructorB));
            return 2;
        }
        if (C0294b.f5312c.d(cls)) {
            return 1;
        }
        Class superclass = cls.getSuperclass();
        if (e(superclass)) {
            D2.h.e(superclass, "superclass");
            if (d(superclass) == 1) {
                return 1;
            }
            Object obj = f5343c.get(superclass);
            D2.h.c(obj);
            arrayList = new ArrayList((Collection) obj);
        } else {
            arrayList = null;
        }
        Class<?>[] interfaces = cls.getInterfaces();
        D2.h.e(interfaces, "klass.interfaces");
        for (Class<?> cls2 : interfaces) {
            if (e(cls2)) {
                D2.h.e(cls2, "intrface");
                if (d(cls2) == 1) {
                    return 1;
                }
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                Object obj2 = f5343c.get(cls2);
                D2.h.c(obj2);
                arrayList.addAll((Collection) obj2);
            }
        }
        if (arrayList == null) {
            return 1;
        }
        f5343c.put(cls, arrayList);
        return 2;
    }
}
