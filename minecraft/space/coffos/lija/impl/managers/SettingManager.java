package space.coffos.lija.impl.managers;

import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.manager.Handler;
import space.coffos.lija.api.setting.BooleanHandler;
import space.coffos.lija.api.setting.DoubleHandler;
import space.coffos.lija.api.setting.ModeHandler;
import space.coffos.lija.api.setting.Setting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SettingManager extends Handler<Setting> {

    public SettingManager() {
        loadSettings();
    }

    private void loadSettings() {
        for (Element module : LiJA.INSTANCE.elementManager.getContents())
            for (Field field : module.getClass().getDeclaredFields())
                if (field.isAnnotationPresent(ModeHandler.class)) {
                    ArrayList<String> ar = new ArrayList<>(Arrays.asList(field.getAnnotation(ModeHandler.class).options()));
                    addSetting(new Setting(field.getAnnotation(ModeHandler.class).name(), module, field.getAnnotation(ModeHandler.class).currentOption(), ar, field.getAnnotation(ModeHandler.class).locked()), module, field);
                } else if (field.isAnnotationPresent(BooleanHandler.class))
                    addSetting(new Setting(field.getAnnotation(BooleanHandler.class).name(), module, field.getAnnotation(BooleanHandler.class).booleanValue()), module, field);
                else if (field.isAnnotationPresent(DoubleHandler.class))
                    addSetting(new Setting(field.getAnnotation(DoubleHandler.class).name(), module, field.getAnnotation(DoubleHandler.class).currentValue(), field.getAnnotation(DoubleHandler.class).minValue(), field.getAnnotation(DoubleHandler.class).maxValue(), field.getAnnotation(DoubleHandler.class).onlyInt(), field.getAnnotation(DoubleHandler.class).locked()), module, field);
    }

    private void addSetting(Setting setting, Element module, Field field) {
        getContents().add(setting);
        field.setAccessible(true);
        try {
            field.set(module, setting);
        } catch (IllegalAccessException ignored) {
        }
    }


    public ArrayList<Setting> getSettingsByElement(Element mod) {
        ArrayList<Setting> out = getContents().stream().filter(s -> s.getParentMod().equals(mod)).collect(Collectors.toCollection(ArrayList::new));
        return out.isEmpty() ? null : out;
    }

    public Setting getSettingByElementByName(Element mod, String name) {
        return getContents().stream().filter(s -> s.getParentMod().equals(mod) && s.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}