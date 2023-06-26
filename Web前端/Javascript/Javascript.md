
# 数组

数组的主要方法：

方法 | 含义
-----|-----
Array.push(element) | 数组末尾添加元素
Array.unshift(element) | 数组头部添加元素
Array.pop() | 删除末尾的元素，并返回被删除的元素
Array.shift() | 删除头部的元素，并返回被删除的元素
[Array.splice(position, num)](https://www.javascripttutorial.net/javascript-array-splice/) | 删除指定位置数据，其中position 表示指定要删除的第一个元素的位置，后面 num 要删除的元素个数；该方法会修改原始数组，并将被删除的元素作为一个数组返回；
Array.splice(position,0,new_element_1,new_element_2,...) | 指定位置插入元素，position表示新元素插入的起始位置，第二个参数是0表示不删除数组任何元素，后面的参数表示需要插入的元素，返回一个空数组
Array.splice(position, num,new_element_1,new_element_2,... ) | num > 0 表示要删除position位置的元素
[Array.slice(start, stop)](https://www.javascripttutorial.net/javascript-array-slice/) | 获取数组的子集，从 [start , stop-1]，如果缺省， start默认是从0开始，stop默认是数组的长度
[Array.map(callback)](https://www.javascripttutorial.net/javascript-array-map/) | 将一个数组通过某些条件，转换为另外一个数组；对数组中的每个元素都调用一个 callback，并返回一个包含结果的新数组。callback可以使用箭头函数
[Array.filter(callback)](https://www.javascripttutorial.net/javascript-array-filter/) | 数组中过滤数据
[Array.reduce(callback)](https://www.javascripttutorial.net/javascript-array-reduce/) | 将一个数组转换为一个值


# 参考资料

- [javascript tutorial](https://www.javascripttutorial.net/)


