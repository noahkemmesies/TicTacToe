import javax.swing.*;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private JPanel mainPanel;
    private JLabel turn;
    private JLabel score;
    private JButton r1c1;
    private JButton r1c2;
    private JButton r1c3;
    private JButton r2c1;
    private JButton r2c2;
    private JButton r2c3;
    private JButton r3c1;
    private JButton r3c2;
    private JButton r3c3;
    private JButton restart;
    private JButton infoAdvanced;
    private JCheckBox advanced;

    private final char[][] board;
    private final int[] firstMove;
    private char currentPlayer = 'X';
    private boolean gameEnd = false;
    private int scoreX = 0, scoreO = 0, moveCount = 0;

    public TicTacToe() {
        setContentPane(mainPanel);
        setTitle("TicTacToe von Noah");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        board = new char[3][3];
        firstMove = new int[2];

        createButtons();
        clearBoard();
    }

    public void createButtons() {
        ActionListener listenerR1c1 = e -> play(1, 1);
        ActionListener listenerR1c2 = e -> play(1, 2);
        ActionListener listenerR1c3 = e -> play(1, 3);
        ActionListener listenerR2c1 = e -> play(2, 1);
        ActionListener listenerR2c2 = e -> play(2, 2);
        ActionListener listenerR2c3 = e -> play(2, 3);
        ActionListener listenerR3c1 = e -> play(3, 1);
        ActionListener listenerR3c2 = e -> play(3, 2);
        ActionListener listenerR3c3 = e -> play(3, 3);

        r1c1.addActionListener(listenerR1c1);
        r1c2.addActionListener(listenerR1c2);
        r1c3.addActionListener(listenerR1c3);
        r2c1.addActionListener(listenerR2c1);
        r2c2.addActionListener(listenerR2c2);
        r2c3.addActionListener(listenerR2c3);
        r3c1.addActionListener(listenerR3c1);
        r3c2.addActionListener(listenerR3c2);
        r3c3.addActionListener(listenerR3c3);

        restart.addActionListener(e -> restart());
        infoAdvanced.addActionListener(e -> JOptionPane.showMessageDialog(mainPanel, "Advanced mode:\n- After 3 obtained spots your first one is erased\n- A draw is not possible"));

        advanced.addActionListener(e -> {
            if (moveCount != 0) {
                advanced.setSelected(!advanced.isSelected());
                int choice = JOptionPane.showConfirmDialog(mainPanel, "Advanced mode can only be changed when starting a new game\nDo you want to restart the game?");
                if (choice == 0) {
                    advanced.setSelected(!advanced.isSelected());
                    restart();
                }
            }
        });
    }

    public void play(int r, int c) {
        if (!gameEnd) {
            r = r - 1;
            c = c - 1;
            if (updateBoard(r, c)) {
                if (checkWinner()) {
                    endScreen();
                }
            } else {
                turn.setText("Player: " + currentPlayer + " that field is already used!");
            }
        }
    }

    public boolean updateBoard(int r, int c) {
        if (board[r][c] != 'X' && board[r][c] != 'O') {
            board[r][c] = currentPlayer;
            updateButton(r+1, c+1, ""+currentPlayer);
            if (currentPlayer == 'X') {
                currentPlayer = 'O';
            } else {
                currentPlayer = 'X';
            }

            moveCount++;
            if (moveCount == 1) {
                firstMove[0] = r;
                firstMove[1] = c;
            } else {
                if (moveCount >= 6) {
                    updateButton(firstMove[0]+1, firstMove[1]+1, "("+board[firstMove[0]][firstMove[1]]+")");
                    int[] toBeDeleted = firstMove.clone();
                    firstMove[0] = r;
                    firstMove[1] = c;
                    if (moveCount > 6) {
                        updateButton(toBeDeleted[0]+1, toBeDeleted[1]+1, " ");
                    }
                }
            }

            turn.setText("Player: " + currentPlayer);
            return true;
        }
        return false;
    }

    public void updateButton(int r, int c, String text) {
        if (r == 1 && c == 1) {
            r1c1.setText(text);
        }
        if (r == 1 && c == 2) {
            r1c2.setText(text);
        }
        if (r == 1 && c == 3) {
            r1c3.setText(text);
        }
        if (r == 2 && c == 1) {
            r2c1.setText(text);
        }
        if (r == 2 && c == 2) {
            r2c2.setText(text);
        }
        if (r == 2 && c == 3) {
            r2c3.setText(text);
        }
        if (r == 3 && c == 1) {
            r3c1.setText(text);
        }
        if (r == 3 && c == 2) {
            r3c2.setText(text);
        }
        if (r == 3 && c == 3) {
            r3c3.setText(text);
        }
    }

    public boolean checkWinner() {
        boolean check = equal3(board[0][0], board[1][1], board[2][2]) || equal3(board[2][0], board[1][1], board[0][2]);

        for (int i = 0; i < board.length; i++) {
            if (equal3(board[i][0], board[i][1], board[i][2]) || equal3(board[0][i], board[1][i], board[2][i])) {
                check = true;
            }
        }

        if (!check) {
            boolean full = true;
            for (char[] chars : board) {
                for (int j = 0; j < board.length; j++) {
                    if (!(chars[j] == 'X' || chars[j] == 'O')) {
                        full = false;
                        break;
                    }
                }
            }
            if (full) {
                board[0][0] = 'D';
                check = true;
            }
        }

        return check;
    }

    public boolean equal3(char c1, char c2, char c3) {
        return (c1 == 'X' || c1 == 'O') && c1 == c2 && c2 == c3;
    }

    public void endScreen() {
        if (board[0][0] == 'D') {
            turn.setText("Draw!!!");
        } else {
            char winner = '?';
            if (currentPlayer == 'X') {
                winner = 'O';
                scoreO += 1;
            } else if (currentPlayer == 'O') {
                winner = 'X';
                scoreX += 1;
            }
            turn.setText("Player: " + winner + " is the Winner!!! It took " + moveCount + " moves");

            score.setText("X: " + scoreX + ", O: " + scoreO);
        }

        gameEnd = true;
    }

    public void restart() {
        clearBoard();

        turn.setText("Player: " + currentPlayer);
        moveCount = 0;
        gameEnd = false;
    }

    public void clearBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = '?';
                updateButton(i+1, j+1, " ");
            }
        }
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
