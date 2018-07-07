package org.ssy;


import static net.sf.expectit.matcher.Matchers.eof;
import static net.sf.expectit.matcher.Matchers.regexp;
import static net.sf.expectit.matcher.Matchers.contains;


import java.io.IOException;
import java.util.concurrent.TimeUnit;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

/**
 * Created by manager on 2018/7/2.
 */
public class Learn_Expect {

  public static void main(String[] args) throws IOException, InterruptedException {
    Process process = Runtime.getRuntime().exec("/bin/sh");

    Expect expect = new ExpectBuilder()
        .withInputs(process.getInputStream())
        .withOutput(process.getOutputStream())
        .withTimeout(1, TimeUnit.SECONDS)
        .withEchoInput(System.out)
        .withEchoOutput(System.err)
        .withExceptionOnFailure()
        .build();
    System.out.println("PWD:" + expect.sendLine("pwd").expect(contains("\n")).getBefore());
    // try-with-resources is omitted for simplicity
    expect.sendLine("ls -lh");
    // capture the total
    String total = expect.expect(regexp("^total (.*)")).group(1);
    System.out.println("Size: " + total);

    expect.sendLine("ls -lh");
    // capture file list
    String list = expect.expect(regexp("\n$")).getBefore();
    // print the result
    System.out.println("List: " + list + "||list");
    expect.sendLine("exit");
    // expect the process to finish
    expect.expect(eof());
    // finally is omitted
    process.waitFor();
    expect.close();
  }




}
