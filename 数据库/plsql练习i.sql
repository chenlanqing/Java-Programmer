PL/SQL练习:
一.块编程
1.输入一个员工号,输出该员工的姓名、薪金和大概的服set serveroutput on
declare
v_ename emp.ename%type;
v_sal emp.sal%type;
v_during number;
begin
select ename,sal,ceil(months_between(sysdate,hiredate)/12) into v_ename,v_sal,v_during from emp where empno=&empno;
dbms_output.put_line('用户名:'||v_ename||',薪水为：'||v_sal||',服务年限:'||v_during);
end;
/ 
2.接收一个员工号,输出该员工所在的部门的名称
set serveroutput on
declare
v_dname dept.dname%type;
begin
select dname into v_dname from dept where deptno=(select deptno from emp where empno=&empno);
dbms_output.put_line('该员工所在部门:'||v_dname);
end;
/ 
3.接收一个部门号,如果该员工的职位是MANAGER,并且在DALLAS工作,那么就给他薪金加15%;
	如果该员工职位是CLERK,并且在NEW YORK工作就给他薪金扣除5%;
set serveroutput on
declare
v_job emp.job%type;
v_loc dept.loc%type;
begin
select job,loc into v_job,v_loc from (select * from emp join dept using(deptno)) where empno=&empno;
if v_job='MANAGER' and v_loc='DALLAS' then
update emp set sal=sal*1.15 where job='MANAGER' and deptno=(select deptno from dept where loc='DALLAS');
elsif v_job='CLERK' and v_loc='NEW YORK' then
update emp set sal=sal*0.95 where job='CLERK' and deptno=(select deptno from dept where loc='NEW YORK');
end if;
--dbms_output.put_line('该员工所在部门:'||v_dname);
end;
/ 
4.接收一个员工号,输出这个员工所在部门的平均工资
set serveroutput on
declare
v_avg_sal number(7,2);
begin
select avg(sal) into v_avg_sal from emp where deptno=(select deptno from emp where empno=&empno) group by deptno;
dbms_output.put_line('该员工所在部门平均工资:'||v_avg_sal);
end;
/ 

5.以交互的方式给部门表插入一条记录,如果出现主键冲突异常,请显示部门号已被占用的字样

二.过程或函数
6.建立一个存储过程来接收一个员工号,返回他的工资和他所在部门的平均工资并作为传出参数传出;

7.建立一个存储过程来接收一个部门号,找出其他的两位最老的员工的员工号,并打印

8.编写一个过程,用来传入一个员工号,在emp表中删除一个员工,当该员工是该部门的最后一个员工时,
	就在dept表中删除该员工所在的部门;















