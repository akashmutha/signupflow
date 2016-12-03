package loginscreen.solution.example.com.loginscreen.Util;

/**
 * Created by mutha on 27/10/16.
 */

public class Constants {

    public static final String USERS_DB_NAME = "user_db";
    public static final String USERS_TABLE_NAME = "users";

    public static final String VALID_EMAIL_ADDRESS_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    public static final String VALID_PHONE_NUMBER_REGEX = "^[0-9]{10}$";
    public static final String VALID_NAME_REGEX = "^[a-zA-Z0-9 ]+$";
    public static final String VALID_PASSWORD_REGEX =
    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^0-9a-zA-Z]).{6,}$";

    private Constants(){

    }
}
