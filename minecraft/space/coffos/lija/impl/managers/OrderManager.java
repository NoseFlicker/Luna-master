package space.coffos.lija.impl.managers;

import org.reflections.Reflections;
import space.coffos.lija.api.order.Order;
import space.coffos.lija.util.entity.PlayerUtils;

import java.util.HashMap;
import java.util.Map;

public class OrderManager {

    /*
     * The HashMap that holds all the orders.
     */
    public HashMap<String[], Order> orders;

    /*
     * The prefix that holds the prefix required for the orders.
     */
    public String prefix;

    public OrderManager() {
        orders = new HashMap<>();
        prefix = "-";
        Reflections reflections = new Reflections();

        /* Grab all Orders that extends "Element" */

        reflections.getSubTypesOf(Order.class).forEach(clazz -> {
            try {
                Order order = clazz.newInstance();
                System.out.println("Added order -> " + order.getClass().getSimpleName().replace("Order", ""));
                orders.put(new String[]{order.getClass().getSimpleName().toLowerCase().replace("order", "")}, order);
            } catch (Exception ignored) {
            }
        });
    }


    public boolean processOrder(String rawMessage) {
        /*
         * Checks if the rawMessage starts if the prefix for the orders. If it does
         * not start with the prefix it does not process the command.
         */
        if (!rawMessage.startsWith(prefix))
            return false;

        /* Checks if the rawMessage has any text after the prefix. */
        boolean safe = rawMessage.split(prefix).length > 1;

        /*
         * If the rawMessage has any text after the prefix it will continue to process
         * the command.
         */
        if (safe) {
            /* Gets rid of the prefix from the rawMessage. */
            String beheaded = rawMessage.split(prefix)[1];

            /*
             * Splits the beheaded message at empty spaces so it can be sent to the command.
             */
            String[] args = beheaded.split(" ");

            /* Gets the command using the start of the array. */
            Order order = getOrder(args[0]);

            /* If a command was found it runs the command. */
            if (order != null) {
                /* If the command failed it tell the user how to use that command. */
                if (!order.run(args)) PlayerUtils.tellPlayer(order.usage(), false);
            }
            /* If no command was found it tell the user to do the help command. */
            else PlayerUtils.tellPlayer("Try " + prefix + "help.", false);
        }
        /*
         * If there is no text after the prefix, it tell the user to do the help
         * command.
         */
        else PlayerUtils.tellPlayer("Try " + prefix + "help.", false);

        return true;
    }

    /*
     * Goes through all the entries in the HashMap and checks if the name provided
     * is a valid command name.
     */
    private Order getOrder(String name) {
        for (Map.Entry entry : orders.entrySet()) {
            String[] key = (String[]) entry.getKey();
            for (String s : key) if (s.equalsIgnoreCase(name)) return (Order) entry.getValue();
        }
        return null;
    }

    /*
     * Returns the HashMap that contains all the orders.
     */
    public HashMap<String[], Order> getOrders() {
        return orders;
    }
}