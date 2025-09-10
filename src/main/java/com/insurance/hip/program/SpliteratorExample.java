package com.insurance.hip.program;

import java.util.*;

public class SpliteratorExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        Spliterator<String> spliterator = names.spliterator();

        // Using tryAdvance (like Iterator)
        System.out.println("Using tryAdvance:");
        while (spliterator.tryAdvance(System.out::println));

        // Reset
        spliterator = names.spliterator();

        // Using forEachRemaining
        System.out.println("\nUsing forEachRemaining:");
        spliterator.forEachRemaining(System.out::println);
    }
}
