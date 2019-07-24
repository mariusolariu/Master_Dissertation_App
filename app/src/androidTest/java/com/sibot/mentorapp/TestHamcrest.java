package com.sibot.mentorapp;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * Some exercieses from https://www.vogella.com/tutorials/Hamcrest/article.html#using-hamcrest-collection-matchers-for-lists
 */
public class TestHamcrest {
    private static boolean setUpIsDone;
    private static List<Integer> list;
    private static Integer[] arr;

    // JUnit instantiates a new instance of the test class for each @Test method.
    @Before
    public void setUp() {
        if (setUpIsDone) {
            return;
        }

        list = Arrays.asList(5, 2, 4);
        arr = new Integer[]{7, 5, 12, 16};
        setUpIsDone = true;
    }

    @Test
    public void testSize() {
        assertThat(list, hasSize(3));
    }

    @Test
    public void containsNumbers() {
        contains(Arrays.asList(2, 4, 5));
        assertThat(list, containsInAnyOrder(2, 4, 5));
    }

    @Test
    public void everyItemGreaterThan1() {
        assertThat(list, everyItem(greaterThan(Integer.valueOf(1))));
    }

    @Test
    public void arraySize() {
        assertThat(arr, arrayWithSize(4));
    }

    @Test
    public void arrayContainsValues() {
        assertThat(arr, arrayContainingInAnyOrder(7, 5, 12, 16));
    }


}