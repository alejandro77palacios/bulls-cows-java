package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Input the length of the secret code:");
            int length = scanner.nextInt();
            try {
                System.out.println("Input the number of possible symbols in the code:");
                int different = scanner.nextInt();
                try {
                    Game game = new Game(length, different);
                    System.out.println("Okay, let's start a game!");
                    int turn = 1;
                    do {
                        System.out.println("Turn " + turn + ":");
                        String guess = scanner.next();
                        game.grade(guess);
                        turn++;
                    } while (!game.finished);
                    System.out.println("Congratulations! You guessed the secret code.");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error");
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Error");
        }
    }

}


class Game {

    protected String secret;
    protected int maxLength = 36;
    protected String allCharacters = "0123456789abcdefghijklmnopqrstuvwxyz";
    protected int cows = 0;
    protected int bulls = 0;
    Random randomGenerator = new Random(0);
    protected boolean finished = false;

    public Game(int lengthSecret, int possibleChars) {
        this.secret = generateSecret(lengthSecret, possibleChars);
    }


    public void grade(String guess) {
        countCowsBulls(guess);
        resultMessage();
        if (bulls == secret.length()) {
            finished = true;
            return;
        }
        resetCount();
    }

    private void resetCount() {
        this.cows = 0;
        this.bulls = 0;
    }

    private void resultMessage() {
        String result;
        if (cows > 0) {
            if (bulls > 0) {
                result = "%d bull(s) and %d cow(s)".formatted(bulls, cows);
            } else {
                result = "%d cow(s)".formatted(cows);
            }
        } else if (bulls > 0) {
            result = "%d bulls(s)".formatted(bulls);
        } else {
            result = "None";
        }
        System.out.println("Grade: " + result);
    }

    private void countCowsBulls(String guess) {
        for (int i = 0; i < guess.length(); i++) {
            char number = guess.charAt(i);
            if (isCow(number)) {
                if (isBull(number, i)) {
                    bulls++;
                } else {
                    cows++;
                }
            }
        }
    }

    private boolean isCow(char number) {
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == number) {
                return true;
            }
        }
        return false;
    }

    private boolean isBull(char number, int position) {
        return number == secret.charAt(position);
    }

    private String generateSecret(int lengthSecret, int numPossibleChars) {
        if (lengthSecret > maxLength || lengthSecret < 1) {
            throw new IllegalArgumentException("Error: can't generate a secret number with a lengthSecret of 11 because there aren't enough unique digits.");
        } else if (numPossibleChars < lengthSecret || numPossibleChars > 36) {
            throw new IllegalArgumentException("Error: impossible");
        }
        StringBuilder secret = new StringBuilder();
//        int[][] index = indexAvailableChars(differentChars);
        while (secret.length() < lengthSecret) {
            int randomInt = randomGenerator.nextInt(numPossibleChars);
            char randomChar = allCharacters.charAt(randomInt);
            secret.append(randomChar);
        }

        String clue = "*".repeat(lengthSecret);
        StringBuilder range = new StringBuilder("(0-");
        if (numPossibleChars <= 10) {
            range.append(allCharacters.charAt(numPossibleChars - 1));
        } else {
            range.append("9, ");
            if (numPossibleChars == 11) {
                range.append("a");
            } else {
                range.append("a-").append(allCharacters.charAt(numPossibleChars - 1));
            }
        }
        range.append(").");
        System.out.println("The secret is prepared " + clue + " " + range);
        return secret.toString();

    }


    private static boolean isRegistered(int num, int[] nums) {
        for (int j : nums) {
            if (num == j) {
                return true;
            }
        }
        return false;
    }

    private static int getMinimum(int[] numbers) {
        int min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        return min;
    }

    private static int getMaximum(int[] numbers) {
        int max = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        return max;
    }

    private static char convertToLetter(int num) {
        return (char) (num + 'a');
    }

    private static void fillNextEmpty(int[] numbers, int num) {
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == -1) {
                numbers[i] = num;
                break;
            }
        }
    }
}
