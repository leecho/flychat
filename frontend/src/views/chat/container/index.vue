<script setup lang="ts">
  import { computed, defineProps, onMounted, Ref, ref } from 'vue';
  import html2canvas from 'html2canvas';
  import useBasicLayout from '@/hooks/layout';
  import { t } from '@/locale';
  import MessageItem from '@/views/chat/message/index.vue';
  import SvgIcon from '@/components/common/SvgIcon/index.vue';
  import useScroll from '@/hooks/scroll';
  import { Message, Modal } from '@arco-design/web-vue';
  import ChatHeader from '@/views/chat/header/index.vue';
  import { ChatMessage } from '@/store/modules/chat/types';
  import {
    ChatRecord,
    conversation,
    ConversationOption,
    loadMessage,
  } from '@/api/chat';

  const controller = new AbortController();
  const { isMobile } = useBasicLayout();
  const { scrollRef, scrollToBottom } = useScroll();
  const prompt = ref<string>('');
  const loading = ref<boolean>(false);
  const inputRef = ref<Ref | null>(null);
  const messages = ref<ChatMessage[]>([]);
  const chat = ref<ChatRecord | null>(null);

  // 添加PromptStore
  // 使用storeToRefs，保证store修改后，联想部分能够重新渲染
  const promptTemplate: any[] = [];

  const emits = defineEmits(['newChat']);

  interface ApplicationValue {
    applicationId: number;
  }

  const props = defineProps<ApplicationValue>();

  const fetchMessages = async () => {
    if (chat.value == null) {
      return;
    }
    try {
      const { data } = await loadMessage(chat.value.id);
      messages.value = data.map((message) => {
        const chatMessage1: ChatMessage = {
          localId: new Date().getTime(),
          createTime: new Date(message.createTime).toLocaleString(),
          content: message.content,
          inversion: message.role === 'user',
          error: false,
          loading: false,
        };
        return chatMessage1;
      });
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      // empty
    }
  };

  const doConversation = async (
    promptText: string,
    chatMessage: ChatMessage
  ) => {
    const options: ConversationOption = {
      onFinish(response: string) {
        // eslint-disable-next-line no-console
        chatMessage.content = response;
        loading.value = false;
      },
      onUpdate(returnChat: ChatRecord, response: string, result: string) {
        // eslint-disable-next-line no-console
        messages.value[messages.value.length - 1] = {
          localId: chatMessage.localId,
          createTime: chatMessage.createTime,
          content: response,
          inversion: false,
          error: false,
          loading: true,
        };
        if (chat.value == null) {
          chat.value = returnChat;
          console.log('new chat from conversation', chat.value);
          emits('newChat', chat.value);
        }
      },
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      onError(error: Error) {
        chatMessage.error = true;
        loading.value = false;
      },
    };
    await conversation(
      chat.value,
      props.applicationId,
      promptText,
      options,
      controller
    );
  };

  async function onConversation() {
    const message = prompt.value;
    if (loading.value) return;
    if (!message || message.trim() === '') return;
    messages.value.push({
      createTime: new Date().toLocaleString(),
      content: message,
      inversion: true,
      error: false,
      localId: new Date().getTime(),
    });
    loading.value = true;
    prompt.value = '';

    const newMessage: ChatMessage = {
      localId: new Date().getTime(),
      createTime: new Date().toLocaleString(),
      content: '',
      inversion: false,
      error: false,
      loading: true,
    };
    messages.value.push(newMessage);
    await doConversation(message, newMessage);
    await scrollToBottom();
  }

  const handleSubmit = () => {
    onConversation();
  };

  const handleExport = () => {
    if (loading.value) return;

    const d = Modal.warning({
      title: t('chat.exportImage'),
      content: t('chat.exportImageConfirm'),
      okText: t('common.yes'),
      cancelText: t('common.no'),
      onOk: async () => {
        try {
          // d.mask = true;
          const ele = document.getElementById('image-wrapper');
          const canvas = await html2canvas(ele as HTMLDivElement, {
            useCORS: true,
          });
          const imgUrl = canvas.toDataURL('image/png');
          const tempLink = document.createElement('a');
          tempLink.style.display = 'none';
          tempLink.href = imgUrl;
          tempLink.setAttribute('download', 'chat-shot.png');
          if (typeof tempLink.download === 'undefined')
            tempLink.setAttribute('target', '_blank');

          document.body.appendChild(tempLink);
          tempLink.click();
          document.body.removeChild(tempLink);
          window.URL.revokeObjectURL(imgUrl);
          // d.mask = false;
          Message.success(t('chat.exportSuccess'));
          Promise.resolve();
        } catch (error: any) {
          Message.error(t('chat.exportFailed'));
        } finally {
          // d.mask = false;
        }
      },
    });
  };

  const handleDelete = () => {
    if (loading.value) return;

    Modal.warning({
      title: t('chat.deleteMessage'),
      content: t('chat.deleteMessageConfirm'),
      okText: t('common.yes'),
      cancelText: t('common.no'),
    });
  };

  const handleClear = () => {
    if (loading.value) return;

    Modal.warning({
      title: t('chat.clearChat'),
      content: t('chat.clearChatConfirm'),
      okText: t('common.yes'),
      cancelText: t('common.no'),
    });
  };

  const handleEnter = (event: KeyboardEvent) => {
    if (!isMobile.value) {
      if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        handleSubmit();
      }
    } else if (event.key === 'Enter' && event.ctrlKey) {
      event.preventDefault();
      handleSubmit();
    }
  };

  const handleStop = () => {
    if (loading.value) {
      controller.abort();
      loading.value = false;
    }
  };

  // 可优化部分
  // 搜索选项计算，这里使用value作为索引项，所以当出现重复value时渲染异常(多项同时出现选中效果)
  // 理想状态下其实应该是key作为索引项,但官方的renderOption会出现问题，所以就需要value反renderLabel实现
  const searchOptions = computed(() => {
    if (prompt.value.startsWith('/')) {
      return promptTemplate
        .filter((item: { key: string }) =>
          item.key
            .toLowerCase()
            .includes(prompt.value.substring(1).toLowerCase())
        )
        .map((obj: { value: any }) => {
          return {
            label: obj.value,
            value: obj.value,
          };
        });
    }

    return [];
  });

  // value反渲染key
  const renderOption = (option: { label: string }) => {
    return [];
  };

  const placeholder = computed(() => {
    if (isMobile.value) return t('chat.placeholderMobile');
    return t('chat.placeholder');
  });

  const buttonDisabled = () => {
    return loading.value || !prompt.value || prompt.value.trim() === '';
  };

  const footerClass = computed(() => {
    let classes = ['p-4'];
    if (isMobile.value)
      classes = [
        'sticky',
        'left-0',
        'bottom-0',
        'right-0',
        'p-2',
        'pr-3',
        'overflow-hidden',
      ];
    return classes;
  });

  defineExpose({
    loadMessage: async (chatToLoad: ChatRecord) => {
      chat.value = chatToLoad;
      await fetchMessages();
    },
    clearMessage: () => {
      chat.value = null;
      messages.value = [];
    },
  });

  onMounted(() => {
    scrollToBottom();
    if (inputRef.value && !isMobile.value) inputRef.value?.focus();
  });
</script>

<template>
  <div class="flex flex-col w-full h-full">
    <chat-header
      v-if="isMobile"
      using-context="false"
      @export="handleExport"
      @handle-clear="handleClear"
    />
    <main class="flex-1 overflow-hidden">
      <div
        id="scrollRef"
        ref="scrollRef"
        class="h-full overflow-hidden overflow-y-auto"
      >
        <div
          id="image-wrapper"
          class="w-full max-w-screen-xl m-auto"
          :class="[isMobile ? 'p-2' : 'p-4']"
        >
          <template v-if="!messages.length">
            <div
              class="flex items-center justify-center mt-4 text-center text-neutral-300"
            >
              <svg-icon icon="ri:bubble-chart-fill" class="mr-2 text-3xl" />
              <span>Aha~</span>
            </div>
          </template>
          <template v-else>
            <div>
              <message-item
                v-for="(item, index) of messages"
                :key="index"
                :date-time="item.createTime"
                :text="item.content"
                :inversion="item.inversion"
                :error="item.error"
                :loading="item.loading"
                @delete="handleDelete"
              />
              <div class="sticky bottom-0 left-0 flex justify-center">
                <a-button v-if="loading" status="warning" @click="handleStop()">
                  <template #icon>
                    <svg-icon icon="ri:stop-circle-line" />
                  </template>
                  {{ t('common.stopResponding') }}
                </a-button>
              </div>
            </div>
          </template>
        </div>
      </div>
    </main>
    <footer :class="footerClass">
      <div class="w-full max-w-screen-xl m-auto">
        <div class="flex items-center justify-between space-x-2">
          <a-tooltip content="This is tooltip content">
            <a-button>
              <svg-icon icon="ri:delete-bin-line" />
            </a-button>
          </a-tooltip>
          <a-tooltip>
            <a-button v-if="!isMobile" @click="handleExport()">
              <svg-icon icon="ri:download-2-line" />
            </a-button>
          </a-tooltip>

          <a-input
            ref="inputRef"
            v-model="prompt"
            :placeholder="placeholder"
            :autosize="{ minRows: 1, maxRows: isMobile ? 4 : 8 }"
            @keypress="handleEnter"
          />
          <a-button type="primary" @click="handleSubmit()">
            <svg-icon icon="ri:send-plane-fill" />
          </a-button>
        </div>
      </div>
    </footer>
  </div>
</template>
