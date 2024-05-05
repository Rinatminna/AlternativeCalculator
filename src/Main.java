import java.util.Scanner;

public class Main {
    // Список возможных операций
    private static final String[] OPERATORS = {"+", "-", "*", "/"};

    public static void main(String[] args) throws Exception {
        String expression = readInput();
        char action = findOperator(expression);
        String[] operands = splitExpression(expression, action);

        validateOperands(operands, action);

        String result = performOperation(operands, action);

        printInQuotes(result);
    }

    private static String readInput() {
        System.out.println("Введите выражение");
        Scanner scn = new Scanner(System.in);
        return scn.nextLine();
    }

    private static char findOperator(String expression) throws Exception {
        for (String operator : OPERATORS) {
            if (expression.contains(operator)) {
                return operator.charAt(0);
            }
        }
        throw new Exception("Некорректный знак действия");
    }

    private static String[] splitExpression(String expression, char action) {
        String[] operands = expression.split(" \\" + action + " ");
        for (int i = 0; i < operands.length; i++) {
            operands[i] = operands[i].replace("\"", "").trim();
        }
        return operands;
    }

    private static void validateOperands(String[] operands, char action) throws Exception {
        if (operands[0].length() > 10 || operands[1].length() > 10) {
            throw new Exception("Введено больше 10 символов");
        }

        if (action == '*' || action == '/') {
            try {
                int value = Integer.parseInt(operands[1]);
                if (value < 1 || value > 10) { // Проверка диапазона от 1 до 10
                    throw new Exception("Число должно быть от 1 до 10");
                }
            } catch (NumberFormatException e) {
                throw new Exception("Строку можно делить или умножать только на число");
            }
        }
    }

    private static String performOperation(String[] operands, char action) {
        return switch (action) {
            case '+' -> "\"" + operands[0] + operands[1] + "\"";
            case '*' -> {
                int multiplier = Integer.parseInt(operands[1]);
                StringBuilder r = new StringBuilder();
                for (int i = 0; i < multiplier; i++) {
                    r.append(operands[0]);
                }
                yield "\"" + r + "\"";
            }
            case '-' -> {
                int index = operands[0].indexOf(operands[1]);
                String r;
                if (index == -1) {
                    r = operands[0];
                } else {
                    r = operands[0].substring(0, index) + operands[0].substring(index + operands[1].length());
                }
                yield "\"" + r + "\"";
            }
            case '/' -> {
                int divisor = Integer.parseInt(operands[1]);
                int newLen = operands[0].length() / divisor;
                yield "\"" + operands[0].substring(0, newLen) + "\"";
            }
            default -> throw new IllegalArgumentException("Неизвестная операция");
        };
    }

    private static void printInQuotes(String text) {
        if (text.length() > 40) text = text.substring(0, 40) + "...";
        System.out.println(text); // Вывод результата в кавычках
    }
}
