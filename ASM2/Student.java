package Java;

public class Student {
    private int id;
    private String name;
    private double score;
    private String rank;

    // Constructor
    public Student(int id, String name, double score) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.rank = calculateRank(score); // Tự động tính Rank khi khởi tạo
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Getter for Name
    public String getName() {
        return name;
    }

    // Getter for Score
    public double getScore() {
        return score;
    }

    // Getter for Rank
    public String getRank() {
        return rank;
    }

    // Method to calculate rank based on score
    private String calculateRank(double score) {
        if (score < 5.0) {
            return "Fail";
        } else if (score < 6.5) {
            return "Medium";
        } else if (score < 7.5) {
            return "Good";
        } else if (score < 9.0) {
            return "Very Good";
        } else {
            return "Excellent";
        }
    }

    // Override toString to include Rank
    @Override
    public String toString() {
        return id + " - " + name + " - " + score + " (" + rank + ")";
    }
}