package net.akoot.plugins.ultravanilla.serializable;

import net.akoot.plugins.ultravanilla.UltraVanilla;
import net.akoot.plugins.ultravanilla.reference.Palette;
import net.akoot.plugins.ultravanilla.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("Pos")
public class PositionLite implements ConfigurationSerializable {

    private int x, y, z;
    private double yaw, pitch;
    private String name, world;

    public PositionLite(int x, int y, int z, double yaw, double pitch, String world) {
        this(null, x, y, z, yaw, pitch, world);
    }

    public PositionLite(int x, int y, int z, String world) {
        this(null, x, y, z, 0, 0, world);
    }

    public PositionLite(String name, int x, int y, int z, double yaw, double pitch) {
        this(name, x, y, z, yaw, pitch, "world");
    }

    public PositionLite(String name, int x, int y, int z) {
        this(name, x, y, z, 0, 0, "world");
    }

    public PositionLite(String name, int x, int y, int z, String world) {
        this(name, x, y, z, 0, 0, world);
    }

    public PositionLite(String name, int x, int y, int z, double yaw, double pitch, String world) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public PositionLite(Position position) {
        this(
                position.getName(),
                (int) Math.floor(position.getX()),
                (int) Math.floor(position.getX()),
                (int) Math.floor(position.getX()),
                position.getYaw(),
                position.getPitch(),
                Bukkit.getServer().getWorld(position.getWorld()).getName()
        );
    }

    public PositionLite(Location location) {
        this(null, location);
    }

    public PositionLite(int x, int y, int z, double yaw, double pitch) {
        this(null, x, y, z, yaw, pitch, "world");
    }

    public PositionLite(int x, int y, int z) {
        this(null, x, y, z, 0, 0, "world");
    }

    public PositionLite(int x, int y, int z, String name, String world) {
        this(name, x, y, z, 0, 0, world);
    }

    public PositionLite(String name, Location location) {
        this(
                name,
                (int) Math.floor(location.getX()),
                (int) Math.floor(location.getY()),
                (int) Math.floor(location.getZ()),
                location.getYaw(),
                location.getPitch(),
                location.getWorld().getName()
        );
    }

    public static PositionLite deserialize(Map<String, Object> map) {
        String[] pos = map.get("pos").toString().split(" ");
        String name = map.containsKey("name") ? map.get("name").toString() : null;
        String world = map.containsKey("world") ? map.get("world").toString() : "world";
        double yaw = 0, pitch = 0;
        if (pos.length == 5) {
            yaw = Double.parseDouble(pos[3]);
            pitch = Double.parseDouble(pos[4]);
        }
        return new PositionLite(
                name,
                Integer.parseInt(pos[0]),
                Integer.parseInt(pos[1]),
                Integer.parseInt(pos[2]),
                yaw,
                pitch,
                world
        );
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorldName() {
        return world;
    }

    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Position getPosition() {
        return new Position(name, getWorld().getUID(), x, y, z, (float) pitch, (float) yaw);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        String pos = String.format("%d %d %d", x, y, z) + (yaw != 0 ? String.format(" %.2f %.2f", yaw, pitch) : "");
        map.put("pos", pos);
        if (name != null) map.put("name", name);
        if (!world.equals("world")) map.put("world", world);
        return map;
    }

    public Location getLocation() {
        return new Location(getWorld(), x, y, z, (float) yaw, (float) pitch);
    }

    @Override
    public String toString() {
        return String.format("%s%d, %d, %d%s",
                name != null ? name + ": " : "",
                x, y, z,
                getOtherWorldName());
    }

    private String getOtherWorldName() {
        return !world.equals("world") ? ", " + world : "";
    }

    private String getWorldStringColored() {
        return UltraVanilla.getInstance().getStrings().getVariable("worlds." + world);
    }

    public String toString(String format) {
        format = StringUtil.replaceIf(format, "name", name != null);
        format = StringUtil.replaceIf(format, "world", !world.equals("world"));
        format = StringUtil.replaceIf(format, "yawpitch", !(yaw == 0 && pitch == 0));
        return Palette.translate(format
                .replaceAll("%x", x + "")
                .replaceAll("%yaw", yaw + "")
                .replaceAll("%y", y + "")
                .replaceAll("%z", z + "")
                .replaceAll("%pitch", pitch + "")
                .replaceAll("%world_colored", getWorldStringColored())
                .replaceAll("%world", world)
                .replaceAll("%name", name != null ? name : ""));
    }
}
