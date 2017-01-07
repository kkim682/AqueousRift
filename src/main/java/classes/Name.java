package classes;

public class Name {

    private String firstName;
    private String lastName;
    private String prefix;

    /**
     * Name constructor
     * @param   firstName - firstName attribute
     * @param   lastName - lastName attribute
     * @param   prefix - prefix attribute
     */
    public Name(String firstName, String lastName, String prefix) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.prefix = prefix;
    }

    /**
     * Constructor without prefix
     * @param   firstName - firstName attribute
     * @param   lastName - lastName attribute
     */
    public Name(String firstName, String lastName) {
        this(firstName, lastName, "");
    }

    /**
     * No Args constructor for fire-base
     */
    public Name() {

    }

    /**
     * Public getter
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Public getter
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Public getter
     * @return prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * String format for Name
     * @return A person's name with all fields.
     */
    public String toString() {
        return prefix + lastName + ", " + firstName;
    }
}
