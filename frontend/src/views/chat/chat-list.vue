<template>
  <div class="flex flex-col h-full">
    <main class="flex flex-col flex-1 min-h-0">
      <div class="p-4">
        <a-button long type="primary" @click="newChat()"> 新对话 </a-button>
      </div>
      <div class="p4">
        <a-list
          class="chat-list"
          size="small"
          :bordered="chatListConfig.bordered"
          :split="chatListConfig.split"
          :hoverable="chatListConfig.hoverable"
        >
          <a-list-item
            v-for="(item, index) in chats"
            :key="item.id"
            :class="{ active: activeChat === item }"
            :index="index"
            @click="switchChat(item, true)"
          >
            <span v-show="item !== editingChat">
              {{ item.name }}
            </span>
            <a-input
              v-show="item === editingChat"
              v-model="item.name"
              size="mini"
              @press-enter="editingChat = null"
              @blur="editingChat = null"
            />
            <template #actions>
              <icon-edit @click="editingChat = item" />
              <a-popconfirm content="确定要删除该对话？" @ok="deleteChat(item)">
                <icon-delete />
              </a-popconfirm>
            </template>
          </a-list-item>
        </a-list>
      </div>
    </main>
  </div>
</template>

<script lang="ts" setup>
  import useChatStore from '@/store/modules/chat';
  import { ref, defineEmits, defineProps, onMounted } from 'vue';
  import { Chat } from '@/store/modules/chat/types';
  import { ChatRecord, loadChats } from '@/api/chat';

  const emits = defineEmits(['change', 'create']);

  const chatStore = useChatStore();
  const chats = ref<ChatRecord[]>([]);
  const activeChat = ref<ChatRecord>();
  interface ChatValue {
    current: number;
    applicationId: number;
  }

  const props = defineProps<ChatValue>();

  const switchChat = async (chat: ChatRecord, fireEvent: boolean) => {
    if (activeChat.value !== chat) {
      activeChat.value = chat;
      if (fireEvent) {
        emits('change', activeChat.value);
      }
    }
    // await router.push({ params: { id: chat.id } });
    // await router.push({ name: 'message', params: { id: chat.id } });
  };

  const newChat = () => {
    // switchChat(chat);
    emits('create');
  };

  const addChat = (chat: ChatRecord) => {
    chats.value.unshift(chat);
    switchChat(chat, false);
  };

  defineExpose({
    addChat,
  });

  const fetchSourceData = async () => {
    try {
      const { data } = await loadChats(props.applicationId);
      chats.value = data;
      chats.value.forEach((chat) => {
        if (chat.id === props.current) {
          switchChat(chat, true);
        }
      });
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      // empty
    }
  };

  const editingChat = ref();

  const chatListConfig = {
    bordered: false,
    split: false,
    hoverable: true,
  };
  const deleteChat = (chat: Chat) => {
    chatStore.remove(chat);
  };
  onMounted(async () => {
    await fetchSourceData();
  });
</script>

<style lang="less">
  .chat-list .arco-list-item-action {
    padding-left: 10px;
    visibility: hidden !important;
  }
  .chat-list .arco-list-item {
    cursor: pointer;
  }
  .chat-list .arco-list-item:hover .arco-list-item-action {
    visibility: visible !important;
  }

  .chat-list .active {
    background-color: var(--color-fill-1);
  }
</style>
