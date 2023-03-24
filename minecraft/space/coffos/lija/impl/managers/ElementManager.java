package space.coffos.lija.impl.managers;

import org.reflections.Reflections;
import space.coffos.lija.LiJA;
import space.coffos.lija.api.element.Category;
import space.coffos.lija.api.element.Element;
import space.coffos.lija.api.element.ElementStructure;
import space.coffos.lija.api.event.EventRegister;
import space.coffos.lija.api.manager.Handler;
import space.coffos.lija.impl.events.EventKeyBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ElementManager extends Handler<Element> {

    /**
     * A list of all the elements.
     */
    public static ArrayList<Element> elements = new ArrayList<>();

    /**
     * Manager for all Elements.
     */
    public ElementManager() {
        LiJA.INSTANCE.eventManager.register(this);
        Reflections reflections = new Reflections();

        /* Grab all Elements that extends "Element" */
        for (Class<? extends Element> clazz : reflections.getSubTypesOf(Element.class)) {
            try {
                Element element = clazz.newInstance();
                System.out.println(element.getClientType());
                if (!element.getName().equalsIgnoreCase("Glide") & !element.getName().equalsIgnoreCase("Test") & !element.getName().equalsIgnoreCase("Step") & !element.getName().equalsIgnoreCase("Basic") & !element.getName().equalsIgnoreCase("Basic Modes")) {
                    System.out.println("Added element => " + element.getName());
                    elements.add(element);
                    addElement(element);
                } else
                    System.err.println("Killed invalid elements");
            } catch (Exception ignored) {
            }
        }
    }

    private static boolean findAnnotation(Element module) {
        return module.getClass().isAnnotationPresent(ElementStructure.class);
    }

    /**
     * Add a array of elements.
     */
    public void addElement(Element... mods) {
        Arrays.stream(mods).filter(ElementManager::findAnnotation).forEach(module -> getContents().add(module));
    }

    /**
     * Remove a array of elements.
     */
    public void removeElement(Element... mods) {
        Arrays.stream(mods).filter(ElementManager::findAnnotation).forEach(module -> getContents().remove(module));
    }

    /**
     * Get a list of elements in the specified category.
     */
    public ArrayList<Element> getElementsForCategory(Category c) {
        return getContents().stream().filter(m -> m.getCategory().equals(c)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get a list of elements that's support for the specified sub-client.
     */
    public ArrayList<Element> getElementsForClient(String subClient) {
        return getContents().stream().filter(m -> m.getClientType().equalsIgnoreCase(subClient)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get a list of toggled elements.
     */
    public ArrayList<Element> getToggledElements() {
        return getContents().stream().filter(Element::isToggled).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get a element by it's class. Ex: getElement(Speed.class)
     */
    public Element getElement(Class<? extends Element> clazz) {
        return getContents().stream().filter(element -> element.getClass() == clazz).findFirst().orElse(null);
    }

    /**
     * Get a element by it's name.
     */
    public Element getElement(String elementName) {
        return getContents().stream().filter(m -> m.getName().equalsIgnoreCase(elementName)).findFirst().orElse(null);
    }

    @EventRegister
    public void onKeyPress(EventKeyBoard eventKeyBoard) {
        getContents().stream().filter(m -> m.getKeyCode() == eventKeyBoard.getKey()).forEachOrdered(Element::toggle);
    }
}