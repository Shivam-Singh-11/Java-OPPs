class Calculator {

    int add(int a, int b) {
        return a + b;
    }
}

public class add {

    public static void main(String[] args) {

        Calculator c = new Calculator();

        int result = c.add(10, 20);

        System.out.println("Sum = " + result);
    }
}