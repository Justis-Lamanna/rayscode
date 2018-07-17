package lucbui.rayscode.token;

import java.math.BigInteger;
import java.util.Deque;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * An enum holding the RayscodeFunction operators.
 *
 * Implemented as an enum so there's only ever one instance of each.
 */
public enum Rayscode implements RayscodeFunction {
    /**
     * A literal two, which places a 2 on the stack.
     */
    TWO() {
        //Cache result so we don't invoke valueOf each time.
        BigInteger two = BigInteger.valueOf(2);

        @Override
        public void execute(Deque<BigInteger> stack) {
            stack.push(two);
        }
    },
    /**
     * A literal three, which places a 3 on the stack.
     */
    THREE() {
        //Cache result so we don't invoke valueOf each time.
        BigInteger three = BigInteger.valueOf(3);

        @Override
        public void execute(Deque<BigInteger> stack) {
            stack.push(three);
        }
    },
    /**
     * A literal infinity, which is also treated as null.
     */
    INFINITY() {
        @Override
        public void execute(Deque<BigInteger> stack) {
            stack.push(null);
        }
    },
    /**
     * An add operator, which pops twice and pushes their sum
     */
    ADD() {
        @Override
        public void execute(Deque<BigInteger> stack) {
            stack.push(stack.pop().add(stack.pop()));
        }
    },
    /**
     * A subtraction operator, which pops twice and pushes their difference
     */
    SUBTRACT() {
        @Override
        public void execute(Deque<BigInteger> stack) {
            stack.push(stack.pop().subtract(stack.pop()));
        }
    },
    /**
     * A multiplication operator, which pops twice and pushes their product
     */
    MULTIPLY() {
        @Override
        public void execute(Deque<BigInteger> stack) {
            stack.push(stack.pop().multiply(stack.pop()));
        }
    },
    /**
     * A division operator, which pops twice and pushes their quotient
     */
    DIVIDE() {
        @Override
        public void execute(Deque<BigInteger> stack) {
            stack.push(stack.pop().divide(stack.pop()));
        }
    },
    /**
     * An input operator, which pops once (the number of chars to read), and pushes that number of chars from STD IN.
     */
    INPUT() {
        @Override
        public void execute(Deque<BigInteger> stack) {
            BigInteger numberOfCharactersToRead = stack.pop();

            Scanner inputScanner = new Scanner(System.in);
            String s = inputScanner.nextLine();
            inputScanner.close();

            Stream.iterate(0, i -> i + 1)
                    .limit(numberOfCharactersToRead == null ? s.length() : numberOfCharactersToRead.longValueExact())
                    .map(i -> BigInteger.valueOf(i < s.length() ? s.charAt(i) : 0))
                    .forEach(stack::push);
        }
    },
    /**
     * An output operator, which pops once (the number of chars to read), and prints that number of chars to STD OUT.
     * The popped characters are consumed.
     */
    OUTPUT() {
        @Override
        public void execute(Deque<BigInteger> stack) {
            BigInteger numberOfCharactersToRead = stack.pop();

            Stream.iterate(0, i -> i + 1)
                    .limit(numberOfCharactersToRead == null ? stack.size() : numberOfCharactersToRead.longValueExact())
                    .forEach(i -> System.out.print((char) stack.pop().intValueExact()));
            System.out.println();

        }
    }
}
