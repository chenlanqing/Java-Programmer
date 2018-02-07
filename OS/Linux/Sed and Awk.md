```
1. 把/etc/passwd 复制到/root/test.txt，用sed打印所有行；
	cp /etc/passwd test.txt		sed -n '1,$'p test.txt
2. 打印test.txt的3到10行；
	sed -n '3,10'p test.txt
3. 打印test.txt 中包含’root’的行；
	sed -n '/root/'p test.txt
4. 删除test.txt 的15行以及以后所有行；
	sed '15,$'d test.txt
5. 删除test.txt中包含’bash’的行；
	sed '/bash/'d test.txt
6. 替换test.txt 中’root’为’toor’；
	sed 's/root/toor/g' test.txt
7. 替换test.txt中’/sbin/nologin’为’/bin/login’
	sed 's/\/sbin\/nologin/\/bin\/login/g' test.txt
8. 删除test.txt中5到10行中所有的数字；
	sed '5,10s/[0-9]//g' test.txt
9. 删除test.txt 中所有特殊字符（除了数字以及大小写字母）；
	sed 's/[^0-9A-Z]//g' test.txt
10. 把test.txt中第一个单词和最后一个单词调换位置；

11. 把test.txt中出现的第一个数字和最后一个单词替换位置；

12. 把test.txt 中第一个数字移动到行末尾；

13. 在test.txt 20行到末行最前面加’aaa:’；

参考答案:
1. /bin/cp /etc/passwd /root/test.txt ; sed -n '1,$'p test.txt

2. sed -n '3,10'p test.txt

3. sed -n '/root/'p test.txt

4. sed '15,$'d test.txt

5. sed '/bash/'d test.txt

6. sed 's/root/toor/g' test.txt

7. sed 's#sbin/nologin#bin/login#g' test.txt

8. sed '5,10s/[0-9]//g' test.txt

9. sed 's/[^0-9a-zA-Z]//g' test.txt

10. sed 's/\(^[a-zA-Z][a-zA-Z]*\)\([^a-zA-Z].*\)\([^a-zA-Z]\)\([a-zA-Z][a-zA-Z]*$\)/\4\2\3\1/' test.txt

11. sed 's#\([^0-9][^0-9]*\)\([0-9][0-9]*\)\([^0-9].*\)\([^a-zA-Z]\)\([a-zA-Z][a-zA-Z]*$\)#\1\5\3\4\2#' test.txt

12. sed 's#\([^0-9][^0-9]*\)\([0-9][0-9]*\)\([^0-9].*$\)#\1\3\2#' test.txt

13. sed '20,$s/^.*$/aaa:&/' test.txt
```