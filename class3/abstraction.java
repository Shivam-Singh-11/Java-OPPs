abstract class Payment {

    abstract void paymentMethod();
}

class UPIPayment extends Payment {

    void paymentMethod() {
        System.out.println("Payment made using UPI");
    }
}

class CardPayment extends Payment {

    void paymentMethod() {
        System.out.println("Payment made using Card");
    }
}

public class abstraction {

    public static void main(String[] args) {

        Payment p1 = new UPIPayment();
        Payment p2 = new CardPayment();

        p1.paymentMethod();
        p2.paymentMethod();
    }
}