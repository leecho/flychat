# FlyChat

# 介绍

基于Java和ChatGPT的AI平台，已实现基本的问答和基于知识库的问题，暂时只有后端应用，没有前端。

![Screenshot](screenshots/img.png)
# 框架

- Spring Boot 2.7.13
- JDK 17
- Postgresql 15
- MyBatis Plus
- Lombok


# 技术要点

## 在线问答

### 上下文聊天

通过 Postgresql 实现聊天数据存储来实现上下文聊天，已实现流式输出结果，可以通过配置参数 maxTokens 来限制上下问问题的数量。

数据库存储了每次聊天对话的记录，在选择上下文聊天时，通过 chatId 获取历史消息，将历史问题以及回答消息都发送给 GPT。

## 知识库问答

### 知识库分词
已实现按照句子分词、段落分词、固定长度分词等分词方式，并通过ChatGPT进行Embedding

### 知识库匹配
通过Postgresql 插件 PGvector进行相似度匹配，获取与问题比较匹配的知识点

### 知识库问答
通过对问题进行Embedding，然后匹配知识库，构建Prompt，发送给ChatGPT获取答案。

# 私有部署问题

可导入知识库文件、文本对知识库进行构建，知识库存储在本地数据库，调用ChatGPT接口进行问答，简而言之，数据还是会给到ChatGPT，但是不是所有的数据都给过去，理论上是一个半私有或者部分私有部署的状态，如果要实现完全私有化则需要部署一套私有的大语言模型。

# 风险声明

本项目仅供学习和研究使用，不鼓励用于商业用途。对于因使用本项目而导致的任何损失，我们不承担任何责任。

# 后续计划
目前项目只有后端没有前端，所有期待感兴趣的前端、UI、测试小伙伴一起参与项目。
后续实现功能：
- 增加类似与ChatPDF的功能
- 增加AI应用功能
- 集成其他大语言模型

# 联系方式

wechat：leecho571

# LICENSE
[MIT](LICENSE)

前端部分基于

[Chagpt-Web](https://github.com/Chanzhaoyu/chatgpt-web)