package space.coffos.lija.impl.managers;

import space.coffos.lija.api.event.Data;
import space.coffos.lija.api.event.Event;
import space.coffos.lija.api.event.EventRegister;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {

    private HashMap<Class<? extends Event>, CopyOnWriteArrayList<Data>> REGISTRY_MAP;

    public EventManager() {
        REGISTRY_MAP = new HashMap<>();
    }

    public void register(Object o) {

        Arrays.stream(o.getClass().getDeclaredMethods()).filter(method -> !isMethodBad(method)).forEach(method -> register(method, o));

        REGISTRY_MAP.values().forEach(flexibleArray -> flexibleArray.sort(((o1, o2) -> (o1.getPriority().getValue() - o2.getPriority().getValue()))));
    }

    private void register(Method method, Object o) {

        Class<? extends Event> clazz = (Class<? extends Event>) method.getParameterTypes()[0];

        Data methodData = new Data(o, method, method.getAnnotation(EventRegister.class).priority());

        if (!methodData.getTarget().isAccessible())
            methodData.getTarget().setAccessible(true);

        if (REGISTRY_MAP.containsKey(clazz)) {
            if (!REGISTRY_MAP.get(clazz).contains(methodData))
                REGISTRY_MAP.get(clazz).add(methodData);
        } else REGISTRY_MAP.put(clazz, new CopyOnWriteArrayList<>(Collections.singletonList(methodData)));
    }

    public void unregister(Object o) {
        REGISTRY_MAP.values().forEach(flexibleArray -> flexibleArray.removeIf(methodData -> methodData.getSource().equals(o)));
        REGISTRY_MAP.entrySet().removeIf(hashSetEntry -> hashSetEntry.getValue().isEmpty());
    }

    private boolean isMethodBad(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventRegister.class);
    }

    public CopyOnWriteArrayList<Data> get(Class<? extends Event> clazz) {
        return REGISTRY_MAP.get(clazz);
    }
}