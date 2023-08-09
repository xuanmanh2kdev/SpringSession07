package fa.training.eventbox.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncodeTest {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String plainText = "123456";
        String ecryptPassword = passwordEncoder.encode(plainText);
        System.out.println(ecryptPassword);
        // $2a$10$blNtzfYaQqVuPF0OL3AwSe4rwSm5xHmN8P8WmbcqereG8Aow4Zgyi

//        System.out.println(passwordEncoder
//                .matches(plainText, "$2a$10$blNtzfYaQqVuPF0OL3AwSe4rwSm5xHmN8P8WmbcqereG8Aow4Zgyi"));
    }
}
