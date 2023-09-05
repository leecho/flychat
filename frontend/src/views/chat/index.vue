<template>
  <a-layout class="layout" style="height: 100%">
    <a-layout-sider
      class="layout-sider"
      breakpoint="xl"
      :collapsible="true"
      :width="220"
      :hide-trigger="true"
    >
      <chat-list
        ref="chatListRef"
        :application-id="+applicationId"
        :current="+id"
        @change="loadChat"
        @create="newChat"
      />
    </a-layout-sider>
    <a-layout-content class="chat-content">
      <chat-container ref="chatContainerRef" :application-id="+applicationId" @new-chat="addChat" />
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts" setup>
  import ChatList from '@/views/chat/chat-list.vue';
  import { useRoute } from 'vue-router';
  import ChatContainer from '@/views/chat/container/index.vue';
  import { ref } from 'vue';
  import { ChatRecord } from '@/api/chat';

  const chatListRef = ref();
  const chatContainerRef = ref();
  const route = useRoute();
  const { applicationId, id } = route.params;
  const loadChat = (chat: ChatRecord) => {
    chatContainerRef.value.loadMessage(chat);
  };
  const newChat = () => {
    chatContainerRef.value.clearMessage();
  };
  const addChat = (chat: ChatRecord) => {
    chatListRef.value.addChat(chat);
  };

  // eslint-disable-next-line no-console
  console.log(route.params.id);
  console.log(route.params.applicationId);

  /* if (id) {
    chatContainerRef.value.loadMessage(+id);
  } */
</script>

<style lang="less">
  .chat-content {
    background-color: var(--color-bg-1);
  }
</style>
