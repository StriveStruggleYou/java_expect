package org.ssy;

import static net.sf.expectit.matcher.Matchers.contains;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.filter.Filters;

/**
 * Created by manager on 2018/7/8.
 */
public class LearnFilterExpect {

  public static void main(String args[]) throws IOException {
    Expect expect = new ExpectBuilder()
        .withInputs(new ByteArrayInputStream("\u0000\u0008x".getBytes()))
        .withInputFilters(Filters.removeNonPrintable())
        .build();
    System.out.println(expect.expect(contains("x")));

    expect.close();


  }
}
