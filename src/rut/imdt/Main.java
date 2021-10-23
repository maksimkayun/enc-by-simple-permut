package rut.imdt;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static List<String[][]> tabletList = new ArrayList<>(); // лист для хранения таблиц с закодированным
    // сообщением
    private static List<Integer> key = new ArrayList<>(); // для записи ключа шифрования по таблице
    private static List<Integer> support = new ArrayList<>(); // для хранения чисел-количеств букв в столбцах

    public static void createTables(String message, int n, int m) {
        int length = message.length();
        int quantity = length / (n * m);
        if (length % (n * m) != 0) {
            quantity++;
        }
        for (int i = 0; i < quantity; i++) {
            tabletList.add(new String[n][m]);
        }
    }

    public static void setSupport(String message, int n, int m) {
        for (int k = 0; k < tabletList.size(); k++) {
            if (k != tabletList.size() - 1 && tabletList.size() > 1) {
                for (int i = 0; i < m; i++) {
                    support.add(n);
                }
            }
            else {
                int quantity = (n * m) - (message.length() -
                        (tabletList.size() - 1)*(n * m )); // количество оставшихся пустых ячеек
                String[][] buffer = new String[n][m];
                for (int i = n - 1; i > -1 && quantity > 0; i--) {
                    for (int j = m - 1; j > -1 && quantity > 0; j--) {
                        if (buffer[i][j] == null) {
                            buffer[i][j] = "";
                            quantity--;
                        }
                    }
                }
                for (int i = 0; i < m; i++) {
                    int counter = 0;
                    for (int j = 0; j < n; j++) {
                        if (buffer[j][i] == null) {
                            counter++;
                        }
                    }
                    support.add(counter);
                }
                tabletList.set(tabletList.size() - 1,  buffer);
            }
        }
    }

    public static String enc(String input, int m, int n) // функция кодирования сообщения
    {
        String output = ""; // переменная для возврата закодированного сообщения, пока что пустая строка

        int size = input.length();
        int counter = 0;
        while (size > 0) {
            String[][] table = new String[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    table[i][j] = String.valueOf(input.charAt(counter));
                    counter++;
                    size--;
                    if (size == 0)
                        break;
                }
                if (size == 0)
                    break;
            }
            tabletList.add(table);
        }
        for (int p = 0; p < tabletList.size(); p++) {
            for (int i = 0; i < m; i++) {
                int j = findCol(i + 1);
                for (int k = 0; k < n; k++) {
                    if (tabletList.get(p)[k][j] == null)
                        break;
                    output += tabletList.get(p)[k][j];

                }
            }
        }
        return output;
    }

    public static String dec(String encMessage, int m, int n) {
        String output = "";
        createTables(encMessage, n, m);
        setSupport(encMessage, n, m);

        for (int i = 0; i < tabletList.size(); i++) {
            for (int j = 0,number=1; number <= m; j++) { // number - это номер столбца, в который мы сейчас
                // будем записывать сообщение, его мы ищем в ключе и детектим, на какой он позиции
                if (key.get(j) == number) {

                    String[] column = new String[support.get(j + i * m)];
                    for (int k = 0; k < support.get(j + i * m); k++) {
                        column[k] = String.valueOf(encMessage.charAt(k));
                    }
                    encMessage = encMessage.substring(support.get(j + i * m));

                    for (int k = 0, s = 0; k < n && s < column.length; k++) {
                        if (tabletList.get(i)[k][j] == null) {
                            tabletList.get(i)[k][j] = column[s];
                            s++;
                        }
                    }
                    number++;
                }
                if(j==m-1){
                    j=-1;
                }
            }
        }
        for (int i = 0; i < tabletList.size(); i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    if (tabletList.get(i)[j][k].equals("")) {
                        return output;
                    }
                    if (tabletList.get(i)[j][k].equals(String.valueOf((char) 142))) {
                        continue;
                    }
                    output += tabletList.get(i)[j][k];
                }
            }
        }
        return output;
    }

    public static int findCol(int index) { // Функция возвращает № столбца, который нужно записывать в сообщение
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) == index)
                return i;
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.print("Введите количество строк таблицы: ");
        try {
            int n = Integer.parseInt(sc.nextLine());
            if (n < 1) {
                throw new NumberFormatException("Количество строк должно быть > 0!");
            }
            System.out.print("Введите количество столбцов таблицы: ");
            int m = Integer.parseInt(sc.nextLine());
            if (m < 1) {
                throw new NumberFormatException("Количество столбцов должно быть > 0!");
            }

            System.out.printf("Введите цифровой ключ через пробел для таблицы (количество = %d): ", m);
            String[] inputkey = sc.nextLine().split(" ");
            if (inputkey.length != m) {
                throw new NumberFormatException(String.format("Количество элементов ключа должно быть равно " + m));
            }
            for (int i = 0; i < m; i++) {
                if (Integer.parseInt(inputkey[i]) < 1 || Integer.parseInt(inputkey[i]) > m)
                    throw new NumberFormatException(String.format("Допустимые символы для ключа от %d до " +
                            "%d включительно", 1, m));
                key.add(Integer.parseInt(inputkey[i]));
            }
            for (int i = 0; i < m - 1; i++) {
                for (int j = i + 1; j < m; j++) {
                    if (Integer.parseInt(inputkey[i]) == Integer.parseInt(inputkey[j])) {
                        throw new NumberFormatException(String.format("Элементы ключа должны пробегать все " +
                                "значения от 1 до %d включительно", m));
                    }
                }
            }

            System.out.print("Введите сообщение для шифрования: ");
            String input = sc.nextLine();

            String outenc = enc(input, m, n);

            System.out.printf("Закодированное сообщение: %s", outenc);

            tabletList.clear(); // очистка таблиц

            String outdec = dec(outenc, m, n);
            System.out.printf("\nРаскодированное сообщение: %s", outdec);
            Feature.main(input, n, m);
        }
        catch (NumberFormatException e) {
            System.out.println("Ошибка! " + e.getMessage());
            System.out.println("Перезапустите программу и попытайтесь снова");
        }
    }
}
