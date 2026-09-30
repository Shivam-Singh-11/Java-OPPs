import java.util.*;

class Student {
    String name;
    int rollNo;
    int marks;

    Student(String name, int rollNo, int marks) {
        this.name = name;
        this.rollNo = rollNo;
        this.marks = marks;
    }

    public String toString() {
        return name + " " + rollNo + " " + marks;
    }
}

class MyComparator implements Comparator<Student> {

    public int compare(Student s1, Student s2) {
        return s2.name.compareTo(s1.name);
    }
}

public class comparator1 {
    public static void main(String[] args) {

        ArrayList<Student> students = new ArrayList<>();

        students.add(new Student("Rahul", 103, 85));
        students.add(new Student("Aman", 101, 95));
        students.add(new Student("Riya", 104, 85));
        students.add(new Student("Neha", 102, 95));
        students.add(new Student("Vikas", 105, 75));

        Collections.sort(students, new MyComparator());

        for (Student s : students) {
            System.out.println(s);
        }
    }
}