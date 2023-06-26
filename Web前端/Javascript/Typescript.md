# 1、概述

TypeScript是带有类型语法的JavaScript

如何安装typescript 编译器：
```
npm install -g typescript
npm install -g ts-node
```

# 2、基本语法

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
