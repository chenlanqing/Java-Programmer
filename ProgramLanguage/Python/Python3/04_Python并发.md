# 一、多线程

## 1、线程模块

### 1.1、单线程程序

```py
from time import sleep, perf_counter
def task():
    print('Starting a task...')
    sleep(1)
    print('done')
start_time = perf_counter()
task()
task()
end_time = perf_counter()
print(f'It took {end_time- start_time: 0.2f} second(s) to complete.')
```
该程序只有一个进程，只有一个线程，即主线程。因为程序只有一个线程，所以称为单线程程序

### 1.2、多线程程序

要创建多线程程序，需要使用 Python threading模块，使用方式：
```py
from threading import Thread
# 创建线程
new_thread = Thread(target=fn,args=args_tuple)
```
Thread 接收多个参数，主要参数：
- `target`：指定要在新线程中运行的函数（`fn`）。
- `args`：指定函数 (`fn`) 的参数。args 参数是一个元组

启动线程：`new_thread.start()`，如果想在主线程中等待线程完成，可以调用 join() 方法：`new_thread.join()`，通过调用 `join()` 方法，主线程将等待子线程完成后才终止；

示例：
```py
from time import sleep, perf_counter
from threading import Thread
def task():
    print('Starting a task...')
    sleep(1)
    print('done')
start_time = perf_counter()
# create two new threads
t1 = Thread(target=task)
t2 = Thread(target=task)
# start the threads
t1.start()
t2.start()
# wait for the threads to complete
t1.join()
t2.join()
end_time = perf_counter()
print(f'It took {end_time- start_time: 0.2f} second(s) to complete.')
```
线程带参数：
```py
threads = []
for n in range(1, 11):
    t = Thread(target=task, args=(n,))
    threads.append(t)
    t.start()
```
请注意，如果在循环内调用 `join()` 方法，程序会等待第一个线程完成后再启动下一个线程；

### 1.3、适用场景

- I/O 绑定任务 - 用于 I/O 的时间明显多于用于计算的时间。
- CPU 绑定任务 - 用于计算的时间明显多于等待 I/O 的时间。

Python 线程针对 I/O 绑定任务进行了优化。例如，请求远程资源、连接数据库服务器或读写文件

## 2、扩展Thread类

Python 程序启动时，会有一个称为主线程的线程。有时，想把 I/O 绑定的任务卸载到一个新线程中，让它们并发执行。为此，可以使用内置的线程模块；

在新线程中执行代码的一种方法是扩展threading模块的 Thread 类。具体步骤如下：
- 首先，定义 `threading.Thread` 类的子类。
- 其次，在子类的 `__init__()` 方法中重载 `__init__(self, [,args])` 方法，以添加自定义参数。
- 第三，重载子类中的 `run(self, [,args])` 方法，以便在创建新线程时自定义新线程类的行为。

示例：
```py
from threading import Thread
import urllib.request
class HttpRequestThread(Thread):
    def __init__(self, url: str) -> None:
        super().__init__()
        self.url = url
    def run(self) -> None:
        print(f'Checking {self.url} ...')
        try:
            response = urllib.request.urlopen(self.url)
            print(response.code)
        except urllib.error.HTTPError as e:
            print(e.code)
        except urllib.error.URLError as e:
            print(e.reason)
```
要使用 HttpRequestThread 类，需要创建 HttpRequestThread 类实例并调用 start() 方法。此外，你还可以调用 join() 方法来等待所有线程完成;

## 3、获取线程返回值

要从线程中返回值，可以扩展线程类，并将该值存储在该类的实例中，比如：
```py
from threading import Thread
import urllib.request
class HttpRequestThread(Thread):
    def __init__(self, url: str) -> None:
        super().__init__()
        self.url = url
        self.http_status_code = None
        self.reason = None
    def run(self) -> None:
        try:
            response = urllib.request.urlopen(self.url)
            self.http_status_code = response.code
        except urllib.error.HTTPError as e:
            self.http_status_code = e.code
        except urllib.error.URLError as e:
            self.reason = e.reason
def main() -> None:
    urls = [
        'https://httpstat.us/200',
        'https://httpstat.us/400'
    ]
    # create new threads
    threads = [HttpRequestThread(url) for url in urls]
    # start the threads
    [t.start() for t in threads]
    # wait for the threads to complete
    [t.join() for t in threads]
    # display the URLs with HTTP status codes
    [print(f'{t.url}: {t.http_status_code}') for t in threads]

if __name__ == '__main__':
    main()
```
总结：扩展线程类并在子类中设置实例变量，以便将子线程的值返回给主线程

## 4、守护线程

有时候想在后台执行一项任务。为此，需要使用一种特殊的线程，称为守护线程；根据定义，守护进程线程是后台线程。换句话说，守护进程线程在后台执行任务

以下场景适合使用：
- 在后台将信息记录到文件。
- 在后台抓取网站内容。
- 在后台将数据自动保存到数据库。

创建守护线程：
```py
t = Thread(target=f, deamon=True)
# 或者，也可以在创建线程实例后将守护进程属性设置为 True
t = Thread(target=f)
t.deamon = True
```
此外，当程序退出时，守护进程线程会被自动杀死。因为它不需要等待守护进程线程完成；

## 5、ThreadPoolExecutor

手动管理线程并不高效，因为频繁创建和销毁许多线程的计算成本非常高昂。如果希望在程序中运行许多临时任务，可以重复使用线程，而不是这样重复创建、销毁。线程池可以实现这一目的

线程池是一种在程序中实现并发执行的模式。通过线程池，可以自动高效地管理线程池，线程池中的每个线程都称为工作线程或工作者。线程池允许您在任务完成后重复使用工作线程。它还能防止意外故障，如异常；

通常，线程池允许配置工作线程的数量，并为每个工作线程提供特定的命名约定。要创建线程池，可使用 `concurrent.futures` 模块中的 `ThreadPoolExecutor` 类，ThreadPoolExecutor 类扩展了 Executor 类，并返回一个 Future 对象

### 5.1、Executor

Executor 类有三个控制线程池的方法：
- `submit()` ：调度一个要执行的函数，并返回一个 Future 对象。submit() 方法接收一个函数并异步执行。
- `map()` ：针对可迭代元素中的每个元素异步执行一个函数。
- `shutdown()` ：关闭执行器。

创建 ThreadPoolExecutor 类的新实例时，Python 会启动 Executor；完成执行器的工作后，必须明确调用 shutdown() 方法来释放执行器持有的资源。为避免显式调用 shutdown() 方法，可以使用上下文管理器

### 5.2、Future

Future 是一个表示异步操作最终结果的对象。Future 类有两个有用的方法
- result() - 返回异步操作的结果。
- exception() - 返回异步操作的异常，以防出现异常

### 5.3、线程示例

```py
from time import sleep, perf_counter
from concurrent.futures import ThreadPoolExecutor

def task(id):
    print(f'Starting the task {id}...')
    sleep(1)
    return f'Done with task {id}'

start = perf_counter()

with ThreadPoolExecutor() as executor:
    f1 = executor.submit(task, 1)
    f2 = executor.submit(task, 2)

    print(f1.result())
    print(f2.result())    

finish = perf_counter()

print(f"It took {finish-start} second(s) to finish.")
```
submit() 方法返回一个 Future 对象。在本例中，我们有两个 Future 对象 f1 和 f2。为了从 Future 对象中获取结果，我们调用了它的 result() 方法

使用`map()`方法：
```py
from time import sleep, perf_counter
from concurrent.futures import ThreadPoolExecutor
def task(id):
    print(f'Starting the task {id}...')
    sleep(1)
    return f'Done with task {id}'

start = perf_counter()
with ThreadPoolExecutor() as executor:
    results = executor.map(task, [1,2])
    for result in results:
        print(result)
finish = perf_counter()
print(f"It took {finish-start} second(s) to finish.")
```

# 二、线程同步

## 1、Lock

竞争条件：多个线程同时操作同一个共享变量时发生的竞争

为防止出现竞争条件，可以使用线程锁。线程锁是一种同步原语，在多线程应用程序中提供对共享资源的独占访问。线程锁也称为互斥锁（mutex），是互斥的简称。

通常，线程锁有两种状态：锁定和解锁。当线程获得锁时，锁就会进入锁定状态。线程可以独占访问共享资源。其他线程如果试图在锁定状态下获取锁，则会被阻塞，并等待锁被释放；

在 Python 中，可以使用 threading 模块中的 Lock 类创建锁对象：
```py
# （1）创建锁实例；默认情况下，在线程获取锁之前，锁是解锁的
lock = Lock()
# （2）通过调用 acquire() 方法获取锁
lock.acquire()
# （3）线程完成更改共享变量后释放锁
lock.release()
```
示例：
```py
from threading import Thread, Lock
from time import sleep
counter = 0
def increase(by, lock):
    global counter
    lock.acquire()
    local_counter = counter
    local_counter += by
    sleep(0.1)
    counter = local_counter
    print(f'counter={counter}')
    lock.release()
lock = Lock()
# create threads
t1 = Thread(target=increase, args=(10, lock))
t2 = Thread(target=increase, args=(20, lock))
# start the threads
t1.start()
t2.start()
# wait for the threads to complete
t1.join()
t2.join()
print(f'The final counter is {counter}')
```

**在 with 语句来使用线程锁**
```py
import threading
# Create a lock object
lock = threading.Lock()
# Perform some operations within a critical section
with lock:
    # Lock was acquired within the with block
    # Perform operations on the shared resource
    # ...
# the lock is released outside the with block
```
比如上面的increase中加锁的方法：
```py
def increase(by, lock):
    global counter

    with lock:
        local_counter = counter
        local_counter += by
        sleep(0.1)
        counter = local_counter
        print(f'counter={counter}')
```

定义一个线程安全的 Counter
```py
from threading import Thread, Lock
from time import sleep
class Counter:
    def __init__(self):
        self.value = 0
        self.lock = Lock()
    def increase(self, by):
        with self.lock:
            current_value = self.value
            current_value += by
            sleep(0.1)
            self.value = current_value
            print(f'counter={self.value}')
def main():
    counter = Counter()
    # create threads
    t1 = Thread(target=counter.increase, args=(10, ))
    t2 = Thread(target=counter.increase, args=(20, ))
    # start the threads
    t1.start()
    t2.start()
    # wait for the threads to complete
    t1.join()
    t2.join()
    print(f'The final counter is {counter.value}')
if __name__ == '__main__':
    main()
```

## 2、线程事件

有时候，需要在线程之间进行通信。为此可以使用锁（mutex）和布尔变量；Pyython 为提供了线程间通信的更好方法，使用threading模块中的Event类；

Event类为线程之间的协调提供了一种简单而有效的方法：一个线程发出信号，其他线程等待事件发生，Event对象封装了一个布尔标志，可以设置（True）或清除（False）。多个线程可以等待事件被设置后再继续处理，也可以将事件重置回清除状态
```py
from threading import Event
event = Event() # 默认情况下，事件未被设置（清除）。Event对象的 is_set() 方法将返回 False
if event.is_set():
   # ...
event.set() # 一旦设置了事件，所有等待该事件的线程都会自动收到通知
event.clear() # 清除
event.wait() # 线程可以通过 wait() 方法等待事件被设置
```
wait() 方法会阻止一个线程的执行，直到事件被设置。换句话说，wait() 方法会阻止当前线程的执行，直到另一个线程调用 set() 方法设置事件；如果设置了事件，函数 wait() 会立即返回；

要指定线程等待的时间，可以使用超时参数：
```py
event.wait(timeout=5) # wait for 5 seconds
```

示例：
```py
from threading import Thread, Event
from time import sleep
def task(event: Event, id: int) -> None:
    print(f'Thread {id} started. Waiting for the signal....')
    event.wait()
    print(f'Received signal. The thread {id} was completed.')

def main() -> None:
    event = Event()
    t1 = Thread(target=task, args=(event,1))
    t2 = Thread(target=task, args=(event,2))
    t1.start()
    t2.start()
    print('Blocking the main thread for 3 seconds...')
    sleep(3) 
    event.set()
if __name__ == '__main__':
    main()
```

使用Event来实现如下：
- 线程1下载文件，文件下载完成后，通知线程2统计下载文件中单词的数量；
- 线程2统计下载文件中单词的数量；
```py
from threading import Thread, Event
from urllib import request
def download_file(url, event):
    # Download the file form URL
    print(f"Downloading file from {url}...")
    filename, _ = request.urlretrieve(url, "rfc793.txt")
    # File download completed, set the event
    event.set()

def process_file(event):
    print("Waiting for the file to be downloaded...")
    event.wait()  # Wait for the event to be set
    # File has been downloaded, start processing it
    print("File download completed. Starting file processing...")
    # Count the number of words in the file
    word_count = 0
    with open("rfc793.txt", "r") as file:
        for line in file:
            words = line.split()
            word_count += len(words)
    # Print the word count
    print(f"Number of words in the file: {word_count}")

def main():
    # Create an Event object
    event = Event()
    # Create and start the file download thread
    download_thread = Thread(target=download_file, args=("https://www.ietf.org/rfc/rfc793.txt",  event))
    download_thread.start()
    # Create and start the file processing thread
    process_thread = Thread(target=process_file, args=(event,))
    process_thread.start()
    # Wait for both threads to complete
    download_thread.join()
    process_thread.join()
    print("Main thread finished.")

if __name__ == '__main__'    :
    main()
```

## 3、停止线程

要停止线程，可以使用threading模块的Event类。Event类有一个内部线程安全布尔标志，可设置为 True 或 False。默认情况下，内部标志为 False；

在Event类中，set() 方法将内部标志设置为 True，而 clear() 方法则将标志重置为 False。此外，如果内部标志被设置为 True，则 is_set() 方法返回 True；

要从主线程停止子线程，可使用Event对象，步骤如下：
- 首先，创建一个新的Event对象并将其传递给子线程。
- 其次，通过调用 is_set()方法定期检查子线程是否设置了Event对象的内部标志，如果设置了内部标志，则停止子线程。
- 第三，在某个时间点调用主线程中的 set() 方法来停止子线程。

示例：
```py
from threading import Thread, Event
from time import sleep

def task(event: Event) -> None:
    for i in range(6):
        print(f'Running #{i+1}')
        sleep(1)
        if event.is_set():
            print('The thread was stopped prematurely.')
            break
    else:
        print('The thread was stopped maturely.')

def main() -> None:
    event = Event()
    thread = Thread(target=task, args=(event,))
    
    # start the thread
    thread.start()

    # suspend  the thread after 3 seconds
    sleep(3)

    # stop the child thread
    event.set()    
   
if __name__ == '__main__':
    main()
```

**使用线程类的子类停止线程**

下面的示例展示了如何使用Thread类的派生类创建子线程，并使用Event对象停止子线程与主线程的需求：
```py
from threading import Thread, Event
from time import sleep

class Worker(Thread):
    def __init__(self, event, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.event = event

    def run(self) -> None:
        for i in range(6):
            print(f'Running #{i+1}')
            sleep(1)
            if self.event.is_set():
                print('The thread was stopped prematurely.')
                break
        else:
            print('The thread was stopped maturely.')
def main() -> None:
    # create a new Event object
    event = Event()
    # create a new Worker thread
    thread = Worker(event)
    # start the thread
    thread.start()
    # suspend  the thread after 3 seconds
    sleep(3)
    # stop the child thread
    event.set()    
if __name__ == '__main__':
    main()
```

## 4、信号量

Python semaphore 是一种同步原语，它允许控制对共享资源的访问。基本上，一个信号是一个与锁相关联的计数器，它限制了可以同时访问共享资源的线程数；在这种情况下，多个线程会尝试同时访问资源，从而干扰彼此的操作。

一个semaphore会保存一个计数。当一个线程要访问共享资源时，semaphore 会检查计数；如果计数大于零，就会减少计数并允许线程访问资源。如果计数为零，则信号屏蔽会阻塞线程，直到计数大于零。

信号量有两个主要操作：
- Acquire：获取操作会检查计数，如果大于零，就会递减计数。如果计数为零，则该 semaphore 将阻塞线程，直到其他线程释放该 semaphore。
- Release：释放操作会递增计数，允许其他线程获取它。

如何使用：
```py
import threading
semaphore = threading.Semaphore(3) # 创建 Semaphore 对象，并指定可同时获取该对象的线程数
semaphore.acquire() # 调用 acquire() 方法从线程中获取一个semaphore 
semaphore.release() # 在运行关键代码段后，调用 release() 方法释放一个semaphore 
```
为了确保即使在运行代码的关键部分时出现异常，也能正确获取和释放信号，可以使用 with 语句：
```py
with semaphore:
    # Code within this block has acquired the semaphore

    # Perform operations on the shared resource
    # ...
# The semaphore is released outside the with block
```

案例：
```py
import threading
import urllib.request

MAX_CONCURRENT_DOWNLOADS = 3
semaphore = threading.Semaphore(MAX_CONCURRENT_DOWNLOADS)

def download(url):
    with semaphore:
        print(f"Downloading {url}...")
        response = urllib.request.urlopen(url)
        data = response.read()
        print(f"Finished downloading {url}")
        return data

def main():
    # URLs to download
    urls = [
        'https://www.ietf.org/rfc/rfc791.txt',
        'https://www.ietf.org/rfc/rfc792.txt',
        'https://www.ietf.org/rfc/rfc793.txt',
        'https://www.ietf.org/rfc/rfc794.txt',
        'https://www.ietf.org/rfc/rfc795.txt',
    ]
    # Create threads for each download
    threads = []
    for url in urls:
        thread = threading.Thread(target=download, args=(url,))
        threads.append(thread)
        thread.start()

    # Wait for all threads to complete
    for thread in threads:
        thread.join()
if __name__ == '__main__':
    main()
```

# 三、线程安全队列

通过内置的queue模块，可以在多个线程之间安全地交换数据。Queue模块中的queue类实现了所有必需的锁定语义

**创建队列：**
```py
from queue import Queue
queue = Queue()
```
要创建有大小限制的队列，可以使用 maxsize 参数。例如，下面创建的队列最多可存储 10 个项目：
```py
queue = Queue(maxsize=10)
```

**添加元素到队列**
```py
queue.put(item)
```
一旦队列已满，就无法再向其中添加项目。此外，调用 put() 方法将阻塞，直到队列有可用空间为止；如果不想在队列已满时阻塞 put() 方法，可以将block 参数设置为 "False"。
```py
queue.put(item, block=False)
```
在这种情况下，如果队列已满，put() 方法将引发 queue.Full 异常。
```py
try:
   queue.put(item, block=False)
except queue.Full as e:
   # handle exceptoin
```
要将一个项目添加到有大小限制的队列中并使用超时阻塞，可以使用timeout参数，如下所示：
```py
try:
   queue.put(item, timeout=3)
except queue.Full as e:
   # handle exceptoin
```

**从队列中获取元素**
```py
item = queue.get()
```
get() 方法将阻塞，直到有项目可从队列中检索为止；要在不阻塞的情况下从队列中获取项目，可以将block 参数设置为False：
```py
try:
   queue.get(block=False)
except queue.Empty:
   # handle exception
```
要从队列中获取一个项目并在一定时间内阻塞它，可以使用带有超时的 get() 方法：
```py
try:
   item = queue.get(timeout=10)
except queue.Empty:
   # ...
```

**获取队列长度**
```py
size = queue.size()
```
此外，如果队列为空，empty() 方法返回 True，否则返回 False。另一方面，如果队列已满，full() 方法返回 True，否则返回 False。

**将任务标记为已完成**

添加到队列中的项目代表一个工作单位或一项任务，当线程调用 get() 方法从队列中获取项目时，可能需要在任务被认为完成之前对其进行处理，完成后，线程可调用队列的 task_done() 方法，以表示已完全处理了任务：
```py
item = queue.get()

# process the item
# ...

# mark the item as completed
queue.task_done()
```
等待队列中的所有任务完成：`queue.join()`

**使用python实现生产者/消费者**
```py
import time
from queue import Empty, Queue
from threading import Thread

def producer(queue):
    for i in range(1, 6):
        print(f'Inserting item {i} into the queue')
        time.sleep(1)
        queue.put(i)

def consumer(queue):
    while True:
        try:
            item = queue.get()
        except Empty:
            continue
        else:
            print(f'Processing item {item}')
            time.sleep(2)
            queue.task_done()

def main():
    queue = Queue()

    # create a producer thread and start it
    producer_thread = Thread(
        target=producer,
        args=(queue,)
    )
    producer_thread.start()

    # create a consumer thread and start it
    consumer_thread = Thread(
        target=consumer,
        args=(queue,),
        daemon=True
    )
    consumer_thread.start()

    # wait for all tasks to be added to the queue
    producer_thread.join()

    # wait for all tasks on the queue to be completed
    queue.join()

if __name__ == '__main__':
    main()
```
