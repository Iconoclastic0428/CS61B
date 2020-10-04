package byow.lab12;

public class Hex {
    private int length;
    private String[][] hex;
    private int frame;
    private int Void;
    private boolean flag = false;

    public Hex(int len) {
        this.length = len;
        frame = length + 2 * (length - 1);
        hex = new String[frame - length + 2][frame];
        Void = length - 1;
    }

    public void createHex() {
        createHelp(Void, length, 0);
    }

    private void createHelp(int Void, int len, int height) {
        for (int i = 0; i < Void; i++) {
            hex[height][i] = " ";
        }
        for (int i = Void; i < len + Void; i++) {
            hex[height][i] = "a";
        }
        for (int i = len + Void; i < frame; i++) {
            hex[height][i] = " ";
        }
        if (len == frame && !flag) {
            flag = true;
            createHelp(Void, len, height + 1);
            return;
        }
        if (flag && len == length) {
            return;
        }
        if (flag) {
            createHelp(Void + 1, len - 2, height + 1);
        } else {
            createHelp(Void - 1, len + 2, height + 1);
        }
    }

    public void printHex() {
        for (int i = 0; i < frame - (length - 2); i++) {
            for (int j = 0; j < frame; j++) {
                System.out.print(hex[i][j]);
                if (j == frame - 1) {
                    System.out.println();
                }
            }
        }
    }

    public static void main(String[] args) {
        Hex h = new Hex(3);
        h.createHex();
        h.printHex();
    }
}
