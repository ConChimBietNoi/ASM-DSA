package Java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentManager extends JFrame {
    private AVLTree tree;
    private DefaultListModel<String> listModel;
    private JList<String> studentList;
    private JTextField idField, nameField, scoreField;
    
    public StudentManager() {
        tree = new AVLTree();
        listModel = new DefaultListModel<>();
        studentList = new JList<>(listModel);

        setTitle("Student Management - AVL Tree");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2)); // Adjusted to fit the new button
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Score:"));
        scoreField = new JTextField();
        inputPanel.add(scoreField);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton sortButton = new JButton("Sort");
        JButton searchButton = new JButton("Search"); // New search button

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editStudent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateList();
            }
        });

        searchButton.addActionListener(new ActionListener() { // Action for search button
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });

        inputPanel.add(addButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);
        inputPanel.add(sortButton);
        inputPanel.add(searchButton); // Add search button to the panel

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(studentList), BorderLayout.CENTER);
    }

    private void addStudent() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText().trim();
            double score = Double.parseDouble(scoreField.getText());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra trùng ID
            if (tree.containsId(tree.root, id)) {
                JOptionPane.showMessageDialog(this, "Student ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = new Student(id, name, score); // Rank được tính tự động
            tree.insert(student);

            updateList();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editStudent() {
        int selectedIndex = studentList.getSelectedIndex();
        if (selectedIndex != -1) {
            try {
                String selectedValue = listModel.getElementAt(selectedIndex);
                double oldScore = Double.parseDouble(selectedValue.split("-")[2].trim().split(" ")[0]);

                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText().trim();
                double newScore = Double.parseDouble(scoreField.getText());

                tree.delete(oldScore);
                tree.insert(new Student(id, name, newScore)); // Rank được tính tự động

                updateList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error editing student!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a student to edit!", "Notification", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteStudent() {
        int selectedIndex = studentList.getSelectedIndex();
        if (selectedIndex != -1) {
            try {
                String selectedValue = listModel.getElementAt(selectedIndex);
                double score = Double.parseDouble(selectedValue.split("-")[2].trim().split(" ")[0]);

                tree.delete(score);
                updateList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting student!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a student to delete!", "Notification", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateList() {
        listModel.clear();
        tree.inorder(tree.root, listModel); // Hiển thị dữ liệu theo thứ tự tăng dần
    }

    private void searchStudent() { // New search method
        String searchTerm = JOptionPane.showInputDialog(this, "Enter student name to search:");
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            searchTerm = searchTerm.trim();
            java.util.List<Student> students = treeToSortedList(); // Get sorted list from AVL tree

            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No students in the list to search.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int index = binarySearch(students, searchTerm); // Perform binary search
            listModel.clear();

            if (index != -1) {
                // Add the found student to the list
                listModel.addElement(students.get(index).toString());

                // Check for duplicates (students with the same name)
                int left = index - 1;
                int right = index + 1;

                while (left >= 0 && students.get(left).getName().equalsIgnoreCase(searchTerm)) {
                    listModel.addElement(students.get(left).toString());
                    left--;
                }

                while (right < students.size() && students.get(right).getName().equalsIgnoreCase(searchTerm)) {
                    listModel.addElement(students.get(right).toString());
                    right++;
                }
            } else {
                JOptionPane.showMessageDialog(this, "No students found with the given name.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                updateList(); // Restore the full list if no results are found
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private java.util.List<Student> treeToSortedList() {
        java.util.List<Student> students = new java.util.ArrayList<>();
        tree.inorderToList(tree.root, students); // Collect students in sorted order
        return students;
    }

    private int binarySearch(java.util.List<Student> students, String searchTerm) {
        int left = 0;
        int right = students.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = students.get(mid).getName().compareToIgnoreCase(searchTerm);

            if (comparison == 0) {
                return mid; // Found the student
            } else if (comparison < 0) {
                left = mid + 1; // Search in the right half
            } else {
                right = mid - 1; // Search in the left half
            }
        }

        return -1; // Student not found
    }

    private String getRank(double score) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManager().setVisible(true));
    }

public boolean containsId(AVLNode node, int id) {
    if (node == null) {
        return false;
    }
    if (node.student.getId() == id) {
        return true;
    }
    return containsId(node.left, id) || containsId(node.right, id);
}
}