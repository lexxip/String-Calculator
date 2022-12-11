import java.util.Scanner;
import java.lang.Exception;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class IncorrectExpressionException extends Exception {
    public IncorrectExpressionException (String message) {
        super(message);
    }
}
class Split {

    String str;
    public String[] split(String str) throws IncorrectExpressionException {
        this.str = str;
        String [] arrStr = str.split("[/*+-]");
        if ((arrStr.length != 2) || (arrStr[0] == "")) {
            throw new IncorrectExpressionException("Введено некорректное выражение");
        }
        String delimiter = String.valueOf(str.charAt(arrStr[0].length()));

        return new String[] {arrStr[0].trim(), arrStr[1].trim(), delimiter};
    }
}

class Calculation {
    String op1, op2, operator;
    public String calculation(String[] str) throws IncorrectExpressionException {
        this.op1 = str[0];
        this.op2 = str[1];
        this.operator = str[2];
        if (!(op1.startsWith("\"") && op1.endsWith("\""))) {
            throw new IncorrectExpressionException("Первый операнд обязательно должен быть заключен в двойные кавычки (\")");
        } else if ("+-".contains(operator) && !(op2.startsWith("\"") && op2.endsWith("\""))) {
            throw new IncorrectExpressionException("При операциях \"+\" и \"-\" второй операнд обязательно должен быть заключен в двойные кавычки (\")");
        }
        int digit = 0;
        if ("/*".contains(operator)) {
            try {
                digit = Integer.parseInt(op2);
            } catch (NumberFormatException e) {
                throw new IncorrectExpressionException("При операциях \"/\" и \"*\" второй операнд должен быть целым числом без двойных кавычек");
            }
            if ((digit < 1) || (digit > 10)) {
                throw new IncorrectExpressionException("Значение второго операнда должно быть в диапазоне от 1 до 10 включительно");
            }
        }
        String result = "";
        String substr = "";
        switch (operator) {
            case "+" :
                result = op1.substring(0, op1.length()-1) + op2.substring(1);
                break;
            case "-" :
                substr = op2.substring(1, op2.length()-1);
                Pattern p = Pattern.compile(substr);
                Matcher m = p.matcher(op1);
                result = m.replaceAll("");
                break;
            case "/" :
                result = op1.substring(0, (op1.length()-2)/digit+1) + "\"";
                break;
            case "*" :
                substr = op1.substring(1, op1.length()-1);
                for (int i=0; i < digit; i++) {
                    result = result + substr;
                }
                result = "\"" + result + "\"";
        }
        return result;
    }
}

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите строковое выражение");
        System.out.println("Первый операнд является срокой и должен быть обязательно заключен в двойные кавычки (\")");
        System.out.println("Если операция \"+\" или \"-\", второй операнд тоже является строкой и должен быть обязательно заключен в двойные кавычки (\")");
        System.out.println("Если операция \"*\" или \"/\", второй операнд должен быть целым числом в диапазоне от 1 до 10 включительно, без кавычек");
        String str = in.nextLine();
        Split Split = new Split();
        try {
            String [] expression = Split.split(str);
            Calculation Calculation = new Calculation();
            String result = Calculation.calculation(expression);
            System.out.println(result);
        } catch (IncorrectExpressionException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
    }
}