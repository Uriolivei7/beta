package androidx.lifecycle;

import androidx.lifecycle.AbstractC0299g;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: renamed from: androidx.lifecycle.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
final class C0294b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static C0294b f5312c = new C0294b();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Map f5313a = new HashMap();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Map f5314b = new HashMap();

    /* JADX INFO: renamed from: androidx.lifecycle.b$a */
    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Map f5315a = new HashMap();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final Map f5316b;

        a(Map map) {
            this.f5316b = map;
            for (Map.Entry entry : map.entrySet()) {
                AbstractC0299g.a aVar = (AbstractC0299g.a) entry.getValue();
                List arrayList = (List) this.f5315a.get(aVar);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    this.f5315a.put(aVar, arrayList);
                }
                arrayList.add((C0078b) entry.getKey());
            }
        }

        private static void b(List list, l lVar, AbstractC0299g.a aVar, Object obj) {
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    ((C0078b) list.get(size)).a(lVar, aVar, obj);
                }
            }
        }

        void a(l lVar, AbstractC0299g.a aVar, Object obj) {
            b((List) this.f5315a.get(aVar), lVar, aVar, obj);
            b((List) this.f5315a.get(AbstractC0299g.a.ON_ANY), lVar, aVar, obj);
        }
    }

    /* JADX INFO: renamed from: androidx.lifecycle.b$b, reason: collision with other inner class name */
    static final class C0078b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final int f5317a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final Method f5318b;

        C0078b(int i3, Method method) {
            this.f5317a = i3;
            this.f5318b = method;
            method.setAccessible(true);
        }

        void a(l lVar, AbstractC0299g.a aVar, Object obj) {
            try {
                int i3 = this.f5317a;
                if (i3 == 0) {
                    this.f5318b.invoke(obj, new Object[0]);
                } else if (i3 == 1) {
                    this.f5318b.invoke(obj, lVar);
                } else {
                    if (i3 != 2) {
                        return;
                    }
                    this.f5318b.invoke(obj, lVar, aVar);
                }
            } catch (IllegalAccessException e4) {
                throw new RuntimeException(e4);
            } catch (InvocationTargetException e5) {
                throw new RuntimeException("Failed to call observer method", e5.getCause());
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof C0078b)) {
                return false;
            }
            C0078b c0078b = (C0078b) obj;
            return this.f5317a == c0078b.f5317a && this.f5318b.getName().equals(c0078b.f5318b.getName());
        }

        public int hashCode() {
            return (this.f5317a * 31) + this.f5318b.getName().hashCode();
        }
    }

    C0294b() {
    }

    private a a(Class cls, Method[] methodArr) {
        int i3;
        a aVarC;
        Class superclass = cls.getSuperclass();
        HashMap map = new HashMap();
        if (superclass != null && (aVarC = c(superclass)) != null) {
            map.putAll(aVarC.f5316b);
        }
        for (Class<?> cls2 : cls.getInterfaces()) {
            for (Map.Entry entry : c(cls2).f5316b.entrySet()) {
                e(map, (C0078b) entry.getKey(), (AbstractC0299g.a) entry.getValue(), cls);
            }
        }
        if (methodArr == null) {
            methodArr = b(cls);
        }
        boolean z3 = false;
        for (Method method : methodArr) {
            r rVar = (r) method.getAnnotation(r.class);
            if (rVar != null) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length <= 0) {
                    i3 = 0;
                } else {
                    if (!l.class.isAssignableFrom(parameterTypes[0])) {
                        throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner");
                    }
                    i3 = 1;
                }
                AbstractC0299g.a aVarValue = rVar.value();
                if (parameterTypes.length > 1) {
                    if (!AbstractC0299g.a.class.isAssignableFrom(parameterTypes[1])) {
                        throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
                    }
                    if (aVarValue != AbstractC0299g.a.ON_ANY) {
                        throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
                    }
                    i3 = 2;
                }
                if (parameterTypes.length > 2) {
                    throw new IllegalArgumentException("cannot have more than 2 params");
                }
                e(map, new C0078b(i3, method), aVarValue, cls);
                z3 = true;
            }
        }
        a aVar = new a(map);
        this.f5313a.put(cls, aVar);
        this.f5314b.put(cls, Boolean.valueOf(z3));
        return aVar;
    }

    private Method[] b(Class cls) {
        try {
            return cls.getDeclaredMethods();
        } catch (NoClassDefFoundError e4) {
            throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", e4);
        }
    }

    private void e(Map map, C0078b c0078b, AbstractC0299g.a aVar, Class cls) {
        AbstractC0299g.a aVar2 = (AbstractC0299g.a) map.get(c0078b);
        if (aVar2 == null || aVar == aVar2) {
            if (aVar2 == null) {
                map.put(c0078b, aVar);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Method " + c0078b.f5318b.getName() + " in " + cls.getName() + " already declared with different @OnLifecycleEvent value: previous value " + aVar2 + ", new value " + aVar);
    }

    a c(Class cls) {
        a aVar = (a) this.f5313a.get(cls);
        return aVar != null ? aVar : a(cls, null);
    }

    boolean d(Class cls) {
        Boolean bool = (Boolean) this.f5314b.get(cls);
        if (bool != null) {
            return bool.booleanValue();
        }
        Method[] methodArrB = b(cls);
        for (Method method : methodArrB) {
            if (((r) method.getAnnotation(r.class)) != null) {
                a(cls, methodArrB);
                return true;
            }
        }
        this.f5314b.put(cls, Boolean.FALSE);
        return false;
    }
}
