package classes;

public enum OverallCondition {
    Safe("Safe"),
    Treatable("Treatable"),
    Unsafe("Unsafe");

    private String condition;

    /**
     * Constructor with one parameter.
     *
     * @param condition the condition what user specified about the water source
     */
    OverallCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Set condition with what user selects.
     *
     * @param condition the condition what user specified
     *                  about the water source
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets the overall condition of the water resource.
     *
     * @return the condition of the water
     */
    public String getCondition() {
        return condition;
    }
}