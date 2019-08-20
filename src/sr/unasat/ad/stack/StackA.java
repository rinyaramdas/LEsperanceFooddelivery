package sr.unasat.ad.stack;

public class StackA {
    private final int SIZE = 30;
    private int[] st;
    private int top;

    public StackA() {
        st = new int[SIZE]; //array aanmaken
        top = -1;
    }

    public void push(int j) { //put item into stack
        st[++top] = j;
    }

    public int pop() { //item removen
        return st[top--];
    }

    public int peek() {
        return st[top];
    }

    public boolean isEmpty() { //true als er niets is op die stack
        return (top == -1);
    }
}
