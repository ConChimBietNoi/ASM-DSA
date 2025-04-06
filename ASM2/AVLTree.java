package Java;

import javax.swing.DefaultListModel;
import java.util.List;

class AVLNode {
    Student student;
    AVLNode left, right;
    int height;

    public AVLNode(Student student) {
        this.student = student;
        height = 1;
    }
}

class AVLTree {
    class AVLNode {
        Student student;
        AVLNode left, right;
        int height;

        public AVLNode(Student student) {
            this.student = student;
            this.height = 1;
            left = right = null;
        }
    }

    AVLNode root;

    public AVLTree() {
        root = null;
    }

    private int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Method to search students by name
    public void searchByName(AVLNode node, String name, DefaultListModel<String> listModel) {
        if (node == null) {
            return;
        }
        searchByName(node.left, name, listModel);
        if (node.student.getName().toLowerCase().contains(name.toLowerCase())) {
            listModel.addElement(node.student.toString());
        }
        searchByName(node.right, name, listModel);
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    public AVLNode insert(AVLNode node, Student student) {
        if (node == null) 
            return new AVLNode(student);

        if (student.getScore() < node.student.getScore())
            node.left = insert(node.left, student);
        else if (student.getScore() > node.student.getScore())
            node.right = insert(node.right, student);
        else {
            // Nếu điểm số bằng nhau, kiểm tra thêm các thuộc tính khác
            if (!student.getName().equals(node.student.getName()) || student.getId() != node.student.getId()) {
                node.right = insert(node.right, student); // Thêm vào nhánh phải nếu khác
            } else {
                return node; // Không thêm nếu hoàn toàn trùng lặp
            }
        }

        node.height = Math.max(height(node.left), height(node.right)) + 1;

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && student.getScore() < node.left.student.getScore())
            return rotateRight(node);

        // Right Right Case
        if (balance < -1 && student.getScore() > node.right.student.getScore())
            return rotateLeft(node);

        // Left Right Case
        if (balance > 1 && student.getScore() > node.left.student.getScore()) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Left Case
        if (balance < -1 && student.getScore() < node.right.student.getScore()) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public void insert(Student student) {
        root = insert(root, student);
    }

    public void inorder(AVLNode node, DefaultListModel<String> model) {
        if (node != null) {
            inorder(node.left, model);
            model.addElement(node.student.toString());
            inorder(node.right, model);
        }
    }

    public void inorderToList(AVLNode node, List<Student> students) {
        if (node != null) {
            inorderToList(node.left, students);
            students.add(node.student); // Add student to the list
            inorderToList(node.right, students);
        }
    }

    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public void delete(double score) {
        root = delete(root, score);
    }

    public AVLNode delete(AVLNode root, double score) {
        if (root == null) 
            return root;

        if (score < root.student.getScore())
            root.left = delete(root.left, score);
        else if (score > root.student.getScore())
            root.right = delete(root.right, score);
        else {
            if ((root.left == null) || (root.right == null)) {
                AVLNode temp = (root.left != null) ? root.left : root.right;
                if (temp == null)
                    root = null;
                else
                    root = temp;
            } else {
                AVLNode temp = minValueNode(root.right);
                root.student = temp.student;
                root.right = delete(root.right, temp.student.getScore());
            }
        }

        if (root == null)
            return root;

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        int balance = getBalance(root);

        // Balancing cases
        if (balance > 1 && getBalance(root.left) >= 0)
            return rotateRight(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0)
            return rotateLeft(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }

        return root;
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

            Student student = new Student(id, name, score);
            tree.insert(student);
            updateList();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateList() {
        listModel.clear();
        tree.inorder(tree.root, listModel); // Hiển thị dữ liệu theo thứ tự tăng dần
    }

    public boolean containsId(AVLNode root2, int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'containsId'");
    }
}

