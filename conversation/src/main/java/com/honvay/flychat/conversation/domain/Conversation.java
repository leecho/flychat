package com.honvay.flychat.conversation.domain;

import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatQuote;
import com.honvay.flychat.chat.domain.model.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Conversation {

    /**
     * 对话ID
     */
    private String id;


    /**
     * 提示词
     */
    private String prompt;

    private Chat chat;


    /**
     * 上下文环境
     */
    private Relation relation;

    /**
     * 知识库配置
     */
    private Knowledge knowledge;

    /**
     * 模型参数设置
     */
    private Model model;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 结果
     */
    private String result;


    private boolean stream = false;

    public void init() {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        if (knowledge == null) {
            this.knowledge = new Knowledge();
        }
        if (this.model == null) {
            this.model = new Model();
            model.init();
        }
        if (this.relation == null) {
            this.relation = new Relation();
            this.relation.setClean(false);
        }
        if (this.chat == null) {
            this.chat = new Chat();
        }
    }

    public Chat toChat() {
        Chat chat = new Chat();
        chat.setId(this.chat.getId());
        chat.setName(this.chat.getName());
        User user = new User();
        user.setId(userId);
        chat.setUser(user);
        return chat;
    }

    public void addQuote(List<ChatQuote> quotes) {
        List<ChatQuote> quotes1 = this.knowledge.getQuotes();
        if (quotes1 == null) {
            quotes1 = new ArrayList<>();
        }
        quotes1.addAll(quotes);
    }
}
