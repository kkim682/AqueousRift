package classes;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class PasswordRecoveryManager {

    private static final String ACCOUNT_SID = "AC9fbab034d194c58044ced2"
        + "763e0ecf42";

    private static final String AUTH_TOKEN = "8db783f2a7f0f19502cbfba82"
        + "3ab918f";

    private static final String AQUEOUS_RIFT_PHONE_NUMBER = "+16782573949";
    private String phoneNumber;
    private String password;

    /**
     * Constructor for password recovery manager
     * @param  phoneNumber The phone number to send the password to
     * @param  password    The password associated with the account
     */
    public PasswordRecoveryManager(String phoneNumber, String password) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        this.phoneNumber = "+1" + phoneNumber;
        this.password = password;
        System.out.println("created");
    }

    /**
     * Sends the password given in the constructor
     */
    public void sendPassword() {
        String body = "Password: " + password;
        Message message = Message
            .creator(new PhoneNumber(phoneNumber),
                new PhoneNumber(AQUEOUS_RIFT_PHONE_NUMBER), body).create();
        System.out.println(message.getSid());
    }
}