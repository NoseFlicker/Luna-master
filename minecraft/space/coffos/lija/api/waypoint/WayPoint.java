package space.coffos.lija.api.waypoint;

import com.google.gson.annotations.SerializedName;

public class WayPoint {

    @SerializedName(value = "name")
    private String name;

    @SerializedName(value = "server")
    private String serverIP;

    @SerializedName(value = "x")
    private double x;

    @SerializedName(value = "y")
    private double y;

    @SerializedName(value = "z")
    private double z;

    public WayPoint(String name, String serverIP, double x, double y, double z) {
        this.name = name;
        this.serverIP = serverIP;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public String getServerIP() {
        return serverIP;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}