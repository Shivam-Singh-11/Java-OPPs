import java.util.Scanner;
public class AgeValidation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int age = sc.nextInt();

        try {
            ageCheck(age);
        }
        catch (InvalidAgeException e) {
            System.out.println(e.getMessage());
        }
    }

    static void ageCheck(int a) throws InvalidAgeException {

        if (a < 18) {
            throw new InvalidAgeException("Age is Invalid");
        }

        System.out.println("eligible to vote");
    }
}
class InvalidAgeException extends Exception {

    InvalidAgeException(String msg) {
        super(msg);
    }
}