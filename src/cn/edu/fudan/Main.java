package cn.edu.fudan;

public class Main {

    public static void change(int [] x) {
        x[0] = 5;
    }


    public static void main(String[] args) {
        int [] x = new int[1];
        x[0] = 1;
        change(x);
        System.out.println(x[0]);
    }
}
