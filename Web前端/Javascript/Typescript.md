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

联合类型描述了一个值可以是几种类型中的一种，TypeScript联合类型允许你在一个变量中存储一个或多个类型的值，比如：
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

## 2.8、never

never类型是一种不包含任何值的类型。正因为如此，你不能给一个never类型的变量分配任何值

通常情况下，你用never类型来表示一个总是抛出错误的函数的返回类型。比如说：
```ts
function raiseError(message: string): never {
    throw new Error(message);
}
```

## 2.9、keyof

TypeScript允许我们遍历某种类型的属性，并通过keyof操作符提取其属性的名称。

keyof操作符可以用于获取某种类型的所有键，其返回类型是联合类型

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

# 4、类

Typescript的类中类是的属性和方法都包含了类型注解；

## 4.1、访问修饰符

class包含了3个访问修饰符：
- private：只能在同一个类中访问
    ```ts
    class Person {
        private ssn: string;
        private firstName: string;
        private lastName: string;
        // ...
    }
    ```
- protected：同一个类或子类中可以使用
- public：默认修饰符，如果没有指定修饰符，默认就是 public

类可以按照如下方式定义：
```ts
class Person {
  // ssn: string;
  // private firstName: string;
  // private lastName: string;
  constructor(protected ssn: string, private firstName: string, private lastName: string) {
    this.ssn = ssn;
    this.firstName = firstName;
    this.lastName = lastName;
  }
  getFullName(): string {
    return `${this.firstName} ${this.lastName}`;
  }
}
```

## 4.2、只读属性

定义只读属性（同Java final字段含义）
```ts
class Person {
    readonly birthDate: Date;
    constructor(birthDate: Date) {
        this.birthDate = birthDate;
    }
}
// 或者
class Person {
    constructor(readonly birthDate: Date) {
        this.birthDate = birthDate;
    }
}
```
只读属性必须初始化，一般使用构造函数来进行初始化；

## 4.3、getter和setter

```ts
class Person {
    private _age: number;
    private _firstName: string;
    private _lastName: string;
    public get age() {
        return this._age;
    }
    public set age(theAge: number) {
        if (theAge <= 0 || theAge >= 200) {
            throw new Error('The age is invalid');
        }
        this._age = theAge;
    }
    public getFullName(): string {
        return `${this._firstName} ${this._lastName}`;
    }
}
```
调用时可以直接使用：
```ts
const person = new Person(18, "John", "Doe");
console.log(person.age)
person.age = 100;
```

## 4.4、接口

大致用法同Java，但是在TypeScript允许一个接口扩展一个类。在这种情况下，接口继承了类的属性和方法。另外，接口可以继承类的私有和受保护的成员，而不仅仅是公共成员。
意味着当一个接口扩展了一个具有私有或保护成员的类时，该接口只能由该类或该类的子类实现，而该接口是从该类中扩展出来的；通过这样做，你将该接口的使用限制在该接口所继承的类或子类中。如果你试图从一个不是该接口所继承的类的子类中实现该接口，compile error
```ts
class Control {
    private state: boolean;
}
interface StatefulControl extends Control {
    enable(): void
}
class Button extends Control implements StatefulControl {
    enable() { }
}
class TextBox extends Control implements StatefulControl {
    enable() { }
}
class Label extends Control { }
// Error: cannot implement
class Chart implements StatefulControl {
    enable() { }
}
```

# 5、高级类型

## 5.1、交叉类型

交叉类型通过结合多个现有类型创建一个新的类型。新类型具有现有类型的所有特征。使用 `&` 来实现交叉类型
```ts
// 语法如下：typeAB 有包含typeA 和 typeB 的所有属性
type typeAB = typeA & typeB;
```
注意：如果 typeA 和 typeB 有相同属性名称，他们的类型必须一直，否则会报错

**与 Union Type的区别：** 注意，联合类型使用了 `|` 操作符，该操作符定义了一个变量，该变量可以持有类型A或类型B的值。
- 在使用交叉类型时，一个值必须同时具备所有交叉类型中的特性，即满足所有类型的要求；
- 在使用联合类型时，一个值只需要满足其中一个类型的要求即可；
```ts
interface BusinessPartner {
    name: string;
    credit: number;
}
interface Identity {
    id: number;
    name: string;
}
// 交叉类型
type interactionType = BusinessPartner & Identity; 
// interactionTypeInstance 必须要包含 BusinessPartner 和 Identity 所有属性
const interactionTypeInstance: interactionType = {
  name: "",
  credit: 123,
  id: 456
};
// 联合类型
type unionType = BusinessPartner | Identity;
// unionTypeInstance 只需要包含 BusinessPartner 或者 Identity 的其中一个的所有属性 
const unionTypeInstance: unionType = {
  name: "1234",
  credit: 456,
  id : 10
};
// 如下代码会报错: Property 'id' does not exist on type 'BusinessPartner'
// unionType 是要么满足 BusinessPartner，要么满足 Identity
console.log(unionTypeInstance.id);
```
特殊情况：
```ts
// a 是字符串一个特殊情况，那么test1就是一个 a
type test1 = 'a' & string;
// 字符串不可能同时是 a 又是 b，test2是个 never
type test2 = 'a' & 'b';
// 有效的
type test3 = 'a' | 'b';
// 
type test4 = 'a' | 'b' | 1 & string; 
// 等价于于
type test5 = ('a' & string) | ('b' & string) | (1 & string)
```

## 5.2、类型守卫（Type Guard）

### 5.2.1、typeof

typeof操作符用于获取变量的类型，因此操作符后面接的始终是一个变量，`typeof variable === 'type'`是用来确定基本类型的惯用手法比如：
```ts
type alphanumeric = string | number;
function add(a: alphanumeric, b: alphanumeric) {
    if (typeof a === 'number' && typeof b === 'number') {
        return a + b;
    }
    if (typeof a === 'string' && typeof b === 'string') {
        return a.concat(b);
    }
    throw new Error('Invalid arguments. Both arguments must be either numbers or strings.');
}
```
并且typename只能是number、string、boolean或symbol，因为其余的typeof检测结果不那么可靠，typeof 任何对象返回都是 object


### 5.2.2、instanceof

判断一个变量的类型，常常会用到 typeof 运算符，但当用 typeof 来判断引用类型变量时，无论是什么类型的变量，它都会返回 Object；

instanceof 操作符用于检测对象是否属于某个 class，同时，检测过程中也会将继承关系考虑在内；

instanceof 与 typeof 相比，instanceof 方法要求开发者明确的确认对象为某特定类型。即 instanceof 用于判断引用类型属于哪个构造函数的方法；

- 类的类型：typeof className
- 类实例的类型：typeof className.prototype或者className

```ts
class Customer {
    isCreditAllowed(): boolean {
        return true;
    }
}
class Supplier {
    isInShortList(): boolean {
        return true;
    }
}
type BusinessPartner = Customer | Supplier;

function signContract(partner: BusinessPartner) : string {
    let message: string;
    if (partner instanceof Customer) {
        message = partner.isCreditAllowed() ? 'Sign a new contract with the customer' : 'Credit issue';
    }
    if (partner instanceof Supplier) {
        message = partner.isInShortList() ? 'Sign a new contract the supplier' : 'Need to evaluate further';
    }
    return message;
}
```

### 5.2.3、in

in操作符对对象上的一个属性的存在进行安全检查
```ts
function signContract(partner: BusinessPartner) : string {
    let message: string;
    if ('isCreditAllowed' in partner) {
        message = partner.isCreditAllowed() ? 'Sign a new contract with the customer' : 'Credit issue';
    } else {
        // must be Supplier
        message = partner.isInShortList() ? 'Sign a new contract the supplier ' : 'Need to evaluate further';
    }
    return message;
}
```

### 5.2.4、自定义type guard

一个用户定义的类型保护函数是一个简单返回arg是aType的函数：
```ts
function isCustomer(partner: any): partner is Customer {
    return partner instanceof Customer;
}
```

## 5.3、类型强转

type casting：将一个变量从一个类型转换为另一个类型，主要有两种方式：

**as**：
```ts
let input = document.querySelector('input[type="text"]') as HTMLInputElement;
let el: HTMLElement;
el = new HTMLInputElement(); // HTMLInputElement 是 HTMLElement子类

let a: typeA;
let b = a as typeB;
```

**<>**：
```ts
let input = <HTMLInputElement>document.querySelector('input[type="text"]');
let a: typeA;
let b = <typeB>a;
```

## 5.4、类型断言

type Assertions 指示TypeScript编译器将一个值作为一个指定的类型。它使用as关键字来做到这一点：
```ts
expression as targetType
```
比如如下例子：
```ts
function getNetPrice(price: number, discount: number, format: boolean): number | string {
    let netPrice = price * (1 - discount);
    return format ? `$${netPrice}` : netPrice;
}
let netPrice = getNetPrice(100, 0.05, true) as string;
console.log(netPrice);
let netPrice = getNetPrice(100, 0.05, false) as number;
console.log(netPrice);
```
请注意：一个类型断言并不带有任何类型转换。它只是告诉编译器，为了类型检查的目的，它应该把哪种类型应用于一个值

也可以使用如下方式：
```ts
<targetType> value
// 比如
let netPrice = <number>getNetPrice(100, 0.05, false);
```
注意在react等类库中不要使用`<>`，可能报错信息：
```
Cannot assign to read only property 'message' of object 'SyntaxError: /src/01_base_type/05_advanced_type.tsx: Unterminated JSX contents. (25:24)
```

# 6、泛型

## 6.1、基本介绍

```ts
function getRandomElement<T>(items: T[]): T {
    let randomIndex = Math.floor(Math.random() * items.length);
    return items[randomIndex];
}
```
调用泛型函数：
```ts
let numbers = [1, 5, 7, 4, 2, 9];
let randomEle = getRandomElement<number>(numbers); 
// 或者直接使用
let randomEle = getRandomElement(numbers); 
```
泛型包含多个类型的：
```ts
function merge<U, V>(obj1: U, obj2: V) {
    return {
        ...obj1,
        ...obj2
    };
}
```
好处：
- 在编译时利用类型检查；
- 消除了类型转换；
- 允许你实现通用算法；

## 6.2、泛型约束

上面的merge例子，比如传如下参数：
```ts
let person = merge(
    { name: 'John' },
    25
);
console.log(person); // { name: 'John' }
```
typescript没有报错，为了限定类型，可以使用泛型约束的方式：
```ts
function merge<U extends object, V extends object>(obj1: U, obj2: V) {
    return {
        ...obj1,
        ...obj2
    };
}
// 按如下方式调用会报错，提示：Argument of type 'number' is not assignable to parameter of type 'object'.ts(2345)
let person = merge(
    { name: 'John' },
    25
);
```
因为现在merge这个函数只针对 object 类型有效了；

**参数泛型约束**：TypeScript允许你声明一个类型参数受到另一个类型参数的约束
```ts
// K 是 T  的属性
function prop<T, K extends keyof T>(obj: T, key: K) {
    return obj[key];
}
let str = prop({ name: 'John' }, 'name');
console.log(str); // Jhon
let str = prop({ name: 'John' }, 'age'); //报错：Argument of type '"age"' is not assignable to parameter of type '"name"'.
```

## 6.3、泛型类

```ts
class className<T>{
    //... 
}
class className<K,T>{
    //...
}
class className<T extends TypeA>{
    //...
}
```

## 6.4、泛型接口

描述索引类型的通用接口
```ts
interface Options<T> {
    [name: string]: T
}
let inputOptions: Options<boolean> = {
    'disabled': false,
    'visible': true
};
```

# 7、模块

## 7.1、导出模块

声明一个interface：
```ts
// Validator.ts
interface Validator {
    isValid(s: string): boolean
}
```
那么上面的 Validator 只能在 Validator.ts 内部使用，对外面来说其实一个private的，如果希望能被外部使用，需要使用 export：
```ts
export interface Validator {
    isValid(s: string): boolean
}
// 或者
interface Validator {
    isValid(s: string): boolean
}
export { Validator }; 
// 当然也可以重命名：
export { Validator as StringValidator };
```

## 7.2、导入模块

```ts
import { Validator } from './Validator';
class EmailValidator implements Validator {
    isValid(s: string): boolean {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(s);
    }
}
export { EmailValidator };

// 或者如下：
import { Validator as StringValidator } from './Validator';
class EmailValidator implements StringValidator {
    isValid(s: string): boolean {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(s);
    }
}
export { EmailValidator };
```

## 7.3、导入类型

```ts
// Types.ts
export type alphanumeric = string | number;
// import from Types.ts
import type {alphanumeric} from './Types';
```
注意，TypeScript从3.8版本开始支持导入类型语句。在TypeScript 3.8之前，你需要使用导入语句来代替
```ts
import {alphanumeric} from './Types';
```

## 7.4、导入一切

```ts
import * from 'module_name';
```

## 7.5、默认导出

TypeScript允许每个模块有一个 default export。要将一个 export 标记为 default export，你可以使用default关键字
```ts
import { Validator } from './Validator';
export default class ZipCodeValidator implements Validator {
    isValid(s: string): boolean {
        const numberRegexp = /^[0-9]+$/;
        return s.length === 5 && numberRegexp.test(s);
    }
}
```

# 8、Utility Types

- [Utility Types](https://www.typescriptlang.org/docs/handbook/utility-types.html)

TS 内置的 实用类型，用于类型转换

## 8.1、Awaited<Type>

该类型用于模拟async函数中的await操作，或者promise上的`.then()`方法——具体来说，就是递归展开promise的方式：
```ts
type A = Awaited<Promise<string>>;
//   ^? type A = string
type B = Awaited<Promise<Promise<number>>>;
//   ^? type b = nunber
type C = Awaited<boolean | Promise<number>>;
//   ^? type c = number | boolean
```

## 8.2、Partial<Type>

构造一个将type的所有属性设置为可选的类型。此实用程序将返回表示给定类型的所有子集的类型
```ts
interface Todo {
  title: string;
  description: string;
}
type TodoUpdate = Partial<Todo>;
// TodoUpdate 中 title 和 description 都是可选字段了
```

## 8.3、Required<Type>

构造由type set为required的所有属性组成的类型。Partial 的反义词。
```ts
interface Props {
  a?: number;
  b?: string;
}
const obj: Props = { a: 5 };
const obj2: Required<Props> = { a: 5 }; // Property 'b' is missing in type '{ a: number; }' but required in type 'Required<Props>'
```

## 8.4、Readonly<Type>

构造一个将type的所有属性设置为只读的类型，这意味着不能重新分配构造类型的属性
```ts
interface Todo {
  title: string;
}
const todo: Readonly<Todo> = {
  title: "Delete inactive users",
};
todo.title = "Hello"; // Cannot assign to 'title' because it is a read-only property.
```

## 8.5、Record<Keys, Type>

构造一个对象类型，其属性键为keys，属性值为type。此实用程序可用于将一个类型的属性映射到另一个类型
```ts
interface CatInfo {
  age: number;
  breed: string;
}
type CatName = "miffy" | "boris" | "mordred";
const cats: Record<CatName, CatInfo> = {
  miffy: { age: 10, breed: "Persian" },
  boris: { age: 5, breed: "Maine Coon" },
  mordred: { age: 16, breed: "British Shorthair" },
};
```

## 8.6、Pick<Type, Keys>

通过从type中选取一组属性Keys(字符串字面值或字符串字面值的并集)来构造一个类型
```ts
interface Todo {
  title: string;
  description: string;
  completed: boolean;
}
type TodoPreview = Pick<Todo, "title" | "completed">;
const todo: TodoPreview = {
  title: "Clean room",
  completed: false,
};
```

## 8.7、Omit<Type, Keys>

通过从type中选取所有属性然后移除Keys(字符串字面值或字符串字面值的并集)来构造类型。Pick的反义词。
```ts
interface Todo {
  title: string;
  description: string;
  completed: boolean;
  createdAt: number;
}
type TodoInfo = Omit<Todo, "completed" | "createdAt">;
const todoInfo: TodoInfo = {
  title: "Pick up kids",
  description: "Kindergarten closes at 5pm",
};
```

## 8.8、Exclude<UnionType, ExcludedMembers>

通过从UnionType中排除可分配给ExcludedMembers的所有联合成员来构造类型：
```ts
type T0 = Exclude<"a" | "b" | "c", "a">;
// type T0 = "b" | "c"
type T1 = Exclude<"a" | "b" | "c", "a" | "b">;
// type T1 = "c"
type T2 = Exclude<string | number | (() => void), Function>;
// type T2 = string | number
```

# 扩展

## 去除符号字段

```
string & keyof T
```

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
