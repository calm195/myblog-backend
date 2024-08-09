# houbb sensitive

[`com.github.houbb.sensitive`](https://github.com/houbb/sensitive-word) 是一个基于注解的脱敏工具，支持对对象、字符串、集合等数据进行脱敏处理。

## word

1. `SensitiveWordBs`：脱敏词库
    - `addWord(String word)`：添加脱敏词
    - `contains(String word)`：判断是否包含脱敏词
    - `remove(String word)`：移除脱敏词
    - `size()`：脱敏词数量
    - `clear()`：清空脱敏词
