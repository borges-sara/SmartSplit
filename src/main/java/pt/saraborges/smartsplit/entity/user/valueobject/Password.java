package pt.saraborges.smartsplit.entity.user.valueobject;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pt.saraborges.smartsplit.validator.PasswordValidator;

@Getter
public final class Password {

    private final String hash;

    private Password(String hash){
        this.hash = hash;
    }

    public static Password fromPlainText(String plainText){
        PasswordValidator.validate(plainText);
        String hash = BCrypt.hashpw(plainText, BCrypt.gensalt(12));
        return new Password(hash);
    }

    /**
     * Use when loading an already-hashed password from DB
     * @param hash
     * @return
     */
    public static Password fromHash(String hash){
        return new Password(hash);
    }

    public boolean matches(String plainTextAttempt){
        return BCrypt.checkpw(plainTextAttempt, this.hash);
    }

    @Override
    public String toString() {
        return "Password[PROTECTED]";
    }
}
