package rut.imdt;

public class Feature {
    public static void main(String input, int n, int m) {
        if (input.equals("") && !(m == 1 && n == 1)) {
            System.out.println("\nP.s. Можно было бы потрудиться и написать хотя бы одну букву в сообщение!");
        }
        if (m == 1 && n == 1 && !input.equals("")) {
            System.out.println("\nP.s. И какой смысл в такой кодировке? ...");
        }
        if (input.equals("") && m == 1 && n == 1) {
            System.out.println("\nP.s. Можно было бы потрудиться и написать хотя бы одну букву в сообщение!" +
                    "\n Хотя куда там... С такими размерами таблицы можно было вообще не запускать программу");
        }
    }
}
