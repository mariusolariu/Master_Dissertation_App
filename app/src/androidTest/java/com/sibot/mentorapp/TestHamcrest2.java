package com.sibot.mentorapp;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

public class TestHamcrest2 {
    private static Todo todoObj;
    private static boolean setupDone;

    @Before
    public void setUp() {
        if (setupDone) return;

        todoObj = new Todo(1, "Learn Hamcrest", "Vogella tutorial");
        setupDone = true;
    }

//    @Test Doesn't work because Android SDK doesn't contain a class called Introspector
//    public void todoPropertySummary(){
//        assertThat(todoObj, hasProperty("summary"));
//    }

    @Test
    public void emptyString() {
        assertThat("", isEmptyString());
    }

    @Test
    public void notEmptyOrNullString() {
        String allan = new String("Allan");
        assertThat(allan, not(isEmptyOrNullString()));
    }


    class Todo {

        private final long id;
        private String summary;
        private String description;
        private int year;

        Todo(long id, String summary, String description) {
            this.id = id;
            this.summary = summary;
            this.description = description;
        }

        public long getId() {
            return id;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}
