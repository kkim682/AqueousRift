package classes;

public enum WaterType {
    Bottled("Bottled"),
    Well("Well"),
    Stream("Stream"),
    Lake("Lake"),
    Spring("Spring"),
    Other("Other");

    private String type;

    /**
     * Constructor with one parameter.
     *
     * @param type the type what user specified about the water source
     */
    WaterType(String type) {
        this.type = type;
    }

    /**
     * Set type with what user select.
     *
     * @param type the type what user specified about the water source
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the type of the water resource.
     *
     * @return the type of the water
     */
    public String getType() {
        return type;
    }
}
