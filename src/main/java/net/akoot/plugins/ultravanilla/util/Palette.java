package net.akoot.plugins.ultravanilla.util;

public class Palette {
    private String id;
    private String name;
    private String description;
    private PaletteColor[] colors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PaletteColor[] getColors() {
        return colors;
    }

    public void setColors(PaletteColor[] colors) {
        this.colors = colors;
    }
}
