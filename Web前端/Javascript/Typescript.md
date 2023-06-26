# 1、概述

TypeScript是带有类型语法的JavaScript

如何安装typescript 编译器：
```
npm install -g typescript
npm install -g ts-node
```

为什么是Typescript:
- typescript 增加了类型系统以避免JavaScript动态类型的许多问题；
- TypeScript实现了JavaScript的未来功能

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
