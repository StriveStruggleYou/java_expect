package org.ssy;

import static net.sf.expectit.filter.Filters.removeColors;
import static net.sf.expectit.filter.Filters.removeNonPrintable;
import static net.sf.expectit.matcher.Matchers.contains;
import static net.sf.expectit.matcher.Matchers.regexp;

import java.io.IOException;
import java.security.PublicKey;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Shell;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

/**
 * Created by manager on 2018/7/11.
 */
public class LearnSshJExpect {

  public static void main(String[] args) throws IOException {
    SSHClient ssh = new SSHClient();
    ssh.addHostKeyVerifier(
        new HostKeyVerifier() {
          @Override
          public boolean verify(String s, int i, PublicKey publicKey) {
            return true;
          }
        });
    ssh.connect("47.97.6.72",22);
    ssh.authPassword("root", "Chendekai00");
    Session session = ssh.startSession();
    session.allocateDefaultPTY();
    Shell shell = session.startShell();
    Expect expect = new ExpectBuilder()
        .withOutput(shell.getOutputStream())
        .withInputs(shell.getInputStream(), shell.getErrorStream())
        .withEchoInput(System.out)
        .withEchoOutput(System.err)
        .withInputFilters(removeColors(), removeNonPrintable())
        .withExceptionOnFailure()
        .build();
    try {
      expect.expect(contains("[RETURN]"));
      expect.sendLine("");
      String ipAddress = expect.expect(regexp("Trying (.*)\\.\\.\\.")).group(1);
      System.out.println("Captured IP: " + ipAddress);
      expect.expect(contains("login:"));
      expect.sendLine("new");
      expect.expect(contains("(Y/N)"));
      expect.send("N");
      expect.expect(regexp(": $"));
      expect.send("\b");
      expect.expect(regexp("\\(y\\/n\\)"));
      expect.sendLine("y");
      expect.expect(contains("Would you like to sign the guestbook?"));
      expect.send("n");
      expect.expect(contains("[RETURN]"));
      expect.sendLine();
    } finally {
      expect.close();
      session.close();
      ssh.close();
    }
  }

}
