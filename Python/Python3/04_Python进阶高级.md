
# 一、错误与调试

在程序运行的过程中，如果发生了错误，可以事先约定返回一个错误代码，这样，就可以知道是否有错，以及出错的原因

## 1、错误与异常处理

### 1.1、`try...except...finally...`的错误处理机制
```python
try:
	print('try...')
	r = 10 / 0
	print('result'， r)
except ZeroDivisionError as e:
	print('Except:'， e)
finally:
	print('finally...')
print('End')
```
- 当我们认为某些代码可能会出错时，就可以用try来运行这段代码，如果执行出错，则后续代码不会继续执行，而是直接跳转至错误处理代码，即except语句块，执行完except后，如果有finally语句块，则执行finally语句块，至此，执行完毕；
- 此外，如果没有错误发生，可以在except语句块后面加一个else，当没有错误发生时，会自动执行else语句
```python
try:
	print('try...')
	r = 10 / int('2')
	print('result:'， r)
except ValueError as e:
	print('ValueError:'， e)
except ZeroDivisionError as e:
	print('ZeroDivisionError:'， e)
else:
	print('no error!')
finally:
	print('finally...')
print('END')
```
- python其实也是class，所有的错误类型都继承自BaseException，不需要在每个可能出错的地方去捕获错误，只要在合适的层次去捕获错误就可以了

### 1.2、调用堆栈

如果错误没有被捕获，它就会一直往上抛，最后被Python解释器捕获，打印一个错误信息，然后程序退出；

### 1.3、记录错误
如果不捕获错误，自然可以让Python解释器来打印出错误堆栈，但程序也被结束了。既然我们能捕获错误，就可以把错误堆栈打印出来，然后分析错误原因，同时，让程序继续执行下去

- Python内置的logging模块可以非常容易地记录错误信息，通过配置，logging还可以把错误记录到日志文件里
	```python
	# 同样是出错，但程序打印完错误信息后会继续执行，并正常退出：
	import logging
	def foo(s):
		return 10 / int(s)
	def bar(s):
		return foo(s) * 2
	def main():
		try:
			bar('0')
		except Exception as e:
			logging.exception(e)
	main()
	print('END')
	```
### 1.4、抛出错误

- 要抛出错误，首先根据需要，可以定义一个错误的class，选择好继承关系，然后用raise语句抛出一个错误的实例:
	```python
	class FooError(ValueError):
		pass
	def foo(s):
		n = int(s)
		if n == 0:
			raise FooError('invalid value %s' % s)
		return 10 / n
	foo('0')
	```

- 只有在必要的时候才定义我们自己的错误类型。如果可以选择Python已有的内置的错误类型(比如ValueError，TypeError)，尽量使用Python内置的错误类型

### 1.5、其他处理方式

```python
def foo(s):
	n = int(s)
	if n==0:
		raise ValueError('invalid value: %s' % s)
	return 10 / n
def bar():
	try:
		foo('0')
	except ValueError as e:
		print('ValueError!')
		raise
bar()
```
- 由于当前函数不知道应该怎么处理该错误，所以，最恰当的方式是继续往上抛，让顶层调用者去处理
- raise语句如果不带参数，就会把当前错误原样抛出.此外，在except中raise一个Error，还可以把一种类型的错误转化成另一种类型

## 3、python调试

### 3.1、print()语句

- 直接简单粗暴：使用print()语句把可能有问题的变量全部打印出来；
- 缺点：后期需要将print()语句删除
	
### 3.2、断言

凡是可以使用print()来辅助查看的地方，都可以使用断言assert代替:

```python
def foo(s):
	n = int(s)
	assert n != 0， 'n is zero!'
	return 10 / n
def main():
	foo('0')
```

(1).assert的意思是:表达式n != 0应该是True，否则，根据程序运行的逻辑，后面的代码肯定会出错。
(2).如果断言失败，assert语句本身就会抛出AssertionError：
(3).启动Python解释器时可以用-O参数来关闭assert

3.3.logging:和assert比，logging不会抛出错误，而且可以输出到文件
	import logging
	logging.basicConfig(level=logging.INFO)		
	logging.info('n = %d' % n)
	# 它允许你指定记录信息的级别，有debug，info，warning，error等几个级别
	(1).logging的另一个好处是通过简单的配置，一条语句可以同时输出到不同的地方

3.4.pdb:启动python的调试器pdb，让程序以单步运行，可以随时查看运行状态:
	如:
		#err.py
		s = '0'
		n = int(s)
		print(10 / n)
	(1).启动调试:python -m pdb err.py
		$ python -m pdb err.py
		> /Users/michael/Github/learn-python3/samples/debug/err.py(2)<module>()
		-> s = '0'
	(2).以参数-m pdb启动后，pdb定位到下一步要执行的代码-> s = '0'。输入命令l来查看代码
	(3).输入命令n可以单步执行代码
		(Pdb) n
		> /Users/michael/Github/learn-python3/samples/debug/err.py(3)<module>()
		-> n = int(s)
		(Pdb) n
		> /Users/michael/Github/learn-python3/samples/debug/err.py(4)<module>()
		-> print 10 / n
	(4).任何时候都可以输入命令p 变量名来查看变量：
		(Pdb) p s
		'0'
		(Pdb) p n
		0
	(5).输入命令q结束调试，退出程序：
		(Pdb) q
	缺点:如果代码量大，很难调试

3.5.pdb.set_trace():这个方法也是用pdb，但是不需要单步执行，我们只需要import pdb，
	然后在可能出错的地方放一个pdb.set_trace()，就可以设置一个断点
		import pdb
		s = '0'
		n = int(s)
		pdb.set_trace() # 运行到这里会自动暂停
		print 10 / n
	(1).运行代码，程序会自动在pdb.set_trace()暂停并进入pdb调试环境，可以用命令p查看变量，或者用命令c继续运行：
3.6.IDE调试:目前比较好的Python IDE有PyCharm，Eclipse加上pydev插件也可以调试Python程序

4.单元测试:
	4.1.单元测试的优势:
		单元测试可以有效地测试某个程序模块的行为，是未来重构代码的信心保证。
		单元测试的测试用例要覆盖常用的输入组合、边界条件和异常。
		单元测试代码要非常简单，如果测试代码太复杂，那么测试代码本身就可能有bug。
		单元测试通过了并不意味着程序就没有bug了，但是不通过程序肯定有bug。
	4.2.编写单元测试:
		(1).编写一个测试类，从 unittest.TestCase 继承
			import unittest
			class TestDict(unittest.TestCase):
		(2).以 test 开头的方法就是测试方法，不以 tets 开头的方法不被认为是测试方法；
			对每一类测试都需要编写一个 tets_xxx()方法，由于unittest.TestCase提供了很多内置的条件判断，
			我们只需要调用这些方法就可以断言输出是否是我们所期望的。最常用的断言就是assertEqual()
			self.assertEqual(abs(-1)， 1) # 断言函数返回的结果与1相等
	4.3.运行单元测试:
		(1).最简单的方法:在代码最后加上两行代码
			if __name__ == '__main__':
				unittest.main()
			这样可以把测试类当作正常的python脚本运行
		(2).在命令行通过参数-m unittest直接运行单元测试 ##推荐使用此种方法，可以批量运行
			python -m unittest py文件

	4.5.setUp和tearDonw
		在单元测试中编写两个特殊的setUp()和tearDown()方法，这两个方法会分别在每调用一个测试方法的前后分别被执行
		##setUp()和tearDown()方法有什么用呢?
		设想你的测试需要启动一个数据库，就可以在setUp()方法中连接数据库，在tearDown()方法中关闭数据库，
		不必在每个测试方法中重复相同的代码:
	4.6.完整的单元测试案例:
		
5.文档测试:Python内置的“文档测试”（doctest）模块可以直接提取注释中的代码并执行测试
	5.1.doctest严格按照Python交互式命令行的输入和输出来判断测试结果是否正确，
		只有测试异常的时候，可以用...表示中间一大段烦人的输出
	5.2.编写注释时，如果写上如下的注释:
		def abs(n):
			'''
			Function to get absolute value of number.

			Example:

			>>> abs(1)
			1
			>>> abs(-1)
			1
			>>> abs(0)
			0
			'''
			return n if n >= 0 else (-n)
	5.3.案例:
		# mydict2.py
		class Dict(dict):
			'''
			Simple dict but also support access as x.y style

			>>> d1 = Dict()
			>>> d1['x'] = 100
			>>> d1.x
			100
			>>> d1.y = 200
			>>> d1['y']
			200
			>>> d2 = Dict(a=1， b=2， c='3')
			>>> d2.c
			'3'
			>>> d2['empty']
			Traceback (most recent call last):
				...
			KeyError: 'empty'
			>>> d2.empty
			Traceback (most recent call last):
				...
			AttributeError: 'Dict' object has no attribute 'empty'
			'''
			def __init__(self， **kw):
				super().__init__(**kw)

			def __getattr__(self， key):
				try:
					return self[key]
				except KeyError:
					raise AttributeError(r"'Dict' object has no attribute '%s'" % key)
			
			def __setattr__(self， key， value):
				self[key] = value

		if __name__ == '__main__':
			import doctest
			doctest.testmod()
		
		(1).运行代码:python mydict2.py
			什么输出也没有。这说明我们编写的doctest运行都是正确的。如果程序有问题，比如把__getattr__()方法注释掉，再运行就会报错
		(2).当模块正常导入时，doctest不会被执行.只有在命令行直接运行时，才执行doctest.
			所以，不必担心doctest会在非测试环境下执行
				1 items had failures:
				   2 of   9 in __main__.Dict
				***Test Failed*** 2 failures.

		
二.IO编程:读写文件是最常见的IO操作，Python内置了读写文件的函数，用法和C是兼容的
1.读文件:
	1.1.要以读文件的模式打开一个文件对象.使用Python内置的open()函数，传入文件名和标示符
		>>> f = open('/Users/michael/test.txt'， 'r') # 标示符'r'表示读
		(1).如果文件不存在，open()函数就会抛出一个IOError的错误，并且给出错误码和详细的信息告诉你文件不存在
		(2).如果文件打开成功，调用read()方法可以一次读取文件的全部内容，Python把内容读到内存，用一个str对象表示；
			>>> f.read()
		(3).最后一步是调用close()方法关闭文件
			>>> f.close()
		(4).由于文件读写时都有可能产生IOError，一旦出错，后面的f.close()就不会调用，可以使用try...finally...来实现
			try:
				f = open('/path/to/file'， 'r')
				print(f.read())
			finally:
				if f:
					f.close()
			# 但是每次都这么写实在太繁琐，所以，Python引入了with语句来自动帮我们调用close()方法:
			with open('/path/to/file'， 'r') as f:
				print(f.read())
			# 效果一样，同f.close()
		(5).调用read()会一次性读取文件的全部内容，如果文件很大，内存会溢出，为了安全起见，可以反复调用:
				read(size)  ==> 每次最多读取size个字节的内容
				readlines() ==> 一次读取所有内容并按行返回list
			# 如果文件很小，read()一次性读取最方便；如果不能确定文件大小，反复调用read(size)比较保险；
			# 如果是配置文件，调用readlines()最方便
			for line in f.readlines():
				print(line.strip()) # 把末尾的'\n'删掉
	1.2.像open()函数返回的这种有个read()方法的对象，在Python中统称为file-like Object.
		除了file外，还可以是内存的字节流，网络流，自定义流等等.file-like Object不要求从特定类继承，
		只要写个read()方法就行
		# StringIO就是在内存中创建的file-like Object，常用作临时缓冲
	1.3.二进制文件:
		前面讲的默认都是读取文本文件，并且是UTF-8编码的文本文件.要读取二进制文件，比如图片、视频等等，用'rb'模式打开文件即可
		>>> f = open('/Users/michael/test.jpg'， 'rb')
	1.4.字符编码:要读取非UTF-8编码的文本文件，需要给open()函数传入encoding参数；例如，读取GBK编码的文件：
		>>> f = open('/Users/michael/gbk.txt'， 'r'， encoding='gbk')
		到有些编码不规范的文件，你可能会遇到UnicodeDecodeError，因为在文本文件中可能夹杂了一些非法编码的字符。
		遇到这种情况，open()函数还接收一个errors参数，表示如果遇到编码错误后如何处理。最简单的方式是直接忽略:
		>>> f = open('/Users/michael/gbk.txt'， 'r'， encoding='gbk'， errors='ignore')

2.写文件:和读文件是一样的，唯一区别是调用open()函数时，传入标识符'w'或者'wb'表示写文本文件或写二进制文件
	>>> f = open('/Users/michael/test.txt'， 'w')
	>>> f.write('Hello， world!')
	>>> f.close()
	(1).当我们写文件时，操作系统往往不会立刻把数据写入磁盘，而是放到内存缓存起来，空闲的时候再慢慢写入。
		只有调用close()方法时，操作系统才保证把没有写入的数据全部写入磁盘。忘记调用close()的后果是数据可能只
		写了一部分到磁盘，剩下的丢失了。所以，还是用with语句来得保险:
		with open('/Users/michael/test.txt'， 'w') as f:
			f.write('Hello， world!')
		# 要写入特定编码的文本文件，请给open()函数传入encoding参数，将字符串自动转换成指定编码。

3.StringIO:在内存中读写str 	# StringIO操作的只能是str，
	3.1.数据读写不一定是文件，也可以在内存中读写
	3.2.要把str写入StringIO，我们需要先创建一个StringIO，然后，像文件一样写入即可
		>>> from io import StringIO
		>>> f = StringIO()
		>>> f.write('hello')
		5
		>>> f.write(' ')
		1
		>>> f.write('world!')
		6
		>>> print(f.getvalue())
		hello world!
		# getvalue()方法用于获得写入后的str
	3.3.读取StringIO可以用一个str初始化StringIO，然后像文件一样读取:
		>>> from io import StringIO
		>>> f = StringIO('Hello!\nHi!\nGoodbye!')
		>>> while True:
		...     s = f.readline()
		...     if s == '':
		...         break
		...     print(s.strip())
		...
		Hello!
		Hi!
		Goodbye!

4.BytesIO:操作二进制数据，BytesIO实现了在内存中读写bytes
	(1).写入的不是str，而是经过UTF-8编码的bytes
		>>> from io import BytesIO
		>>> f = BytesIO()
		>>> f.write('中文'.encode('utf-8'))
		6
		>>> print(f.getvalue())
		b'\xe4\xb8\xad\xe6\x96\x87'
	(2).
		>>> from io import StringIO
		>>> f = BytesIO(b'\xe4\xb8\xad\xe6\x96\x87')
		>>> f.read()
		b'\xe4\xb8\xad\xe6\x96\x87'

5.操作文件和目录
	5.1.os模块:直接调用操作系统提供的接口函数
	>>> import os
	>>> os.name # 操作系统类型
	'nt'
	#如果是posix:说明系统是Linux、Unix或Mac OS X；如果是nt:就是Windows系统
	(1).要获取详细的系统信息，可以调用uname()函数
		# 注意:uname()函数在Windows上不提供，也就是说，os模块的某些函数是跟操作系统相关的
		
	5.2.环境变量:
		(1).在操作系统中定义的环境变量，全部保存在 os.environ 这个变量中，可以直接查看；
		(2).要获取某个环境变量的值，可以调用os.environ.get('key')；
		
	5.3.操作目录:
		5.3.1.操作文件和目录的函数一部分放在 os 模块中，一部分放在 os.path 模块中
		5.3.2.查看、创建和删除目录可以这么调用:
			# 查看当前目录的绝对路径:
			>>> os.path.abspath('.')
			'/Users/michael'
			# 在某个目录下创建一个新目录，首先把新目录的完整路径表示出来:
			>>> os.path.join('/Users/michael'， 'testdir')
			'/Users/michael/testdir'
			# 然后创建一个目录:
			>>> os.mkdir('/Users/michael/testdir')
			# 删掉一个目录:
			>>> os.rmdir('/Users/michael/testdir')
			
			(1).两个路径合成一个时，不要直接拼字符串，而要通过os.path.join()函数，这样可以正确处理不同操作系统的路径分隔符，
				在Linux/Unix/Mac下，os.path.join()返回这样的字符串：
				==>part-1/part-2
				而Windows下会返回这样的字符串：
				==>'part-1\\part-2'
			(2).拆分路径时，也不要直接去拆字符串，而要通过os.path.split()函数，这样可以把一个路径拆分为两部分，
				后一部分总是最后级别的目录或文件名；
				>>> os.path.split('F:\class\HelloWorld.java')
				('F:\\class'， 'HelloWorld.java')
				>>> os.path.splitext('F:\class\HelloWorld.java')
				('F:\\class\\HelloWorld'， '.java')
				# 这些合并、拆分路径的函数并不要求目录和文件要真实存在，它们只对字符串进行操作
			
	5.4.文件操作:
		假设当前目录下有一个文件test.txt
		5.4.1.对文件操作使用如下函数:
			(1).文件重命名和删除
				# 对文件重命名:
				>>> os.rename('test.txt'， 'test.py')
				# 删掉文件:
				>>> os.remove('test.py')
			(2).复制文件:复制文件的函数在os模块中不存在 #原因是复制文件并非由操作系统提供的系统调用
				shutil模块提供了copyfile()的函数，你还可以在shutil模块中找到很多实用函数，它们可以看做是os模块的补充
			(3).os.path.isdir(x) ==> 判断文件是否为目录
				os.path.isfile(x) ==> 判断是否为文件
				os.path.listdir() ==> 列出选择的目下的所有文件
				
				#列出当前目录下的所有目录:
				==> [x for x in os.listdir('.') if os.path.isdir(x)]
				# 要列出所有的.py文件
				==> [x for x in os.listdir('.') if os.path.isfile(x) and os.path.splitext(x)[1]=='.py']
	5.5.练习:
		(1).利用os模块编写一个能实现dir -l输出的程序
		(2).编写一个程序，能在当前目录以及当前目录的所有子目录下查找文件名包含指定字符串的文件，并打印出相对路径
		
6.数据传输:
	6.1.序列化(pickling)与反序列化(unpickling):
		6.1.1.序列化:把变量从内存中变成可存储或传输的过程
			(1).Python提供了pickle模块来实现序列化
			# 把一个对象序列化并写入文件
			import pickle
			d = dict(name='Bob'， age=20， score=88)
			pickle.dumps(d) # pickle.dumps()方法把任意对象序列化成一个bytes，然后，就可以把这个bytes写入文件
			f = open('test.txt'，'wb')
			pickle.dumps(d，f)
		6.1.2.反序列化:把变量内容从序列化的对象重新读到内存里称之为反序列化
			要把对象从磁盘读到内存时，可以先把内容读到一个bytes，然后用pickle.loads()方法反序列化出对象，
			也可以直接用pickle.load()方法从一个file-like Object中直接反序列化出对象
			import pickle
			f = open('test.txt'，'r')
			d = pickle.load(f)
			f.close()
	6.2.JSON:
		6.2.1.JSON表示出来就是一个字符串，可以被所有语言读取，也可以方便地存储到磁盘或者通过网络传输.
			JSON不仅是标准格式，并且比XML更快，而且可以直接在Web页面中读取，非常方便；	
		6.2.2.JSON表示的对象就是标准的JavaScript语言的对象，JSON和Python内置的数据类型对应如下:
			JSON类型		Python类型
			{}				dict
			[]				list
			"string"		str
			123.45			int或float
			true/false		True/False
			null			None
		6.2.3.Python内置的json模块提供了非常完善的Python对象到JSON格式的转换:(JSON标准规定JSON编码是UTF-8)
			(1).python对象变成json:json.dumps()，dumps()方法返回一个str，内容是标准的json
				>>> import json
				>>> d = dict(name='Bob'， age=20， score=88)
				>>> json.dumps(d)
				'{"age": 20， "score": 88， "name": "Bob"}'		
		
			(2).要把JSON反序列化为Python对象，用loads()或者对应的load()方法，前者把JSON的字符串反序列化，
				后者从file-like Object中读取字符串并反序列化：
				>>> json_str = '{"age": 20， "score": 88， "name": "Bob"}'
				>>> json.loads(json_str)
				{'age': 20， 'score': 88， 'name': 'Bob'}
		6.2.4.JSON进阶:
			(1).定义类再序列化为json
				import json
				def Student(object):
					def __init__(self， name， age， score):
						self.name = name
						self.age = age
						self.score = score
				s = Student('Bob'， 25， 90)
				print(json.dumps(s))		
				==> 出现异常:TypeError: <__main__.Student object at 0x0000000001EFC320> is not JSON serializable
				# 错误的原因是Student对象不是一个可序列化为JSON的对象
			(2).dumps()方法的参数列表，除了第一个必须的obj参数外，dumps()方法还提供了一大堆的可选参数
				可选参数default就是把任意一个对象变成一个可序列为JSON的对象，我们只需要为Student专门写一个转换函数，
				再把函数传进去即可
					import json
					class Student(object):
						def __init__(self， name， age， score):
							self.name = name
							self.age = age
							self.score = score
					s = Student('Bob'， 25， 90)
					def student2dict(std):
						return {
							'name':std.name，
							'age':std.age，
							'score':std.score
						}
					print(json.dumps(s， default=student2dict))				
			(3).把任意class的实例变为dict:
				json.dumps(s， default=lambda obj: obj.__dict__)
				#原因:
				通常class的实例都有一个__dict__属性，它就是一个dict，用来存储实例变量，
				# 也有少数例外，比如定义了__slots__的class
				
			(4).如果我们要把JSON反序列化为一个Student对象实例，loads()方法首先转换出一个dict对象，
				然后，我们传入的object_hook函数负责把dict转换为Student实例
				def dict2student(d):
					return Student(d['name']， d['age']， d['score'])
				>>> json_str = '{"age": 20， "score": 88， "name": "Bob"}'
				>>> print(json.loads(json_str， object_hook=dict2student))
				<__main__.Student object at 0x10cd3c190>
		
三.进程和线程:[使用Liunx系统学习下](得重新学习)
1.多进程:
	(1).Unix/Linux操作系统提供了一个fork()系统调用，它非常特殊。普通的函数调用，调用一次，返回一次，
		但是fork()调用一次，返回两次，因为操作系统自动把当前进程（称为父进程）复制了一份（称为子进程），
		然后，分别在父进程和子进程内返回。
		子进程永远返回0，而父进程返回子进程的ID。这样做的理由是，一个父进程可以fork出很多子进程，
		所以，父进程要记下每个子进程的ID，而子进程只需要调用getppid()就可以拿到父进程的ID
	(2).Python的os模块封装了常见的系统调用，其中就包括 fork()，可以在Python程序中轻松创建子进程
		import os
		print('Process (%s) start...' % os.getpid())
		# Only works on Unix/Linux/Mac:
		pid = os.fork()
		if pid == 0:
		    print('I am child process (%s) and my parent is %s.' % (os.getpid()， os.getppid()))
		else:
		    print('I (%s) just created a child process (%s).' % (os.getpid()， pid))
		# 由于Windows没有fork调用，上面的代码在Windows上无法运行
		有了fork调用，一个进程在接到新任务时就可以复制出一个子进程来处理新任务，
		常见的Apache服务器就是由父进程监听端口，每当有新的http请求时，就fork出子进程来处理新的http请求
	(3).multiprocessing:
		编写多进程的服务程序，Unix/Linux无疑是正确的选择，由于Windows没有fork调用，由于Python是跨平台的，
		自然也应该提供一个跨平台的多进程支持multiprocessing 模块就是跨平台版本的多进程模块
		①.multiprocessing模块提供了一个Process类来代表一个进程对象
			from multiprocessing import Process
			import os
			def run_proc(name):
			    print('Run child process %s (%s)....' % (name， os.getpid()))
			if __name__=='__main__':
			    print('Parent process %s.' % os.getpid())
			    p = Process(target=run_proc， args=('test'，))
			    print('Child process will start')
			    p.start()
			    p.join()
			    print('Child process end...')
			创建子进程时，只需要传入一个执行函数和函数的参数，创建一个Process实例，用start()方法启动，
			这样创建进程比fork()还要简单。join()方法可以等待子进程结束后再继续往下运行，通常用于进程间的同步
	(4).Pool:进程池，如果要启动大量的子进程，可以用进程池的方式批量创建子进程
		from multiprocessing import Pool
		import os， time， random
		def long_time_task(name):
		    print('Run task %s (%s)...' % (name， os.getpid()))
		    start = time.time()
		    time.sleep(random.random() * 3)
		    end = time.time()
		    print('Task %s runs %0.2f seconds.' % (name， (end - start)))
		if __name__=='__main__':
		    print('Parent process %s.' % os.getpid())
		    p = Pool(4)
		    for i in range(5):
		        p.apply_async(long_time_task， args=(i，))
		    print('Waiting for all subprocesses done...')
		    p.close()
		    p.join()
		    print('All subprocesses done.')
		对Pool对象调用join()方法会等待所有子进程执行完毕，调用join()之前必须先调用close()，调用close()之后就
		不能继续添加新的Process了
		请注意输出的结果，task 0，1，2，3是立刻执行的，而task 4要等待前面某个task完成后才执行，这是因为Pool
		的默认大小在我的电脑上是4，因此，最多同时执行4个进程。这是Pool有意设计的限制，并不是操作系统的限制
	(5).子进程:	
		很多时候，子进程并不是自身，而是一个外部进程。我们创建了子进程后，还需要控制子进程的输入和输出
		subprocess 模块可以让我们非常方便地启动一个子进程，然后控制其输入和输出
		①.
			import subprocess 
			# 如何在Python代码中运行命令nslookup www.python.org
			print('$ nslookup www.python.org')
			r = subprocess.call(['nslookup'， 'www.python.org'])
			print('Exit code:'， r)；
		②.如果子进程还需要输入，则可以通过communicate()方法输入：
	(6).进程间通信:
		Python的 multiprocessing 模块包装了底层的机制，提供了 Queue、Pipes 等多种方式来交换数据
2.多线程:线程是操作系统直接支持的执行单元，一个进程至少有一个线程；Python的线程是真正的Posix Thread，而不是模拟出来的线程
	2.1.python的线程模块:_thread和threading，_thread是低级模块，threading是高级模块，对_thread进行了封装；
		启动一个线程就是把一个函数传入并创建 Thread 实例，然后调用 start()开始执行:
	2.2.Lock:创建一个锁就是通过threading.Lock()来实现
		而多线程中，所有变量都由所有线程共享，所以，任何一个变量都可以被任何一个线程修改，因此，
		线程之间共享数据最大的危险在于多个线程同时改一个变量
		(1).多线程处理:
			# 当多个线程同时执行lock.acquire()时，只有一个线程能成功地获取锁，然后继续执行代码，其他线程就继续等待直到获得锁为止
			# 获得锁的线程用完后一定要释放锁，用try...finally来确保锁一定会被释放
			balance = 0
			lock = threading.Lock()
			def change_it(n):
			    global balance
			    balance = balance + n
			    balance = balance - n
			def run_thread(n):
			    for i in range(100000):
			        lock.acquire() 
			        try:
			            change_it(n)
			        finally:
			            lock.release() # 锁一定要释放

			t1 = threading.Thread(target=run_thread， args=(5，))
			t2 = threading.Thread(target=run_thread， args=(8，))
			t1.start()
			t2.start()
			t1.join()
			t2.join()
			print(balance)
	2.3.多核CPU:
		(1).Python的线程虽然是真正的线程，但解释器执行代码时，有一个 GIL 锁: Global Interpreter Lock，任何Python线程执行前，
		必须先获得GIL锁，然后，每执行100条字节码，解释器就自动释放GIL锁，让别的线程有机会执行。这个GIL全局锁实际上把
		所有线程的执行代码都给上了锁，所以，多线程在Python中只能交替执行，即使100个线程跑在100核CPU上，也只能用到1个核
		(2).多个Python进程有各自独立的GIL锁，互不影响
	2.4.ThreadLocal:
		(1).一个线程使用自己的局部变量比使用全局变量好，因为局部变量只有线程自己能看见，不会影响其他线程，
			而全局变量的修改必须加锁
		(2).局部变量在函数传递时很麻烦:
			如果用一个全局dict存放所有的Student对象，然后以thread自身作为key获得线程对应的Student对象如何
			global_dict = {}
			def std_thread(name):
			    std = Student(name)
			    # 把std放到全局变量global_dict中：
			    global_dict[threading.current_thread()] = std
			    do_task_1()
			    do_task_2()
		(3).上述代码比较丑，可以使用 ThreadLocal
			import threading
			# 创建全局ThreadLocal对象:
			local_school = threading.local() # ThreadLocal对象，每个Thread对它都可以读写student属性，但互不影响
			def process_student():
			    # 获取当前线程关联的student:
			    std = local_school.student
			    print('Hello， %s (in %s)' % (std， threading.current_thread().name))
			def process_thread(name):
			    # 绑定ThreadLocal的student:
			    local_school.student = name
			    process_student()
			t1 = threading.Thread(target= process_thread， args=('Alice'，)， name='Thread-A')
			t2 = threading.Thread(target= process_thread， args=('Bob'，)， name='Thread-B')
			t1.start()
			t2.start()
			t1.join()
			t2.join()
			理解为全局变量local_school是一个 dict，不但可以用local_school.student，还可以绑定其他变量，如local_school.teacher等等
		(4).ThreadLocal最常用的地方就是为每个线程绑定一个数据库连接，HTTP请求，用户身份信息等，
			这样一个线程的所有调用到的处理函数都可以非常方便地访问这些资源
	2.5.分布式进程:???

四.正则表达式:
	在正则表达式中，用*表示任意个字符(包括0个)，用+表示至少一个字符，用?表示0个或1个字符，用{n}表示n个字符，用{n，m}表示n-m个字符		
1.re 模块:
	使用Python的r前缀，就不用考虑转义的问题了
	(1).match() 方法判断是否匹配，如果匹配成功，返回一个Match对象，否则返回None
		常见使用:
		import re
		test = '010-12345678'
		if re.match(r'^\d{3}\-\d{3，8}$'， test):
		    print('Valid Phone Number.')
		else:
		    print('Invalid Phone Number.')
2.切分字符串:
	(1).正常切分:
		>>> 'a b   c'.split(' ')
		['a'， 'b'， ''， ''， 'c']
		# 无法识别连续的空格
	(2).使用正则切分:
		>>> re.split(r'\s+'， 'a b   c')
		['a'， 'b'， 'c']
		>>> re.split(r'[\s\，]+'， 'a，b， c  d')
		['a'， 'b'， 'c'， 'd']
3.正则表达式分组:用()表示的就是要提取的分组(Group)
	如果正则表达式中定义了组，就可以在Match对象上用group()方法提取出子串来
	注意到group(0)永远是原始字符串，group(1)、group(2)……表示第1、2、……个子串
	>>> m = re.match(r'^(\d{3})-(\d{3，8})$'， '010-12345')
	>>> m
	<_sre.SRE_Match object； span=(0， 9)， match='010-12345'>
	>>> m.group(0)
	'010-12345'
	>>> m.group(1)
	'010'
	>>> m.group(2)
	'12345'
4.贪婪匹配:正则匹配默认是贪婪匹配，也就是匹配尽可能多的字符
	>>> re.match(r'^(\d+)(0*)$'， '102300').groups()
	('102300'， '')
	# 由于\d+采用贪婪匹配，直接把后面的0全部匹配了，结果0*只能匹配空字符串了
	必须让'\d+'采用非贪婪匹配（也就是尽可能少匹配），才能把后面的0匹配出来，加个?就可以让'\d+'采用非贪婪匹配
	>>> re.match(r'^(\d+?)(0*)$'， '102300').groups()
	('1023'， '00')
		
5.预编译正则表达式:
	(1).在Python中使用正则表达式时，re模块内部会干两件事情：
		==>编译正则表达式，如果正则表达式的字符串本身不合法，会报错；
		==>用编译后的正则表达式去匹配字符串
	(2).如果一个正则表达式要重复使用几千次，出于效率的考虑，我们可以预编译该正则表达式，接下来重复使用时就不需要编译这个步骤了
		>>> import re
		# 编译:
		>>> re_telephone = re.compile(r'^(\d{3})-(\d{3，8})$')
		# 使用：
		>>> re_telephone.match('010-12345').groups()
		('010'， '12345')
		>>> re_telephone.match('010-8086').groups()
		('010'， '8086')
		编译后生成Regular Expression对象，由于该对象自己包含了正则表达式，所以调用对应的方法时不用给出正则字符串

五.常用内建模块:
1.datetime:处理日期和时间的标准库:
	datetime是模块，datetime模块还包含一个datetime类，通过 from datetime import datetime 导入的才是datetime这个类，
	仅导入 import datetime，则必须引用全名datetime.datetime
	from datetime import datetime
	(1).获取当前日期和时间
		datetime.now()返回当前日期和时间，其类型是datetime
		>>> now = datetime.now() # 获取当前datetime
		>>> print(now)
		2015-05-18 16:28:07.198690
	(2).获取指定日期和时间:
		要指定某个日期和时间，我们直接用参数构造一个datetime
		>>> dt = datetime(2015， 4， 19， 12， 20) # 用指定日期时间创建datetime
		>>> print(dt)
		2015-04-19 12:20:00
	(3).datetime转换为timestamp:
		把1970年1月1日 00:00:00 UTC+00:00时区的时刻称为epoch time，记为 0（1970年以前的时间timestamp为负数），
		当前时间就是相对于epoch time的秒数，称为timestamp， timestamp的值与时区毫无关系
		把一个datetime类型转换为timestamp只需要简单调用timestamp()方法
		>>> dt = datetime(2015， 4， 19， 12， 20) # 用指定日期时间创建datetime
		>>> dt.timestamp() # 把timestamp转换为datetime
		1429417200.0
		# timestamp是一个浮点数。如果有小数位，小数位表示毫秒数
	(4).timestamp转换为datetime，要把timestamp转换为datetime，使用datetime提供的fromtimestamp()方法
		>>> t = 1429417200.0
		>>> print(datetime.fromtimestamp(t))
		2015-04-19 12:20:00
		==> 注意:
		timestamp是一个浮点数，它没有时区的概念，而datetime是有时区的。上述转换是在timestamp和本地时间做转换
		timestamp也可以直接被转换到UTC标准时区的时间
		>>> t = 1429417200.0
		>>> print(datetime.fromtimestamp(t)) # 本地时间
		2015-04-19 12:20:00
		>>> print(datetime.utcfromtimestamp(t)) # UTC时间
		2015-04-19 04:20:00
	(5).str转换为datetime:datetime.strptime()需要一个日期和时间的格式化字符串
		# 注意转换后的datetime是没有时区信息的
		>>> cday = datetime.strptime('2015-6-1 18:19:59'， '%Y-%m-%d %H:%M:%S')
		>>> print(cday)
		2015-06-01 18:19:59
	(6).datetime转换为str，转换方法是通过strftime()实现的，同样需要一个日期和时间的格式化字符串：
		>>> now = datetime.now()
		>>> print(now.strftime('%a， %b %d %H:%M'))
		Mon， May 05 16:28
	(6).datetime加减:加减可以直接用+和-运算符，不过需要导入timedelta这个类
		from datetime import datetime， timedelta
		>>> now = datetime.now()
		>>> now
		datetime.datetime(2015， 5， 18， 16， 57， 3， 540997)
		>>> now + timedelta(hours=10)
		datetime.datetime(2015， 5， 19， 2， 57， 3， 540997)
		>>> now - timedelta(days=1)
		datetime.datetime(2015， 5， 17， 16， 57， 3， 540997)
		>>> now + timedelta(days=2， hours=12)
		datetime.datetime(2015， 5， 21， 4， 57， 3， 540997)
	(7).本地时间转换为UTC时间:
		一个datetime类型有一个时区属性tzinfo，但是默认为 None，所以无法区分这个datetime到底是哪个时区，
		除非强行给datetime设置一个时区
		>>> from datetime import datetime， timedelta， timezone
		>>> tz_utc_8 = timezone(timedelta(hours=8)) # 创建时区UTC+8:00
		>>> now = datetime.now()
		>>> now
		datetime.datetime(2015， 5， 18， 17， 2， 10， 871012)
		>>> dt = now.replace(tzinfo=tz_utc_8) # 强制设置为UTC+8:00
		>>> dt
		datetime.datetime(2015， 5， 18， 17， 2， 10， 871012， tzinfo=datetime.timezone(datetime.timedelta(0， 28800)))
		# 如果系统时区恰好是UTC+8:00，那么上述代码就是正确的，否则，不能强制设置为UTC+8:00时区
	(8).时区转换:通过utcnow()拿到当前的UTC时间，再转换为任意时区的时间9

	(9).小结:
		如果要存储datetime，最佳方法是将其转换为timestamp再存储，因为timestamp的值与时区完全无关
2.collections:python内建的一个集合模块，提供了许多集合类
	2.1.namedtuple 是一个函数，它用来创建一个自定义的 tuple 对象，并且规定了 tuple 元素的个数，
	并以用属性而不是索引来引用 tuple 的某个元素， 具备 tuple 的不变性
		>>> from collections import namedtuple
		>>> Point = namedtuple('Point'， ['x'， 'y'])
		>>> p = Point(1， 2)
		>>> p.x
		1
		>>> p.y
		2
	2.2.deque:这里是双端队列
		使用 list 存储数据时，按索引访问元素很快，但是插入和删除元素就很慢了，因为list是线性存储，数据量大的时候，插入和删除效率很低
		deque 是为了高效实现插入和删除操作的双向列表，适合用于队列和栈
		>>> from collections import deque
		>>> q = deque(['a'， 'b'， 'c'])
		>>> q.append('x')
		>>> q.appendleft('y')
		>>> q
		deque(['y'， 'a'， 'b'， 'c'， 'x'])
		(1).deque除了实现list的append()和pop()外，还支持appendleft()和popleft()，这样就可以非常高效地往头部添加或删除元素。
			append(x): Add x to the right side of the deque.
			appendleft(x): Add x to the left side of the deque.
			pop():Remove and return an element from the right side of the deque.If no elements are present，raises an IndexError.
			popleft(): Remove and return an element from the left side of the deque.If no elements are present，raises an IndexError.
	2.3.defaultdict:
		(1).使用dict时，如果引用的Key不存在，就会抛出KeyError。如果希望key不存在时，返回一个默认值，就可以用defaultdict
		(2).注意默认值是调用函数返回的，而函数在创建defaultdict对象时传入，
			除了在Key不存在时返回默认值，defaultdict的其他行为跟 dict 是完全一样的
			>>> from collections import defaultdict
			>>> dd = defaultdict(lambda: 'N/A')
			>>> dd['key1'] = 'abc'
			>>> dd['key1'] # key1存在
			'abc'
			>>> dd['key2'] # key2不存在，返回默认值
			'N/A'
	2.4.OrderedDict:保持Key的顺序；
		(1).使用dict时，Key是无序的。在对dict做迭代时，我们无法确定Key的顺序
			>>> from collections import OrderedDict
			>>> d = dict([('a'， 1)， ('b'， 2)， ('c'， 3)])
			>>> d # dict的Key是无序的
			{'a': 1， 'c': 3， 'b': 2}
			>>> od = OrderedDict([('a'， 1)， ('b'， 2)， ('c'， 3)])
			>>> od # OrderedDict的Key是有序的
			OrderedDict([('a'， 1)， ('b'， 2)， ('c'， 3)])
		(2).OrderedDict的Key会按照插入的顺序排列，不是Key本身排序：
	2.5.Counter:简单的计数器
		>>> from collections import Counter
		>>> c = Counter()
		>>> for ch in 'programming':
		...     c[ch] = c[ch] + 1
		...
		>>> c
		Counter({'g': 2， 'm': 2， 'r': 2， 'a': 1， 'i': 1， 'o': 1， 'n': 1， 'p': 1})		
		Counter实际上也是dict的一个子类，上面的结果可以看出，字符'g'、'm'、'r'各出现了两次，其他字符各出现了一次

3.Base64:一种用64个字符来表示任意二进制数据的方法，一种最常见的二进制编码方法
	3.1.原理:
		(1).准备一个包含64个字符的数组:
			['A'， 'B'， 'C'， ... 'a'， 'b'， 'c'， ... '0'， '1'， ... '+'， '/']
		(2).对二进制数据进行处理，每3个字节一组，一共是3x8=24bit，划为4组，每组正好6个bit：
			得到4个数字作为索引，然后查表，获得相应的4个字符，就是编码后的字符串
		(3).Base64编码会把3字节的二进制数据编码为4字节的文本数据，长度增加33%
		(4).如果编码的二进制数据不是3的倍数，最后会剩下1个或2个字节怎么办？Base64用'\x00'字节在末尾补足后，
			再在编码的末尾加上1个或2个=号，表示补了多少字节，解码的时候，会自动去掉；
	3.2.python内置 base64 模块可以直接进行 base64 编解码
		>>> import base64
		>>> base64.b64encode(b'binary\x00string')
		b'YmluYXJ5AHN0cmluZw=='
		>>> base64.b64decode(b'YmluYXJ5AHN0cmluZw==')
		b'binary\x00string'
	3.3.由于标准的Base64编码后可能出现字符+和/，在URL中就不能直接作为参数，所以又有一种"url safe"的base64编码，
		其实就是把字符+和/分别变成-和_：
		>>> base64.b64encode(b'i\xb7\x1d\xfb\xef\xff')
		b'abcd++//'
		>>> base64.urlsafe_b64encode(b'i\xb7\x1d\xfb\xef\xff')
		b'abcd--__'
		>>> base64.urlsafe_b64decode('abcd--__')
		b'i\xb7\x1d\xfb\xef\xff'
	3.3.Base64是一种通过查表的编码方法，不能用于加密，即使使用自定义的编码表也不行
		Base64适用于小段内容的编码，比如数字证书签名、Cookie的内容等。
		由于=字符也可能出现在Base64编码中，但=用在URL、Cookie里面会造成歧义，所以，很多Base64编码后会把=去掉
		去掉=后怎么解码呢？因为Base64是把3个字节变为4个字节，所以，Base64编码的长度永远是4的倍数，
		因此，需要加上=把Base64字符串的长度变为4的倍数，就可以正常解码了
4.struct:Python没有专门处理字节的数据类型.但由于str既是字符串，又可以表示字节，所以，字节数组＝str
	Python提供了一个struct模块来解决 bytes 和其他二进制数据类型的转换
	(1).struct的 pack() 函数把任意数据类型变成 bytes
		>>> import struct			
		>>> struct.pack('>I'， 10240099) #
		b'\x00\x9c@c'
		pack的第一个参数是处理指令，'>I'的意思是：
			>表示字节顺序是big-endian，也就是网络序，I表示4字节无符号整数。
			后面的参数个数要和处理指令一致
	(2).unpack把 bytes 变成相应的数据类型：
		>>> struct.unpack('>IH'， b'\xf0\xf0\xf0\xf0\x80\x80')
		(4042322160， 32896)
		根据>IH的说明，后面的bytes依次变为I：4字节无符号整数 和 H：2字节无符号整数。
	(3).使用struct分析 bmp 图片:
		BMP格式采用小端方式存储数据，文件头的结构按顺序如下：
		b'BM\xae\x07\x12\x00\x00\x00\x00\x006\x00\x00\x00(\x00\x00\x00\xd6\x02\x00\x00\x1e\x02\x00\x00\x01\x00\x18\x00'
		两个字节：'BM'表示Windows位图，'BA'表示OS/2位图；
		一个4字节整数：表示位图大小；
		一个4字节整数：保留位，始终为0；
		一个4字节整数：实际图像的偏移量；
		一个4字节整数：Header的字节数；
		一个4字节整数：图像宽度；
		一个4字节整数：图像高度；
		一个2字节整数：始终为1；
		一个2字节整数：颜色数。
		>>> struct.unpack('<ccIIIIIIHH'， s)
		(b'B'， b'M'， 691256， 0， 54， 40， 640， 360， 1， 24)
										宽    高     颜色数
		??? 请编写一个bmpinfo.py，可以检查任意文件是否是位图文件，如果是，打印出图片大小和颜色数。
			from io import BytesIO
			import struct
			try:
			    f = open('F:\demo.bmp'， 'rb')；
			    s = f.read(30)
			    print(s)
			    result = struct.unpack('<ccIIIIIIHH'， s)
			    if result[0] == b'B' and result[1] == b'M':
			        print(result[6]， '*'， result[7]，' '， result[9]， 'bit')
			    else:
			        print(filename， ' is not bmp file')
			except BaseException:
			    pass
			finally:
			    if f:
			        f.close()
5.hashlib:提供了常见的摘要算法，如MD5，SHA1等等
	(1).摘要算法又称哈希算法、散列算法.它通过一个函数，把任意长度的数据转换为一个长度固定的数据串(通常用16进制的字符串表示).
		摘要算法就是通过摘要函数f()对任意长度的数据 data 计算出固定长度的摘要digest，目的是为了发现原始数据是否被人篡改过
		摘要算法之所以能指出数据是否被篡改过，就是因为摘要函数是一个单向函数，计算f(data)很容易，但通过digest反推data却非常困难
	(2).MD5是最常见的摘要算法，速度很快，生成结果是固定的128 bit字节，通常用一个32位的16进制字符串表示:
		>>> import hashlib
		>>> md5 = hashlib.md5()
		>>> md5.update('chenlanqing'.encode('utf-8'))
		>>> md5.hexdigest()
		'0ff478d6c1807f791e4b7683c9366cf6'
		如果数据量很大，可以分块多次调用update()，最后计算的结果是一样的；
	(3).另一种常见的摘要算法是SHA1，调用SHA1和调用MD5完全类似:SHA1的结果是160 bit字节，通常用一个40位的16进制字符串表示。
		>>> import hashlib
		>>> sha1 = hashlib.sha1()
		>>> sha1.update('how to use sha1 in '.encode('utf-8'))
		>>> sha1.update('python hashlib?'.encode('utf-8'))
		>>> print(sha1.hexdigest())
		比SHA1更安全的算法是SHA256和SHA512，不过越安全的算法不仅越慢，而且摘要长度更长
	(4).摘要算法应用:
		将口令存储为摘要
	(5).一些方法与属性:
		①.hashlib.algorithms_guaranteed ==> 支持hash算法的集合
		②.hashlib.algorithms_available 
		③.md5.digest_size ==> The size of the resulting hash in bytes.
		④.md5.block_size ==> The internal block size of the hash algorithm in bytes.
		⑤.md5.name
6.itertools:操作迭代对象的函数:itertools模块提供的全部是处理迭代功能的函数，它们的返回值不是 list，
	而是 Iterator，只有用 for 循环迭代的时候才真正计算
	6.1.几个"无限"迭代器，无限序列只有在for迭代时才会无限地迭代下去，如果只是创建了一个迭代对象，
		它不会事先把无限个元素生成出来，事实上也不可能在内存中创建无限
		无限序列虽然可以无限迭代下去，但是通常我们会通过 takewhile() 等函数根据条件判断来截取出一个有限的序列
		(1).count():创建一个无限迭代器，下述代码会无限打印自然数
			>>> import itertools
			>>> natuals = itertools.count(1)
			>>> for n in natuals:
			...     print(n)	
		(2)cycle():会把传入的一个序列无限重复下去
			>>> import itertools
			>>> cs = itertools.cycle('ABC') # 注意字符串也是序列的一种
			>>> for c in cs:
			...     print(c)
		(3).repeat():负责把一个元素无限重复下去，不过如果提供第二个参数就可以限定重复次数
			>>> ns = itertools.repeat('A'， 3)
			>>> for n in ns:
			...     print(n)
		(4).takewhile():根据条件判断来截取出一个有限的序列
			>>> natuals = itertools.count(1)
			>>> ns = itertools.takewhile(lambda x: x <= 10， natuals)
			>>> list(ns)
			[1， 2， 3， 4， 5， 6， 7， 8， 9， 10]
	6.2.迭代器操作函数:
		(1).chain():把一组迭代对象串联起来，形成一个更大的迭代器
			>>> for c in itertools.chain('ABC'， 'XYZ'):
			...     print(c)
			# 迭代效果：'A' 'B' 'C' 'X' 'Y' 'Z'
		(2).groupby():把迭代器中"相邻的重复元素"挑出来放在一起
			>>> for key， group in itertools.groupby('AAABBBCCAAA'):
			...     print(key， list(group))
			...
			A ['A'， 'A'， 'A']
			B ['B'， 'B'， 'B']
			C ['C'， 'C']
			A ['A'， 'A'， 'A']
			实际上挑选规则是通过函数完成的，只要作用于函数的两个元素返回的值相等，这两个元素就被认为是在一组的，
			而函数返回值作为组的key。如果我们要忽略大小写分组，就可以让元素'A'和'a'都返回相同的key
			>>> for key， group in itertools.groupby('AaaBBbcCAAa'， lambda c: c.upper()):
			...     print(key， list(group))
			...
			A ['A'， 'a'， 'a']
			B ['B'， 'B'， 'b']
			C ['c'， 'C']
			A ['A'， 'A'， 'a']

7.XML解析:DOM和SAX
	7.1.DOM会把整个XML读入内存，解析为树，因此占用内存大，解析慢，优点是可以任意遍历树的节点。
		SAX是流模式，边读边解析，占用内存小，解析快，缺点是我们需要自己处理事件
	7.2.在Python中使用SAX解析XML非常简洁，通常我们关心的事件是start_element，end_element和char_data，准备好这3个函数，
		然后就可以解析xml了:
		举个例子，当SAX解析器读到一个节点时：
		<a href="/">python</a>
		会产生3个事件：
			start_element事件，在读取<a href="/">时；
			char_data事件，在读取python；	
			end_element事件，在读取</a>时；
		from xml.parsers.expat import ParserCreate
		class DefaultSaxHandler(object):
		    def start_element(self， name， attrs):
		        print('*********************'，type(attrs))
		        print('sax:start_element:%s， attrs:%s' %(name，str(attrs)))
		    
		    def end_element(self， name):
		        print('sax:end_element: %s' % name)

		    def char_data(self， text):
		        print('sax:char_data: %s' %text)
8.HTMLParser:
	8.1.Python提供了HTMLParser来非常方便地解析HTML
		from html.parser import HTMLParser
		from html.entities import name2codepoint
		class MyHtmlParser(HTMLParser):
		    def handle_starttag(self， tag， attrs):
		        print('<%s>' % tag)
		    def handle_endtag(self， tag):
		        print('</%s>' % tag)
		    def handle_startendtag(self， tag， attrs):
		        print('<%s/>' % tag)
		    def handle_data(self， data):
		        print(data)
		    def handle_comment(self， data):
		        print('<!--'， data， '-->')       
		    def handle_entityref(self， name):
		        print('&%s；' % name)
		    def handle_charref(self， name):
		        print('&#%s；' % name)
		parser = MyHtmlParser()
		parser.feed('''<html>
		<head><head>
		<body>
		    <!-- test html parser -->
		    <p>Some <a href=\"#\">html</a> HTML&nbsp；tutorial...<br>END</p>
		</body>
		</html>
		''')；
	8.2.feed()方法可以多次调用，也就是不一定一次把整个HTML字符串都塞进去，可以一部分一部分塞进去。
		特殊字符有两种，一种是英文表示的&nbsp；，一种是数字表示的&#1234；，这两种字符都可以通过Parser解析出来
	8.3.练习:
		找一个网页，例如https://www.python.org/events/python-events/，用浏览器查看源码并复制，然后尝试解析一下HTML，
		输出Python官网发布的会议时间、名称和地点
9.urllib:提供了一系列用于操作URL的功能
	9.1.Get: urllib的 request 模块可以非常方便地抓取 URL 内容，也就是发送一个GET请求到指定的页面，然后返回HTTP的响应
		from urllib import request
		with request.urlopen('https://www.baidu.com/index.php?tn=98012088_5_dg&ch=10') as f:
		    data = f.read()
		    print('Status:'， f.status， f.reason)
		    for k， v in f.getheaders():
		        print('%s %s ' % (k， v))

		    print('Data:'， data.decode('utf-8'))
		(1).模拟浏览器发送GET请求，就需要使用Request对象，通过往Request对象添加HTTP头，我们就可以把请求伪装成浏览器	
		模拟iPhone6:
		req.add_header('User-Agent'， '''Mozilla/6.0 (iPhone； CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/536.26 
			(KHTML， like Gecko) Version/8.0 Mobile/10A5376e Safari/8536.25''')
	9.2.Post: 如果要以POST发送一个请求，只需要把参数data以bytes形式传入

	9.3.Handler:如果还需要更复杂的控制，比如通过一个Proxy去访问网站，我们需要利用ProxyHandler来处理
		proxy_handler = urllib.request.ProxyHandler({'http': 'http://www.example.com:3128/'})
		proxy_auth_handler = urllib.request.ProxyBasicAuthHandler()
		proxy_auth_handler.add_password('realm'， 'host'， 'username'， 'password')
		opener = urllib.request.build_opener(proxy_handler， proxy_auth_handler)
		with opener.open('http://www.example.com/login.html') as f:
		    pass









