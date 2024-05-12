package Types;

public enum Color {
    RED(java.awt.Color.RED),
    YELLOW(java.awt.Color.YELLOW),
    GREEN(java.awt.Color.GREEN),
    LIGHT_BLUE(new java.awt.Color(51, 153, 255)),
    BLACK(java.awt.Color.BLACK);

    private final java.awt.Color color;

    Color(java.awt.Color color) {
        this.color = color;
    }

    public java.awt.Color getColor() {
        return color;
    }
}
