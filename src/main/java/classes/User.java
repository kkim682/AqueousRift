package classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private Name name;
    private StringProperty password = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty phoneNum = new SimpleStringProperty();
    private StringProperty userId = new SimpleStringProperty();
    private StringProperty userType  = new SimpleStringProperty();
    private StringProperty isBanned = new SimpleStringProperty();
    private StringProperty blocked = new SimpleStringProperty();
    private StringProperty securityQuestion = new SimpleStringProperty();
    private StringProperty securityAnswer = new SimpleStringProperty();

    /**
     * User object constructor
     * @param   password - new user password
     * @param   email - new user email
     * @param   phoneNum - new user phone number
     * @param   userId - new user ID
     * @param   name - new user Name object
     * @param   securityQuestion - the security question chosen
     * @param   userType - new user type (general user, manager, admin, worker)
     */
    public User(String password, String email, String phoneNum, String userId,
            Name name, String securityQuestion, String userType) {
        this.name = name;
        this.password.set(password);
        this.email.set(email);
        this.phoneNum.set(phoneNum);
        this.userId.set(userId);
        this.securityQuestion.set(securityQuestion);
        this.userType.set(userType);
        this.isBanned.set("false");
        this.blocked.set("false");
    }

    /**
     * User object constructor w/o prefix
     * @param   password New user password
     * @param   email New user email
     * @param   phoneNum New user phone number
     * @param   userId New user ID
     * @param   securityQuestion - the security question chosen
     * @param   name New user Name object
     */
    public User(String password, String email, String phoneNum, String userId,
            Name name, String securityQuestion) {
        this(password, email, phoneNum, userId, name,
            securityQuestion, "");
    }

    /**
     * getter
     * @return name
     */
    public Name getName() {
        return name;
    }

    /**
     * Public setter for Name.
     * @param name Multi-attribute name object.
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * getter
     * @return password
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Public setter for password.
     * @param password New password.
     */
    public void setPassword(String password) {
        this.password.set(password);
    }

    /**
     * getter
     * @return isBanned
     */
    public String getIsBanned() {
        return isBanned.get();
    }

    /**
     * Public setter for isBanned.
     * @param isBanned "true" if user is bblocked "false" if not
     */
    public void setIsBanned(String isBanned) {
        this.isBanned.set(isBanned);
    }

    /**
     * getter
     * @return blocked
     */
    public String getBlocked() {
        return blocked.get();
    }

    /**
     * Public setter for blocked.
     * @param blocked "true" if user is blocked "false" if not
     */
    public void setBlocked(String blocked) {
        this.blocked.set(blocked);
    }

    /**
     * getter
     * @return security question
     */
    public String getSecurityQuestion() {
        return securityQuestion.get();
    }

    /**
     * Public setter for security question
     * @param securityQuestion of the given user
     */
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion.set(securityQuestion);
    }

    /**
     * getter
     * @return security answer
     */
    public String getSecurityAnswer() {
        return securityAnswer.get();
    }

    /**
     * Public setter for security answer.
     * @param securityAnswer of the given user
     */
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer.set(securityAnswer);
    }

    /**
     * getter
     * @return email
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * Public setter for email.
     * @param email New email.
     */
    public void setEmail(String email) {
        this.email.set(email);
    }

    /**
     * getter
     * @return phone number
     */
    public String getPhoneNum() {
        return phoneNum.get();
    }

    /**
     * Public setter for phoneNum.
     * @param phoneNum New phone number.
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum.set(phoneNum);
    }

    /**
     * getter
     * @return userId
     */
    public String getUserId() {
        return userId.get();
    }

    /**
     * Public setter for userId.
     * @param userId New user ID.
     */
    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    /**
     * getter
     * @return userType
     */
    public String getUserType() {
        return userType.get();
    }

    /**
     * Public setter for UserType.
     * @param userType New type of User.
     */
    public void setUserType(String userType) {
        this.userType.set(userType);
    }

    /**
     * No Args constructor for FireBase
     */
    private User() {

    }
}
