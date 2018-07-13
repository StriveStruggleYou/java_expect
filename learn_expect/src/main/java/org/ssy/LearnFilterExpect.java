package org.ssy;

import static net.sf.expectit.matcher.Matchers.contains;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.Result;
import net.sf.expectit.filter.Filter;
import net.sf.expectit.filter.Filters;
import net.sf.expectit.matcher.Matchers;

public class LearnFilterExpect {

  public static void main(String args[]) throws IOException {

    filter2();
  }


  public static void filter1() throws IOException {
    Expect expect = new ExpectBuilder()
        .withInputs(new ByteArrayInputStream("\u0000\u0008x".getBytes()))
        .withInputFilters(Filters.removeNonPrintable())
        .build();
    System.out.println(expect.expect(contains("x")));

    expect.close();
  }

  public static void filter2() throws IOException {
    PipedInputStream pipedInputStream = new PipedInputStream();
    PipedOutputStream pipedOutputStream = new PipedOutputStream();

    System.out.println("Connecting pipes");
    pipedInputStream.connect(pipedOutputStream);

    System.out.println("Building expect");
    Expect expect = new ExpectBuilder()
        .withInputs(pipedInputStream)
        .withTimeout(30, TimeUnit.SECONDS)
        .withExceptionOnFailure()
        .withInputFilters(myFilter())
        .build();

    System.out.println("Writing data to output");
    for (int i = 0; i < 1500; i++) {
      pipedOutputStream.write("removeSome text hereremove\n".getBytes());
    }
    pipedOutputStream.write("done\n".getBytes());

    System.out.println("Flushing output");
    pipedOutputStream.flush();

    System.out.println("Waiting for 'done'");
    Result r = expect.expect(Matchers.contains("done"));

    System.out.println("Printing data");
    System.out.println(r.getBefore());

    System.out.println("Done");

    expect.close();

  }

  private static Filter myFilter() {
    return Filters.replaceInBuffer("remove", "");
  }

}
