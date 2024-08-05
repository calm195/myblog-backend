# Flexmark Kit

flexmark-java是 CommonMark（spec 0.28）解析器的Java实现，是一款具有源级别AST的CommonMark / Markdown Java解析器。它的优势在于解析速度快，灵活性强，而且能够支持自定义AST，允许对解析过程进行精细控制，内置大量解析器和常用扩展，为解析行为和 HTML 属性样式提供了更多转换设置和选项，如gfm扩展用于支持gfm风格样式，Toc扩展用于创建目录和自定义目录层级等，同时也可以自定义扩展来实现业务需求，例如为标签增加自定义属性等。

## 引入依赖

```xml
<dependency>
    <groupId>com.vladsch.flexmark</groupId>
    <artifactId>flexmark-all</artifactId>
    <version>0.62.2</version>
</dependency>
```

## 使用示例

```java
// 创建一个 MutableDataSet 对象来配置 Markdown 解析器的选项
MutableDataSet options = new MutableDataSet();

// 添加各种 Markdown 解析器的扩展
options.set(Parser.EXTENSIONS, Arrays.asList(
        AutolinkExtension.create(),     // 自动链接扩展，将URL文本转换为链接
        EmojiExtension.create(),        // 表情符号扩展，用于解析表情符号
        GitLabExtension.create(),       // GitLab特有的Markdown扩展
        FootnoteExtension.create(),     // 脚注扩展，用于添加和解析脚注
        TaskListExtension.create(),     // 任务列表扩展，用于创建任务列表
        TablesExtension.create()));     // 表格扩展，用于解析和渲染表格

// 使用配置的选项构建一个 Markdown 解析器
Parser parser = Parser.builder(options).build();
// 使用相同的选项构建一个 HTML 渲染器
HtmlRenderer renderer = HtmlRenderer.builder(options).build();

// 解析传入的 Markdown 文本并将其渲染为 HTML
return renderer.render(parser.parse(markdown));
```

## 扩展

flexmark-java 提供了丰富的扩展，可以通过 `Parser.EXTENSIONS` 配置选项来添加扩展，常用的扩展如下：

- `AutolinkExtension`：自动链接扩展，将URL文本转换为链接
- `EmojiExtension`：表情符号扩展，用于解析表情符号
- `GitLabExtension`：GitLab特有的Markdown扩展
- `FootnoteExtension`：脚注扩展，用于添加和解析脚注
- `TaskListExtension`：任务列表扩展，用于创建任务列表
- `TablesExtension`：表格扩展，用于解析和渲染表格
- `TocExtension`：目录扩展，用于创建目录和自定义目录层级
- `YamlFrontMatterExtension`：YAML 前置扩展，用于解析 YAML 前置元数据
- `AbbreviationExtension`：缩写扩展，用于解析和渲染缩写
- `DefinitionExtension`：定义扩展，用于解析和渲染定义

## HtmlRenderer

`HtmlRenderer` 是 flexmark-java 提供的一个用于将 Markdown AST 渲染为 HTML 的工具类，可以通过 `HtmlRenderer.builder()` 方法来构建一个 `HtmlRenderer` 对象，然后使用 `render()` 方法将 Markdown AST 渲染为 HTML。

使用示例：

```java
// 使用相同的选项构建一个 HTML 渲染器
HtmlRenderer renderer = HtmlRenderer.builder(options).build();

// 解析传入的 Markdown 文本并将其渲染为 HTML
return renderer.render(parser.parse(markdown));
```
