<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1.order  by  中  null  的处理方法:](#1order--by--%E4%B8%AD--null--%E7%9A%84%E5%A4%84%E7%90%86%E6%96%B9%E6%B3%95)
- [2.case  when  的用法:](#2case--when--%E7%9A%84%E7%94%A8%E6%B3%95)
- [3.Oracle中字符问题:](#3oracle%E4%B8%AD%E5%AD%97%E7%AC%A6%E9%97%AE%E9%A2%98)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 1.order  by  中  null  的处理方法:
	(1).默认处理:
		oracle中  order  by  时默认认为  null  为最大值,所以如果  asc  升序后则排在最后,desc  降序则排在最前面
	(2).使用  nvl  函数:
		nvl  函数可以将输入参数为空时转换为一特定值
		NVL  (expr1,  expr2):expr1为  NULL,返回expr2;不为  NULL,返回  expr1.  注意两者的类型要一致  
		NVL2  (expr1,  expr2,  expr3):xpr1不为  NULL.返回expr2;为  NULL,返回expr3.expr2和expr3类型不同的话,expr3会转换为expr2的类型  
		NULLIF  (expr1,  expr2):相等返回  NULL,不等返回expr1  
	(3).使用  decode  函数:
		decode函数比nvl函数更强大,同样它也可以将输入参数为空时转换为一特定值
		如:
		decode(employee_name,null,’张三’,  employee_name)表示当employee_name为空时则返回’张三’，
		如果不为空则返回employee_name通过这个函数可以定制null的排序位置
	(4).使用  case  语法
		Case语法是Oracle  9i后开始支持的，是一个比较灵活的语法，同样在排序中也可以应用如:
		select  *  from  employee  order  by    (case  employee_name  when  null  then  '张三'  else  employee_name  end)
	  	表示当employee_name为空时则返回’张三’，如果不为空则返回employee_name通过case语法同样可以定制null的排序位置
	(5).使用  nulls  first  或者  nulls  last  语法:
		nulls  first:表示null值的记录将排在最前,不管是  asc  还是  desc
		nulls  last:表示null值的记录将排在最后,不管是  asc  还是  desc
## 2.case  when  的用法: 
    *http://exceptioneye.iteye.com/blog/1197329
	基本用法:
	--简单Case函数    
	CASE  sex    
	WHEN  '1'  THEN  '男'    
	WHEN  '2'  THEN  '女'    
	ELSE  '其他'  END  

	--Case搜索函数    
	CASE
	WHEN  sex  =  '1'  THEN  '男'    
	WHEN  sex  =  '2'  THEN  '女'    
	ELSE  '其他'  END  

	2.1.SELECT  CASE  WHEN  用法:完成不同条件的分组
		SELECT    grade,  
		COUNT(CASE  WHEN  sex  =  1  THEN  1  ELSE  NULL  END)  男生数,
		COUNT(CASE  WHEN  sex  =  2  THEN  1  ELSE  NULL  END)  女生数
		FROM  students  GROUP  BY  grade;

	2.2.WHERE  CASE  WHEN  用法:
		SELECT  T2.*,  T1.*  FROM  T1,  T2
		WHERE  (CASE  WHEN  T2.COMPARE_TYPE  =  'A'  AND  T1.SOME_TYPE  LIKE  'NOTHING%'  THEN  1
	                        WHEN  T2.COMPARE_TYPE  !=  'A'  AND  T1.SOME_TYPE  NOT  LIKE  'NOTHING%'  THEN  1  ELSE  0  END)  =  1
	2.3.GROUP  BY  CASE  WHEN  用法:用这个方法来判断工资的等级,并统计每一等级的人数
		SELECT    
		CASE  WHEN  salary  <=  500  THEN  '1'    
		WHEN  salary  >  500  AND  salary  <=  600    THEN  '2'    
		WHEN  salary  >  600  AND  salary  <=  800    THEN  '3'    
		WHEN  salary  >  800  AND  salary  <=  1000  THEN  '4'    
		ELSE  NULL  END  salary_class,  --  别名命名
		COUNT(*)    
		FROM  Tab
## 3.Oracle中字符问题:
	3.1.查看oracle的字符集: select userenv('language') from dual
	3.2.varchar2 最大是4000字节,如果字符集是16位编码,那么每个字符16位,即2个字节,所以可以容纳2000字符;
		如果是32位编码的字符集,那么只能存储1000个字符
	3.3.nvarchar2最大长度是4000字符,也可以存储4000个汉字



