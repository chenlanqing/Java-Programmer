# 一、数据验证

## 1、[Pydantic](https://docs.pydantic.dev/latest/)

Pydantic 利用 Python 3.6+ 引入的类型提示 (Type Hints)，在运行时对数据进行校验、转换和解析。你只需通过纯粹、规范的 Python 类来定义数据结构，Pydantic 就会自动完成剩下的工作。这使得代码不仅更具可读性、易于维护，还能与静态分析工具（如 MyPy）和 IDE（如 VSCode, PyCharm）无缝集成，提供强大的自动补全和类型检查支持

**主要功能：**
- 高级数据校验-从内置到自定义： Pydantic 不仅支持所有 Python 标准类型，还提供了丰富的约束类型，如 PositiveInt、EmailStr 等。当内置类型无法满足需求时，你可以通过多种方式实现自定义校验逻辑；还可以使用 `@field_validator` 装饰器在模型内部定义校验逻辑，或通过 `@model_validator` 实现跨字段的复杂校验
- 灵活的数据序列化：Pydantic 模型可以轻松地转换为 Python 字典或 JSON 字符串。model_dump() 和 model_dump_json() 方法提供了丰富的参数来控制序列化过程
- 优雅的配置管理-BaseSettings：通过 pydantic-settings 扩展包提供 BaseSettings 类，极大地简化了这一过程。可以像定义普通 Pydantic 模型一样定义配置类，它会自动从环境变量或 `.env` 文件中读取值，并进行类型校验和转换
- 与 Dataclasses 的无缝协作：Pydantic 也提供了 `@pydantic.dataclasses.dataclass` 装饰器。它在标准 dataclass 的基础上增加了 Pydantic 的校验能力

**性能优化与最佳实践**

虽然 Pydantic V2 性能卓越，但在高并发或性能敏感的场景下，遵循最佳实践仍然至关重要。

性能基准：Pydantic vs. Dataclass vs. msgspec
- 实例创建：msgspec 速度最快，几乎没有开销。dataclasses 其次，也非常轻量。Pydantic 由于需要执行校验逻辑，实例化开销相对最高。
- 序列化：msgspec 再次胜出，展现了其极致的性能。有趣的是，Pydantic 结合 orjson 的序列化速度优于 dataclasses，因为它避免了 asdict() 的转换开销。

Pydantic 用适度的实例化开销换来了强大的校验功能和开发便利性，在 API 边界等场景下是务实的选择。而在内部性能热点路径，msgspec 或 dataclasses 可能是更好的选择

# 参考资料

- [将其他文件转换为 Markdown](https://github.com/microsoft/markitdown)
- [Python类库列表](https://github.com/vinta/awesome-python)
- [Python数学库](https://github.com/manimCommunity/manim)
- [Prophet-数据预测，输入历史数据就能预测未来趋势，销量、流量预测超准](https://github.com/facebook/prophet)
- [Pathway：用于流处理、实时分析、LLM 管道和 RAG 的 Python ETL 框架](https://github.com/pathwaycom/pathway)
- 数据并行计算：Dask
- [Python金融程序](https://github.com/shashankvemuri/Finance)
- [blind-watermark：图片盲水印，提取水印无须原图！](https://github.com/guofei9987/blind_watermark)
- [Python 翻译类库](https://github.com/UlionTse/translators)