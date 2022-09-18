import java.util.Scanner;

public class Main {

    enum Roman
    {
        I(1), II(2), III(3), IV(4), V(5), VI(6), VII(7), VIII(8), IX(9), X(10);
        private int arabic;
        Roman(int arabic) {
            this.arabic = arabic;
        }
        public int getArabic() {
            return arabic;
        }
    }

    static Roman getRoman(String s)
    {
        Roman value = null;
        try {
            value  = Roman.valueOf(s);
        } catch (IllegalArgumentException e) {
            // не римское в пределах 10
        }
        return value;
    }

    static String getRoman(int number) {
        return "I".repeat(number)
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите арифметическое выражение с двумя числами:" );
            String input = scanner.nextLine();
            try {
                String result = calc(input);
                System.out.println(result);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка ввода - " + e.getMessage());
                throw e;
            }
        }
    }

    public static String calc(String input) {

        char[] chars = input.toCharArray();
        int operatorPosition = -1;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ('+' == c || '-' == c || '*' == c || '/' == c) {
                if (operatorPosition > -1) throw new IllegalArgumentException("Более одной операции недопустимо, уже есть " + input.charAt(operatorPosition));
                operatorPosition = i;
            }
        }
        if (operatorPosition < 1) throw new IllegalArgumentException("Не задано ни одной операции");

        String sFirstOperand  = input.substring(0, operatorPosition).trim();
        String sSecondOperand = input.substring(operatorPosition + 1).trim();

        Roman rFirstOperand  = getRoman(sFirstOperand);
        Roman rSecondOperand = getRoman(sSecondOperand);
        if ((rFirstOperand != null && rSecondOperand == null) || ((rFirstOperand == null && rSecondOperand != null))) {
            String roman, nonRoman;
            if (rFirstOperand != null) {
                roman = sFirstOperand; nonRoman = sSecondOperand;
            } else {
                roman = sSecondOperand; nonRoman = sFirstOperand;
            }
            throw new IllegalArgumentException("Оба числа должны быть либо римскими либо арабскими и не более 10 - "
                    + roman + " римское в пределах, но " + nonRoman + " нет");
        }
        int iFirstOperand, iSecondOperand;

        boolean isRoman = rFirstOperand != null;
        if (isRoman) {
            iFirstOperand  = rFirstOperand.getArabic();
            iSecondOperand = rSecondOperand.getArabic();
        } else {
            try {
                iFirstOperand  = Integer.parseInt(sFirstOperand);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Первый '" + sFirstOperand + "' не является цислом");
            }
            try {
                iSecondOperand = Integer.parseInt(sSecondOperand);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Второй '" + sSecondOperand + "' не является цислом");
            }
        }
        if (iFirstOperand > 10)  throw new IllegalArgumentException("Первый " + iFirstOperand + " > 10");
        if (iSecondOperand > 10) throw new IllegalArgumentException("Второй " + iSecondOperand + " > 10");

        int result;
        char operator = input.charAt(operatorPosition);
        switch (operator) {
            case '+': result = iFirstOperand + iSecondOperand; break;
            case '-': result = iFirstOperand - iSecondOperand; break;
            case '*': result = iFirstOperand * iSecondOperand; break;
            case '/': result = iFirstOperand / iSecondOperand; break;
            default: throw new IllegalArgumentException("Неизвестная операция"); //недостижима
        }
        if(isRoman) {
            if (result < 1) {
                throw new IllegalArgumentException("Результатом с римскими числами может быть только положительное число");
            }
            return getRoman(result);
        }
        else {
            return String.valueOf(result);
        }
    }
}