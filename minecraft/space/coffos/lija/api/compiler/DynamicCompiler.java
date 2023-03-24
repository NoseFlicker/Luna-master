package space.coffos.lija.api.compiler;

import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.util.entity.PlayerUtils;

import java.io.IOException;

public class DynamicCompiler {

    public static void runCodeFromString(String jPackage, String javaCode) {
        try {
            Class clazz = CompilerUtils.CACHED_COMPILER.loadFromJava(jPackage, javaCode);
            Runnable run = (Runnable) clazz.newInstance();
            run.run();
            PlayerUtils.tellPlayer(">> Code executed, check the output for more details.", false);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void implementClass(String jPackage, String pathToJavaFile) {
        try {
            Class test = CompilerUtils.loadFromResource(jPackage, pathToJavaFile);
            LiJA.INSTANCE.elementManager.elements.add((Element) test.newInstance());
            LiJA.INSTANCE.elementManager.addElement((Element) test.newInstance());
            PlayerUtils.tellPlayer("Added element => " + ((Element) test.newInstance()).getName(), false);
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}