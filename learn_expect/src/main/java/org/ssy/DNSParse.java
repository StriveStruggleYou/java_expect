package org.ssy;

/**
 * Created by manager on 2018/7/22.
 */
public class DNSParse {


  public static void main(String args[]){

    byte[] receiveData = new byte[512];

    StringBuilder qname = new StringBuilder();
    int idx = 12;// skip
    // transaction/id/flags/questions/answer/authority/additional
    int len = receiveData[idx];
    //跳过头部，然后全部解析
    while (len > 0) {
      qname.append(".").append(
          new String(receiveData, idx + 1, len));
      idx += len + 1;
      len = receiveData[idx];
    }

    if (qname.length() > 0) {
      String name = qname.substring(1).toLowerCase();
      int type = receiveData[idx + 1] * 256
          + receiveData[idx + 2];
      log.info(receivePacket.getAddress() + ":"
          + receivePacket.getPort() + "\t" + name + "\t"
          + type);

      if ((!name.equals(host))
          && (!name.endsWith("." + host))) {
        continue;// keep silence
      }
      if (type != 1 && !name.equals(host)) {
        continue;// we only response for A records, except
        // for MX
        // for host
      }
    }
  }
}
