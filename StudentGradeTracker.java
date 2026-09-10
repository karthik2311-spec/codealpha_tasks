import java.util.*;

class Student {
    String name;
    double score;
}

public class StudentGradeTracker {
    public static void main(String[] args) {

        Scanner sc =new Scanner(System.in);
        System.out.println("Student Grade Tracker");

        System.out.print("Enter the number of students: ");
        int n = sc.nextInt();

         if (n <= 0) {
            System.out.println("Number of students must be greater than 0.");
            sc.close();
            return;
        }

        ArrayList<Student> students = new ArrayList<>();

        for (int i = 0; i < n; i++) 
        {
        Student s = new Student();

         System.out.print("Enter student name: ");
         s.name = sc.next();

         System.out.print("Enter student score: ");
         s.score = sc.nextDouble();

         students.add(s);
        }

        double total = 0;

        for(Student s:students)
        {
            total += s.score;
        }

        double avg = total/students.size();

        double highest = students.get(0).score;
        double lowest = students.get(0).score;

        for(Student s:students)
        {
            if(s.score > highest)
            {
                highest = s.score;
            }
            if (s.score < lowest) 
            {
                 lowest = s.score;
            }
        }

         System.out.println("\nStudent Summary Report");

        for (Student s : students) 
        {
            System.out.printf("%s - %.2f%n", s.name, s.score);
        }

        System.out.printf("\nAverage score: %.2f%n", avg);
        System.out.printf("Highest score: %.2f%n", highest);
        System.out.printf("Lowest score: %.2f%n", lowest);

        sc.close();
    }
}
