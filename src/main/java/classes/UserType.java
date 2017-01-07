package classes;

/**
 * enum class of user types
 **/

public enum UserType {
    GeneralUser("User"),
    Admin("Admin"),
    Worker("Worker"),
    Manager("Manager");

    private String role = "User";

    /**
     * Constructor for the enumeration
     *
     * @param role   User type
     */
    UserType(String role) {
        this.role = role;
    }

    /**
     * @return the display string representation of the user type
     */
    public String toString() {
        return role;
    }
}
