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
