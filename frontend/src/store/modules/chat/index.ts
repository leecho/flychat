import { defineStore } from 'pinia';
import { Chat, ChatState, ChatMessage } from '@/store/modules/chat/types';
import { loadChats } from '@/api/chat';

const useChatStore = defineStore('chat', {
  state: (): ChatState => ({
    active: null,
    mapping: {},
    chats: [
      {
        id: 1,
        localId: new Date().getTime(),
        name: 'New Chat',
        messages: [],
        mapping: {},
      } as Chat,
    ],
  }),

  getters: {
    getChats(): Chat[] {
      return this.chats;
    },
  },

  actions: {
    create() {
      const localId = new Date().getTime();
      const chat = <Chat>{
        id: null,
        localId,
        name: 'New Chat',
        messages: [],
        mapping: {},
      };
      this.chats.push(chat);
      this.mapping[localId] = chat;
      this.setActive(chat);
      return chat;
    },
    remove(chat: Chat) {
      const chatIndex = this.chats.indexOf(chat);
      this.chats.splice(chatIndex, 1);
    },

    createMessage(chatId: number, message: ChatMessage) {
      const chat = this.mapping[chatId];
      const messageId = new Date().getTime();
      message.localId = messageId;
      chat.mapping[messageId] = chat.messages.length;
      chat.messages.push(message);
    },

    updateMessage(chatId: number, message: ChatMessage) {
      const chat = this.mapping[chatId];
      const index = chat.mapping[message.localId];
      chat.messages[index] = message;
    },

    setActive(chat: Chat) {
      this.active = chat;
    },

    findById(id: number): Chat {
      return this.chats.find((chat) => chat.localId === id) as Chat;
    },

    findMessageById(id: number) {
      const existsChat = this.chats.find((chat) => chat.id === id);
      if (existsChat) {
        return existsChat.messages;
      }
      return [];
    },
  },
});

export default useChatStore;
