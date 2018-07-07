package org.ssy;

import static net.sf.expectit.matcher.Matchers.contains;
import static net.sf.expectit.matcher.Matchers.eof;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import net.sf.expectit.Result;


/**
 * Created by manager on 2018/7/8.
 */
public class LearnProcessExpects {

  public static final String BIN_SH = "/bin/sh";


  public static void main(String args[]) throws IOException {
    //构建程序
    ProcessBuilder builder = new ProcessBuilder(BIN_SH);
    Process process = builder.start();
    Expect expect = new ExpectBuilder()
        .withTimeout(LONG_TIMEOUT, TimeUnit.MILLISECONDS)
        .withInputs(process.getInputStream(), process.getErrorStream())
        .withOutput(process.getOutputStream())
        .build();

    testEof(expect);

    //设置process
    if (process != null) {
      process.destroy();
    }
    if (expect != null) {
      expect.close();
    }

  }

  public static final long LONG_TIMEOUT
      = Long.getLong(LearnProcessExpects.class.getName() + ".longTimeout", 1500);


  public static void testErrorStream(Expect expect) throws IOException {
    String string = UUID.randomUUID().toString();
    expect.sendLine("echo " + string + " >&2");
    System.out.println(expect.expectIn(1, contains(string)).group());

  }

  public static void testEof(Expect expect) throws IOException {
    expect.sendLine("echo Line1");
    expect.sendLine("echo Line2");
    expect.sendLine("sleep " + LONG_TIMEOUT / 1000 + "; echo Line3; exit");
    Result result = expect.expect(5 * LONG_TIMEOUT, eof());
    System.out.println(result.getBefore());
    System.out.println(result.getBefore().equals("Line1\nLine2\nLine3\n"));
  }

}
