package br.ce.wcaquino.taskbackend.utils;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilsTest {
    @Test
    public void mustReturnTrueToFutureDates() {
        LocalDate localDate = LocalDate.now().plusDays(1);
        Assert.assertTrue(DateUtils.isEqualOrFutureDate(localDate));
    }

    @Test
    public void mustReturnFalseToFutureDates() {
        LocalDate localDate = LocalDate.now().plusDays(-1);
        Assert.assertFalse(DateUtils.isEqualOrFutureDate(localDate));
    }

    @Test
    public void mustReturnTrueToPresentDates() {
        LocalDate localDate = LocalDate.now();
        Assert.assertTrue(DateUtils.isEqualOrFutureDate(localDate));
    }

}
