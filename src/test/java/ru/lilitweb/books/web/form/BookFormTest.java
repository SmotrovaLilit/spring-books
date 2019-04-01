package ru.lilitweb.books.web.form;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class BookFormTest {
    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    public void testIfAllCorrect() {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        List<BookForm> list = new ArrayList<>();
        list.add(new BookForm(
                "title",
                "author",
                2011,
                "description",
                genres));
        list.add(new BookForm(
                "title",
                "author",
                2012,
                "description",
                new ArrayList<>()));
        list.forEach(b -> {
            Set<ConstraintViolation<BookForm>> validates = validator.validate(b);
            Assert.assertEquals(0, validates.size());
        });
    }

    @Test
    public void testIfIncorrectTitle() {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        List<BookForm> list = new ArrayList<>();
        list.add(new BookForm(
                "",
                "author",
                2011,
                "description",
                genres));
        list.add(new BookForm(
                "s",
                "author",
                2011,
                "description",
                genres));
        list.add(new BookForm(
                "st",
                "author",
                2011,
                "description",
                genres));
        list.forEach(b -> {
            Set<ConstraintViolation<BookForm>> validates = validator.validate(b);
            Assert.assertEquals(1, validates.size());
            validates.stream().map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        });
    }

    @Test
    public void testIfIncorrectAuthor() {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        List<BookForm> list = new ArrayList<>();
        list.add(new BookForm(
                "title",
                "",
                2011,
                "description",
                genres));
        list.add(new BookForm(
                "title",
                "12",
                2011,
                "description",
                genres));
        list.forEach(b -> {
            Set<ConstraintViolation<BookForm>> validates = validator.validate(b);
            Assert.assertEquals(1, validates.size());
            validates.stream().map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        });
    }

    @Test
    public void testIfIncorrectDescription() {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        List<BookForm> list = new ArrayList<>();
        list.add(new BookForm(
                "title",
                "author",
                2011,
                "",
                genres));
        list.add(new BookForm(
                "title",
                "author",
                2011,
                "12",
                genres));
        list.forEach(b -> {
            Set<ConstraintViolation<BookForm>> validates = validator.validate(b);
            Assert.assertEquals(1, validates.size());
            validates.stream().map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        });
    }

    @Test
    public void testIfIncorrectYear() {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        List<BookForm> list = new ArrayList<>();
        list.add(new BookForm(
                "title",
                "author",
                0,
                "description",
                genres));
        list.add(new BookForm(
                "title",
                "author",
                50000,
                "description",
                genres));
        list.forEach(b -> {
            Set<ConstraintViolation<BookForm>> validates = validator.validate(b);
            Assert.assertEquals(1, validates.size());
            validates.stream().map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        });
    }
}
