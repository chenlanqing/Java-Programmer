# 一、@Value注解原理

- [你不得不知道的@Value注解原理](https://juejin.cn/post/6844903904732266510)

## 1、基本流程

- 在spring中是由`AutowiredAnnotationBeanPostProcessor`解析处理@Value注解。AutowiredAnnotationBeanPostProcessor是一个BeanPostProcessor，所以每个类的实例化都过经过AutowiredAnnotationBeanPostProcessor类。
- 当post-processor处理bean时，会解析bean Class的所有属性，在解析时会判断属性上是否标有@Value注解，有就解析这个@Value的属性值，将解析后结果放入AutowiredFieldElement类型InjectionMetaData.checkedElements中；
- 当给属性赋值时会使用checkedElements，从而得到@Value注解的Filed属性，调用AutowiredFieldElement.inject()方法进行解析，解析时会使用DefaultListableBeanFactory(用于解析${})和TypeConverter(用于类型转换)，从而得到age属性的值，最后调用field.set(bean, value)，从而获取的值赋给bean的field

整体调用栈：
```
AbstractAutowireCapableBeanFactory.createBean()                                    
├AbstractAutowireCapableBeanFactory.doCreateBean()                               
├─AbstractAutowireCapableBeanFactory.applyMergedBeanDefinitionPostProcessors()   
├──AutowiredAnnotationBeanPostProcessor.postProcessMergedBeanDefinition()        
├───AutowiredAnnotationBeanPostProcessor.findAutowiringMetadata()                
├────AutowiredAnnotationBeanPostProcessor.buildAutowiringMetadata() 
========以上是存逻辑，以下是用逻辑========
> "存"指把需要解析的数据放到InjectionMetadata中
> "用"指对放到InjectionMetadata的数据进行解析
├─AbstractAutowireCapableBeanFactory.populateBean()                               
├──AutowiredAnnotationBeanPostProcessor.postProcessProperties()                  
├───AutowiredAnnotationBeanPostProcessor.findAutowiringMetadata()                
├────InjectionMetadata.inject()                                                  
├─────(InjectionMetadata.InjectedElement)AutowiredFieldElement.inject()
├──────DefaultListableBeanFactory.resolveDependency()
├───────DefaultListableBeanFactory.doResolveDependency() ---解析依赖上@value(${})的核心入口
├────────AbstractBeanFactory.resolveEmbeddedValue() ---解析所有用到${}的放的核心入口
├─────────StringValueResolver lambda子类.resolveStringValue() ---这个lambda使用方式值得琢磨下
├──────────PropertySourcesPropertyResolver.resolveRequiredPlaceholders()
├───────────AbstractPropertyResolver.getPropertyAsRawString()
├────────────PropertyPlaceholderHelper.replacePlaceholders()
├─────────────PropertySource.getProperty()
├────────────PropertyPlaceholderHelper.parseStringValue() 递归
├───────────AbstractPropertyResolver.resolvePlaceholder()
├────────────PropertySource.getProperty()
├───────SimpleTypeConverter.convertIfNecessary()
├────────TypeConverterDelegate.convertIfNecessarT()
├─────────ConversionService.canConvert()
├─────────ConversionService.convert()
```
