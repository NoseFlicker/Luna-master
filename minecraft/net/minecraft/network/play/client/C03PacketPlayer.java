package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import space.coffos.lija.LiJA;
import space.coffos.lija.impl.elements.luna.movement.flight.Flight;
import space.coffos.lija.impl.elements.luna.player.NoFall;
import space.coffos.lija.util.packets.PacketUtils;

import java.io.IOException;

import static space.coffos.lija.api.element.Element.mc;

public class C03PacketPlayer implements Packet {
    private static PacketUtils packetUtils = new PacketUtils();
    public static boolean rotating;
    public static boolean moving;
    public static float pitch;
    public static float yaw;
    public static double x;
    public static double y;
    public static double z;
    boolean field_149474_g;
    boolean field_149480_h;

    public C03PacketPlayer() {
    }

    public C03PacketPlayer(boolean p_i45256_1_) {
        this.field_149474_g = p_i45256_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processPlayer(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_149474_g = data.readUnsignedByte() != 0;
    }

    public double getX(double defaultValue) {
        return this.moving ? this.x : defaultValue;
    }

    public double getY(double defaultValue) {
        return this.moving ? this.y : defaultValue;
    }

    public double getZ(double defaultValue) {
        return this.moving ? this.z : defaultValue;
    }

    public void setField_149474_g(boolean field_149474_g) {
        this.field_149474_g = field_149474_g;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeByte(this.field_149474_g ? 1 : LiJA.INSTANCE.elementManager.getElement(NoFall.class).isToggled() ? 1 : 0);
    }

    public double getPositionX() {
        return this.x;
    }

    public double getPositionY() {
        return this.y;
    }

    public double getPositionZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public boolean func_149465_i() {
        return this.field_149474_g = LiJA.INSTANCE.elementManager.getElement(NoFall.class).isToggled() | this.field_149474_g;
    }

    public boolean func_149466_j() {
        return this.field_149480_h;
    }

    public boolean getRotating() {
        return this.rotating;
    }

    public void func_149469_a(boolean p_149469_1_) {
        this.field_149480_h = p_149469_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer) handler);
    }

    public static class C04PacketPlayerPosition extends C03PacketPlayer {

        public C04PacketPlayerPosition() {
            this.field_149480_h = true;
        }

        public C04PacketPlayerPosition(double p_i45942_1_, double p_i45942_3_, double p_i45942_5_, boolean p_i45942_7_) {
            this.x = p_i45942_1_;
            this.y = p_i45942_3_;
            this.z = p_i45942_5_;
            this.field_149474_g = !(LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled() & Flight.mode.getValString().equalsIgnoreCase("Hypixel Packet")) && p_i45942_7_;
            this.field_149480_h = true;
        }

        public void readPacketData(PacketBuffer data) throws IOException {
            this.x = data.readDouble();
            this.y = data.readDouble();
            this.z = data.readDouble();
            super.readPacketData(data);
        }

        public void writePacketData(PacketBuffer data) throws IOException {
            data.writeDouble(packetUtils.getState() ? packetUtils.xC == 0 ? this.x : this.x + packetUtils.xC : this.x);
            packetUtils.cX = this.x;
            data.writeDouble(packetUtils.getState() ? packetUtils.yC == 0 ? this.y : this.y + packetUtils.yC : this.y);
            packetUtils.cY = this.y;
            data.writeDouble(packetUtils.getState() ? packetUtils.zC == 0 ? this.z : this.z + packetUtils.zC : this.z);
            packetUtils.cZ = this.z;
            super.writePacketData(data);
        }

        public void processPacket(INetHandler handler) {
            super.processPacket((INetHandlerPlayServer) handler);
        }
    }

    public static class C05PacketPlayerLook extends C03PacketPlayer {

        public C05PacketPlayerLook() {
            this.rotating = true;
        }

        public C05PacketPlayerLook(float p_i45255_1_, float p_i45255_2_, boolean p_i45255_3_) {
            this.yaw = p_i45255_1_;
            this.pitch = p_i45255_2_;
            this.field_149474_g = p_i45255_3_;
            this.rotating = true;
        }

        public void readPacketData(PacketBuffer data) throws IOException {
            this.yaw = data.readFloat();
            this.pitch = LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled() & Flight.mode.getValString().equalsIgnoreCase("Hypixel Packet") ? 58 : data.readFloat();
            super.readPacketData(data);
        }

        public void writePacketData(PacketBuffer data) throws IOException {
            data.writeFloat(this.yaw);
            data.writeFloat(LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled() & Flight.mode.getValString().equalsIgnoreCase("Hypixel Packet") ? 58 : this.pitch);
            super.writePacketData(data);
        }

        public void processPacket(INetHandler handler) {
            super.processPacket((INetHandlerPlayServer) handler);
        }
    }

    public static class C06PacketPlayerPosLook extends C03PacketPlayer {

        public C06PacketPlayerPosLook() {
            this.field_149480_h = true;
            this.rotating = true;
        }

        public C06PacketPlayerPosLook(double p_i45941_1_, double p_i45941_3_, double p_i45941_5_, float p_i45941_7_, float p_i45941_8_, boolean p_i45941_9_) {
            this.x = p_i45941_1_;
            this.y = p_i45941_3_;
            this.z = p_i45941_5_;
            this.yaw = p_i45941_7_;
            this.pitch = p_i45941_8_;
            this.field_149474_g = !(LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled() & Flight.mode.getValString().equalsIgnoreCase("Hypixel Packet")) && p_i45941_9_;
            this.rotating = true;
            this.field_149480_h = true;
        }

        public void readPacketData(PacketBuffer data) throws IOException {
            this.x = data.readDouble();
            this.y = data.readDouble();
            this.z = data.readDouble();
            this.yaw = LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled() & Flight.mode.getValString().equalsIgnoreCase("Hypixel Packet") ? -90 : data.readFloat();
            this.pitch = LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled() & Flight.mode.getValString().equalsIgnoreCase("Hypixel Packet") ? 70 : data.readFloat();
            super.readPacketData(data);
        }

        public void writePacketData(PacketBuffer data) throws IOException {
            data.writeDouble(this.x);
            data.writeDouble(this.y);
            data.writeDouble(this.z);
            data.writeFloat(LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled() & Flight.mode.getValString().equalsIgnoreCase("Hypixel Packet") ? -90 : this.yaw);
            data.writeFloat(LiJA.INSTANCE.elementManager.getElement(Flight.class).isToggled() & Flight.mode.getValString().equalsIgnoreCase("Hypixel Packet") ? 70 : this.pitch);
            super.writePacketData(data);
        }

        public void processPacket(INetHandler handler) {
            super.processPacket((INetHandlerPlayServer) handler);
        }
    }
}