package classes;

public enum WaterCondition {
    
    Waste("Waste"),
    Treatable_Clear("Treatable_Clear"),
    Treatable_Muddy("Treatable_Muddy"),
    Potable("Potable");

    private String condition;

    /**
     * Constructor with one parameter.
     *
     * @param condition the condition what user specified about the water source
     */
    WaterCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Set condition with what user select.
     *
     * @param condition the condition what user specified
     *                  about the water source
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Get the condition of the water resource.
     *
     * @return the condition of the water
     */
    public String getCondition() {
        return condition;
    }
}
