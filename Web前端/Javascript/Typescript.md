# 1、概述

## 1.3、基本概述

TypeScript是带有类型语法的JavaScript

如何安装typescript 编译器：
```
npm install -g typescript
npm install -g ts-node
```

为什么是Typescript:
- typescript 增加了类型系统以避免JavaScript动态类型的许多问题；
- TypeScript实现了JavaScript的未来功能

## 1.2、typescript环境配置

- [how to setup typescript](https://www.typescripttutorial.net/typescript-tutorial/setup-typescript/)
- [tsc CLI Options](https://www.typescriptlang.org/docs/handbook/compiler-options.html)

# 2、类型

在typescript：
- 类型是描述一个值的不同属性和方法的标签。
- 每个值都有一个类型

Typescript继承了JavaScript的内置类型，包含：
- 原始类型
- 对象类型

## 2.1、原始类型

Name	|Description
--------|--------
string	| 字符串类型
number	| 数字类型
boolean	| 布尔，true/false
null	| null
undefined	| 它是一个未初始化的变量的默认值
symbol	| 唯一的常量值

## 2.2、对象类型

对象类型是指函数、数组、类等，或创建的自定义对象类型。

## 2.3、类型注解

TypeScript使用类型注解来明确指定变量、函数、对象等标识符的类型。

TypeScript在标识符后使用语法`:type`作为类型注解，其中type可以是任何有效的类型；一旦一个标识符被注解了一个类型，它就只能作为该类型使用。如果标识符被用作不同的类型，TypeScript编译器将发出一个错误
```ts
// 基本类型
let name: string = 'John';
let age: number = 25;
let active: boolean = true;
// 数组
let names: string[] = ['John', 'Jane', 'Peter', 'David', 'Mary'];
// 对象
let person: {
   name: string;
   age: number
};
// 函数
function increment(counter: number) : number {
    return counter++;
}
```

## 2.4、类型推断

```ts
// items 类型是： number[]
let items = [1, 2, 3, null];
// items 类型是： (number | string)[]
let items = [0, 1, null, 'Hi'];
```
上下文语境推断，其会根据语义的上下文来推断出对应的类型

## 2.5、Enum

定义Enum
```ts
enum name {constant1, constant2, ...};
// 比如：
enum Month {
    Jan,
    Feb,
    Mar,
    Apr,
    May,
    Jun,
    Jul,
    Aug,
    Sep,
    Oct,
    Nov,
    Dec
};
```
上面Month生成的JavaScript代码：
```js
var Month;
(function (Month) {
    Month[Month["Jan"] = 0] = "Jan";
    Month[Month["Feb"] = 1] = "Feb";
    Month[Month["Mar"] = 2] = "Mar";
    Month[Month["Apr"] = 3] = "Apr";
    Month[Month["May"] = 4] = "May";
    Month[Month["Jun"] = 5] = "Jun";
    Month[Month["Jul"] = 6] = "Jul";
    Month[Month["Aug"] = 7] = "Aug";
    Month[Month["Sep"] = 8] = "Sep";
    Month[Month["Oct"] = 9] = "Oct";
    Month[Month["Nov"] = 10] = "Nov";
    Month[Month["Dec"] = 11] = "Dec";
})(Month || (Month = {}));
```
上面0表示一月，我们可以指定枚举成员的数字：
```ts
enum Month {
    Jan = 1,
    Feb,
    Mar,
    Apr,
    May,
    Jun,
    Jul,
    Aug,
    Sep,
    Oct,
    Nov,
    Dec
};
```
编译ts为JavaScript代码后：
```js
var Month;
(function (Month) {
    Month[Month["Jan"] = 1] = "Jan";
    Month[Month["Feb"] = 2] = "Feb";
    Month[Month["Mar"] = 3] = "Mar";
    Month[Month["Apr"] = 4] = "Apr";
    Month[Month["May"] = 5] = "May";
    Month[Month["Jun"] = 6] = "Jun";
    Month[Month["Jul"] = 7] = "Jul";
    Month[Month["Aug"] = 8] = "Aug";
    Month[Month["Sep"] = 9] = "Sep";
    Month[Month["Oct"] = 10] = "Oct";
    Month[Month["Nov"] = 11] = "Nov";
    Month[Month["Dec"] = 12] = "Dec";
})(Month || (Month = {}));
;
```

## 2.6、Union Type

TypeScript联合类型允许你在一个变量中存储一个或多个类型的值，比如：
```ts
function add(a: number | string, b: number | string) {
    if (typeof a === 'number' && typeof b === 'number') {
        return a + b;
    }
    if (typeof a === 'string' && typeof b === 'string') {
        return a.concat(b);
    }
    throw new Error('Parameters must be numbers or strings');
}
```

## 2.7、类型别名

类型别名允许你为一个现有的类型创建一个新的名称，比如：
```ts
type alphanumeric = string | number;
let input: alphanumeric;
input = 100; // valid
input = 'Hi'; // valid
input = false; // Compiler error
```

# 3、函数

基本写法：
```ts
function name(parameter: type, parameter:type,...): returnType {
   // do something
}
```

## 3.1、函数类型

一个函数类型有两个部分：参数和返回类型。在声明一个函数类型时，你需要用下面的语法指定这两个部分：
```ts
(parameter: type, parameter:type,...) => type
```
多种写法如下：
```ts
add = function (x: number, y: number) {
    return x + y;
};

let add: (a: number, b: number) => number =
    function (x: number, y: number) {
        return x + y;
    };
```

## 3.2、函数可选参数

```ts
// ? 表示参数可选
function multiply(a: number, b: number, c?: number): number {
    // 请注意，如果你使用表达式if(c)来检查一个参数是否没有被初始化，你会发现空字符串或零会被当作未定义
    if (typeof c !== 'undefined') {
        return a * b * c;
    }
    return a * b;
}
```
可选参数必须出现在参数列表中的必要参数之后，否则会报错：`error TS1016: A required parameter cannot follow an optional parameter.`

## 3.3、函数参数默认值

如果参数没有传值，可以指定默认值
```ts
// 语法
function name(parameter1:type=defaultvalue1, parameter2:type=defaultvalue2,...) {
}
// 示例
function applyDiscount(price: number, discount: number = 0.05): number {
    return price * (1 - discount);
}
```
**请注意，你不能在函数类型定义中包含默认参数**
```ts
let promotion: (price: number, discount: number = 0.05) => number;
// 报错信息
error TS2371: A parameter initializer is only allowed in a function or constructor implementation.
```

可选参数与默认参数
- 默认参数也是可选的。意味着，你可以在调用函数时省略默认参数
- 可选参数必须出现在必要参数之后。然而，默认参数不需要出现在必要参数之后；
- 当默认参数出现在一个必需参数之前时，你需要明确地传递undefined来获得默认的初始化值
    ```ts
    function add(a: number = 10, b: number): void {
        console.log(a, b);
    }
    // 调用上面的函数时必须显示传递 undefined，否则报错：Expected 2 arguments, but got 1
    add(undefined, 5);
    ```

## 3.4、rest parameters

rest parameters 允许你一个函数接受零个或多个指定类型的参数；类似Java的可选参数
- 函数只能有一个 rest parameters；
- rest parameters 一个函数只能有一个；
- rest parameters 类型是一个数组类型；

```ts
// 语法
function fn(...rest: type[]) {
   //...
}
```

## 3.5、函数重载

请注意，TypeScript的函数重载与其他静态类型语言（如C#和Java）所支持的函数重载不同。
```ts
function add(a: number, b: number): number;
function add(a: string, b: string): string;
function add(a: any, b: any): any {
   return a + b;
}
```
在这个例子中，我们给add()函数添加了两个重载：第一个重载告诉编译器，当参数是数字时，add()函数应该返回一个数字。第二个重载做了同样的事情，但是是针对字符串；

当你重载一个函数时，所需参数的数量必须是相同的。如果一个重载的参数比另一个多，你必须把额外的参数变成可选的，否则会报错
```ts
function sum(a: number, b: number): number;
function sum(a: number, b: number, c: number): number;
function sum(a: number, b: number, c?: number): number {
    if (c) return a + b + c;
    return a + b;
}
```

**方法重载**：当一个函数是一个类的属性时，它被称为一个方法。TypeScript也支持方法重载
```ts
class Counter {
    private current: number = 0;
    count(): number;
    count(target: number): number[];
    count(target?: number): number | number[] {
        if (target) {
            let values = [];
            for (let start = this.current; start <= target; start++) {
                values.push(start);
            }
            this.current = target;
            return values;
        }
        return ++this.current;
    }
}
```

# 扩展

## Optional实现

```ts
interface Article {
  title: String;
  content: String;
  author: String;
  date: Date;
  readCount: Number;
}
// Omit 表示将 K 里面的字段从 T 类型中排除；
// Pick 表示取 T 类型中的 K 字段；
// Paritial 表示将 Pick 中的取到变为可选；
// & 表示两个类型合并
type Optional<T, K extends keyof T> = Omit<T, K> & Partial<Pick<T, K>>;

type CreateArticle = Optional<Article, "author" | "date" | "readCount">;

function createArticle(article: CreateArticle) {}
```

# 参考资料

- [TypeScript is JavaScript with syntax for types](https://www.typescriptlang.org/)
- [typescript tutorial](https://www.typescripttutorial.net/)
- [typescript road map](https://roadmap.sh/typescript)
