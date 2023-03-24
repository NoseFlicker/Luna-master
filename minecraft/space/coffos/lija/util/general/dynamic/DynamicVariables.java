package space.coffos.lija.util.general.dynamic;

import java.util.HashMap;

public class DynamicVariables {

    private static HashMap<String, Object> varList = new HashMap<>();
    private static String BROX = "\u00ADÅÄ_\"pÅA09258wAJSKZA>>*>";

    public static void initVar(String varName, Object varValue, boolean overrideExisting, boolean useBROX) {
        if (varList.containsKey(varName) && !overrideExisting) return;
        varList.put(varName, useBROX ? BROX + varValue : varValue);
    }

    public static Object pullVar(String varName, boolean hasBROX) {
        if (!varList.containsKey(varName)) initVar(varName, null, true, false);
        return hasBROX ? varList.getOrDefault(varName, null).toString().replace(BROX, "") : varList.getOrDefault(varName, null);
    }

    HashMap<String, Object> getVar() {
        return varList;
    }
}