import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            while (!reader.ready()) {
                String inputStr = reader.readLine();
                if(inputStr.equalsIgnoreCase("exit")) break;

                System.out.println(calc(inputStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static String calc (String input) throws Exception {

        // Разбиваем входящую строку на массив
        input = input.toUpperCase();
        //String[] items = input.split("[+\\-*/]");


        StringTokenizer tokenizer = new StringTokenizer(input, "+-*/", true);
        List<String> items = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (!token.isEmpty()) {
                items.add(token);
            }
        }

        if (items.size() > 3) {
            throw new Exception("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        } else if (items.size() < 2) {
            throw new Exception("Строка не является математической операцией или в ней меньше двух чисел");
        }

        int a, b;
        boolean romanNumber = isRoman(input);



        if (romanNumber) {
            try {
                a = convertToDecimal(items.get(0));
                b = convertToDecimal(items.get(2));
            } catch (Exception e) {
                return e.getMessage();
            }
        } else {
            a = (int) Double.parseDouble(items.get(0));
            b = (int) Double.parseDouble(items.get(2));
        }


        // Проверка больше ли полученные числа  10
        if (a > 10 || b > 10) {
            return "Калькулятор должен принимать на вход числа от 1 до 10 включительно, не более.";
        }

        // Через отдельный метод определяем операцию выполнения
        char operator = findOperator(input);

        // Через switch выполняем выражение
        int result = switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> 0;
        };

        // проверка на отрицательное число
        if (romanNumber && result < 0) {
            throw new Exception("В римской системе нет отрицательных чисел");
        } else if (romanNumber) {
            // возврат результата в римском счислении
            return convertToRoman(result);
        }

        return String.valueOf(result);


    }


    public static int convertToDecimal(String inputRoman) throws Exception {
        if(inputRoman == null || inputRoman.length() == 0) {
            return 0;
        }

        // проверяем на то чтобы не залетели арабские числа  (пример 1+III)
        if (!inputRoman.matches("[IVX]+")) throw new Exception("Используются одновременно разные системы счисления");

        Map<Character, Integer> values = new HashMap<>();
        values.put('I', 1);
        values.put('V', 5);
        values.put('X', 10);

        int result = 0;
        int prev = 0;

        for(int i = inputRoman.length() - 1; i >= 0; i--) {
            int value = values.get(inputRoman.charAt(i));

            if(value < prev) {
                result -= value;
            } else {
                result += value;
            }

            prev = value;
        }

        return result;
    }

    public static String convertToRoman(int number) {

        // таблица  римских и арабских чисел
        String[] romanSymbols = {"C","XC","L","XL","X","IX","V","IV","I"};
        int[] arabicValues = {100,90,50,40,10,9,5,4,1};

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < arabicValues.length; i++) {
            // пока число больше или равно текущей арабской цифре
            while (number >= arabicValues[i]) {
                // добавляем соответствующий символ
                result.append(romanSymbols[i]);
                // вычитаем арабскую цифру
                number -= arabicValues[i];
            }
        }

        return result.toString();
    }

    public static char findOperator(String inputString) {
        for (char op : "+-*/".toCharArray()) {
            if (inputString.indexOf(op) >= 0) {
                return op;
            }
        }
        return ' ';
    }
    public static boolean isRoman(String input) {
        return input.matches(".*[IVX].*");
    }
}
