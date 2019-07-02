```
2018-06-02 18:25:21
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.151-b12 mixed mode):

"Attach Listener" #14 daemon prio=9 os_prio=31 tid=0x00007fa62d016000 nid=0xf07 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"DestroyJavaVM" #13 prio=5 os_prio=31 tid=0x00007fa62f807000 nid=0x1903 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Thread2" #12 prio=5 os_prio=31 tid=0x00007fa630940800 nid=0x5803 waiting for monitor entry [0x00007000021c3000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at com.learning.example.thread.ThreadDump$Thread2.run(ThreadDump.java:58)
	- waiting to lock <0x000000076ac9e4d8> (a java.lang.Object)
	- locked <0x000000076ac9e4e8> (a java.lang.Object)

"Thread1" #11 prio=5 os_prio=31 tid=0x00007fa6300a6000 nid=0xa803 waiting for monitor entry [0x00007000020c0000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at com.learning.example.thread.ThreadDump$Thread1.run(ThreadDump.java:33)
	- waiting to lock <0x000000076ac9e4e8> (a java.lang.Object)
	- locked <0x000000076ac9e4d8> (a java.lang.Object)

"Service Thread" #10 daemon prio=9 os_prio=31 tid=0x00007fa6300a5800 nid=0x3c03 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread3" #9 daemon prio=9 os_prio=31 tid=0x00007fa62c80e000 nid=0x3b03 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread2" #8 daemon prio=9 os_prio=31 tid=0x00007fa63084f000 nid=0x3e03 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" #7 daemon prio=9 os_prio=31 tid=0x00007fa63084e800 nid=0x3903 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #6 daemon prio=9 os_prio=31 tid=0x00007fa63084d800 nid=0x4103 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Monitor Ctrl-Break" #5 daemon prio=5 os_prio=31 tid=0x00007fa630853800 nid=0x3803 runnable [0x00007000019ab000]
   java.lang.Thread.State: RUNNABLE
	at java.net.SocketInputStream.socketRead0(Native Method)
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:116)
	at java.net.SocketInputStream.read(SocketInputStream.java:171)
	at java.net.SocketInputStream.read(SocketInputStream.java:141)
	at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
	at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
	at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
	- locked <0x000000076adcc900> (a java.io.InputStreamReader)
	at java.io.InputStreamReader.read(InputStreamReader.java:184)
	at java.io.BufferedReader.fill(BufferedReader.java:161)
	at java.io.BufferedReader.readLine(BufferedReader.java:324)
	- locked <0x000000076adcc900> (a java.io.InputStreamReader)
	at java.io.BufferedReader.readLine(BufferedReader.java:389)
	at com.intellij.rt.execution.application.AppMainV2$1.run(AppMainV2.java:64)

"Signal Dispatcher" #4 daemon prio=9 os_prio=31 tid=0x00007fa62f804800 nid=0x3707 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=31 tid=0x00007fa62f009000 nid=0x4b03 in Object.wait() [0x00007000017a5000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x000000076ab08ec8> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
	- locked <0x000000076ab08ec8> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)

"Reference Handler" #2 daemon prio=10 os_prio=31 tid=0x00007fa62f006800 nid=0x4d03 in Object.wait() [0x00007000016a2000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x000000076ab06b68> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:502)
	at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
	- locked <0x000000076ab06b68> (a java.lang.ref.Reference$Lock)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"VM Thread" os_prio=31 tid=0x00007fa630844800 nid=0x3103 runnable 

"GC task thread#0 (ParallelGC)" os_prio=31 tid=0x00007fa62c800800 nid=0x2107 runnable 

"GC task thread#1 (ParallelGC)" os_prio=31 tid=0x00007fa62c80c000 nid=0x2203 runnable 

"GC task thread#2 (ParallelGC)" os_prio=31 tid=0x00007fa62c80c800 nid=0x2b03 runnable 

"GC task thread#3 (ParallelGC)" os_prio=31 tid=0x00007fa630000000 nid=0x5403 runnable 

"GC task thread#4 (ParallelGC)" os_prio=31 tid=0x00007fa62c80d000 nid=0x5303 runnable 

"GC task thread#5 (ParallelGC)" os_prio=31 tid=0x00007fa62f001000 nid=0x5103 runnable 

"GC task thread#6 (ParallelGC)" os_prio=31 tid=0x00007fa630001000 nid=0x2f03 runnable 

"GC task thread#7 (ParallelGC)" os_prio=31 tid=0x00007fa62f002000 nid=0x3003 runnable 

"VM Periodic Task Thread" os_prio=31 tid=0x00007fa62f8a2800 nid=0x5603 waiting on condition 

JNI global references: 22


Found one Java-level deadlock:
=============================
"Thread2":
  waiting to lock monitor 0x00007fa62d015d58 (object 0x000000076ac9e4d8, a java.lang.Object),
  which is held by "Thread1"
"Thread1":
  waiting to lock monitor 0x00007fa62d0148b8 (object 0x000000076ac9e4e8, a java.lang.Object),
  which is held by "Thread2"

Java stack information for the threads listed above:
===================================================
"Thread2":
	at com.learning.example.thread.ThreadDump$Thread2.run(ThreadDump.java:58)
	- waiting to lock <0x000000076ac9e4d8> (a java.lang.Object)
	- locked <0x000000076ac9e4e8> (a java.lang.Object)
"Thread1":
	at com.learning.example.thread.ThreadDump$Thread1.run(ThreadDump.java:33)
	- waiting to lock <0x000000076ac9e4e8> (a java.lang.Object)
	- locked <0x000000076ac9e4d8> (a java.lang.Object)

Found 1 deadlock.
```
