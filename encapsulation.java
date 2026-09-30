class people {

    private int age;

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}

public class encapsulation {

    public static void main(String[] args) {

        people s = new people();

        s.setAge(20);

        System.out.println("Age = " + s.getAge());
    }
}