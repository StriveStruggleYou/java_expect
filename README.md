#Linux Expect

一、概述

我们通过Shell可以实现简单的控制流功能，如：循环、判断等。但是对于需要交互的场合则必须通过人工来干预，有时候我们可能会需要实现和交互程序如telnet服务器等进行交互的功能。而Expect就使用来实现这种功能的工具。
Expect是一个免费的编程工具语言，用来实现自动和交互式任务进行通信，而无需人的干预。Expect的作者Don Libes在1990年 开始编写Expect时对Expect做有如下定义：Expect是一个用来实现自动交互功能的软件套件 (Expect [is a] software suite for automating interactive tools)。使用它系统管理员 的可以创建脚本用来实现对命令或程序提供输入，而这些命令和程序是期望从终端（terminal）得到输入，一般来说这些输入都需要手工输入进行的。 Expect则可以根据程序的提示模拟标准输入提供给程序需要的输入来实现交互程序执行。甚至可以实现实现简单的BBS聊天机器人。 :)
Expect是不断发展的，随着时间的流逝，其功能越来越强大，已经成为系统管理员的的一个强大助手。Expect需要Tcl编程语言的支持，要在系统上运行Expect必须首先安装Tcl。

二、Expect工作原理

从最简单的层次来说，Expect的工作方式象一个通用化的Chat脚本工具。Chat脚本最早用于UUCP网络内，以用来实现计算机之间需要建立连接时进行特定的登录会话的自动化。
Chat脚本由一系列expect-send对组成：expect等待输出中输出特定的字符，通常是一个提示符，然后发送特定的响应。例如下面的 Chat脚本实现等待标准输出出现Login:字符串，然后发送somebody作为用户名；然后等待Password:提示符，并发出响应 sillyme。
引用：Login: somebody Password: sillyme
这个脚本用来实现一个登录过程，并用特定的用户名和密码实现登录。
Expect最简单的脚本操作模式本质上和Chat脚本工作模式是一样的。


三、一个例子：

#!/usr/bin/expect -f
set ip [lindex $argv 0 ]  //接收第一个参数,并设置IP
set password [lindex $argv 1 ] //接收第二个参数,并设置密码
set timeout 10     //设置超时时间
spawn ssh root@$ip  //发送ssh请滶
expect {     //返回信息匹配
 "*yes/no" { send "yes\r"; exp_continue} //第一次ssh连接会提示yes/no,继续
 "*password:" { send "$password\r" }  //出现密码提示,发送密码
}
interact   //交互模式,用户会停留在远程服务器上面.
运行结果：

root@ubuntu:/home/zhangy# ./test.exp 192.168.1.130 admin
spawn ssh root@192.168.1.130
Last login: Fri Sep 7 10:47:43 2012 from 192.168.1.142
[root@linux ~]#

四、 使用maven rep expectit-core
 <!-- https://mvnrepository.com/artifact/net.sf.expectit/expectit-core -->
         <dependency>
           <groupId>net.sf.expectit</groupId>
           <artifactId>expectit-core</artifactId>
           <version>0.8.1</version>
         </dependency>

源代码在：https://github.com/tgbyte/expectj
而且我们都知道expec依赖：Tcl，那么expectj依赖的是TclJava
源代码在:https://github.com/scijava/tcljava





