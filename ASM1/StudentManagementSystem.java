package bac;
import java.util.Scanner;
import java.util.Stack;

class Student {
    private String id;
    private String name;
    private int age;

    // Constructor
    public Student(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student [ID: " + id + ", Name: " + name + ", Age: " + age + "]";
    }
}

public class StudentManagementSystem {
    private static Stack<Student> studentStack = new Stack<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Edit Student");
            System.out.println("3. Delete Student");
            System.out.println("4. View All Students");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    editStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    viewAllStudents();
                    break;
                case 5:
                    System.out.println("Exiting the program...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Add a student to the stack
    private static void addStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Student Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Student student = new Student(id, name, age);
        studentStack.push(student);
        System.out.println("Student added successfully!");
    }

    // Edit a student in the stack
    private static void editStudent() {
        if (studentStack.isEmpty()) {
            System.out.println("No students to edit.");
            return;
        }

        System.out.print("Enter Student ID to edit: ");
        String id = scanner.nextLine();

        // Search for the student in the stack
        for (Student student : studentStack) {
            if (student.getId().equals(id)) {
                System.out.print("Enter new Name: ");
                String newName = scanner.nextLine();
                System.out.print("Enter new Age: ");
                int newAge = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                student.setName(newName);
                student.setAge(newAge);
                System.out.println("Student updated successfully!");
                return;
            }
        }

        System.out.println("Student with ID " + id + " not found.");
    }

    // Delete a student from the stack
    private static void deleteStudent() {
        if (studentStack.isEmpty()) {
            System.out.println("No students to delete.");
            return;
        }

        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine();

        // Create a temporary stack to hold students
        Stack<Student> tempStack = new Stack<>();

        // Search for the student in the stack
        boolean found = false;
        while (!studentStack.isEmpty()) {
            Student student = studentStack.pop();
            if (student.getId().equals(id)) {
                found = true;
                System.out.println("Student deleted: " + student);
                break;
            }
            tempStack.push(student);
        }

        // Restore the remaining students to the original stack
        while (!tempStack.isEmpty()) {
            studentStack.push(tempStack.pop());
        }

        if (!found) {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    // View all students in the stack
    private static void viewAllStudents() {
        if (studentStack.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        System.out.println("List of Students:");
        for (Student student : studentStack) {
            System.out.println(student);
        }
    }
}

