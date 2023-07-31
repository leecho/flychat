<script setup lang="ts">
  import { computed, ref } from 'vue';
  import { t } from '@/locale';
  import { useIconRender } from '@/hooks/icon-render';
  import layout from '@/hooks/layout';
  import copyToClip from '@/utils/copy';
  import SvgIcon from '@/components/common/SvgIcon/index.vue';
  import { Message } from '@arco-design/web-vue';
  import ChatAvatar from './avatar.vue';
  import ChatText from './text.vue';

  interface Props {
    dateTime?: string;
    text?: string;
    inversion?: boolean;
    error?: boolean;
    loading?: boolean;
  }

  interface Emit {
    (ev: 'regenerate'): void;
    (ev: 'delete'): void;
  }

  const props = defineProps<Props>();
  const emit = defineEmits<Emit>();
  const { isMobile } = layout();
  const { iconRender } = useIconRender();
  const textRef = ref<HTMLElement>();
  const asRawText = ref(props.inversion);
  const messageRef = ref<HTMLElement>();

  const handleCopy = async () => {
    try {
      await copyToClip(props.text || '');
      Message.success('复制成功');
    } catch {
      Message.error('复制失败');
    }
  };

  const options = computed(() => {
    const common = [
      {
        label: t('chat.copy'),
        key: 'copyText',
        icon: iconRender({ icon: 'ri:file-copy-2-line' }),
      },
      {
        label: t('common.delete'),
        key: 'delete',
        icon: iconRender({ icon: 'ri:delete-bin-line' }),
      },
    ];

    if (!props.inversion) {
      common.unshift({
        label: asRawText.value ? t('chat.preview') : t('chat.showRawText'),
        key: 'toggleRenderType',
        icon: iconRender({
          icon: asRawText.value ? 'ic:outline-code-off' : 'ic:outline-code',
        }),
      });
    }

    return common;
  });

  function handleSelect(key: 'copyText' | 'delete' | 'toggleRenderType') {
    // eslint-disable-next-line default-case
    switch (key) {
      case 'copyText':
        handleCopy();
        return;
      case 'toggleRenderType':
        asRawText.value = !asRawText.value;
        return;
      case 'delete':
        emit('delete');
    }
  }

  function handleRegenerate() {
    messageRef.value?.scrollIntoView();
    emit('regenerate');
  }
</script>

<template>
  <div
    ref="messageRef"
    class="flex w-full mb-6 overflow-hidden"
    :class="[{ 'flex-row-reverse': inversion }]"
  >
    <div
      class="flex items-center justify-center flex-shrink-0 h-8 overflow-hidden rounded-full basis-8"
      :class="[inversion ? 'ml-2' : 'mr-2']"
    >
      <chat-avatar :image="inversion" />
    </div>
    <div
      class="overflow-hidden text-sm"
      :class="[inversion ? 'items-end' : 'items-start']"
    >
      <p
        class="text-xs text-[#b4bbc4]"
        :class="[inversion ? 'text-right' : 'text-left']"
      >
        {{ dateTime }}
      </p>
      <div
        class="flex items-end gap-1 mt-2"
        :class="[inversion ? 'flex-row-reverse' : 'flex-row']"
      >
        <chat-text
          ref="textRef"
          :inversion="inversion"
          :error="error"
          :text="text"
          :loading="loading"
          :as-raw-text="asRawText"
        />
        <div class="flex flex-col">
          <button
            v-if="!inversion"
            class="mb-2 transition text-neutral-300 hover:text-neutral-800 dark:hover:text-neutral-300"
            @click="handleRegenerate"
          >
            <svg-icon icon="ri:restart-line" />
          </button>
          <a-dropdown
            :trigger="isMobile ? 'click' : 'hover'"
            :position="!inversion ? 'br' : 'bl'"
            :options="options"
            @select="handleSelect"
          >
            <button
              class="transition text-neutral-300 hover:text-neutral-800 dark:hover:text-neutral-200"
            >
              <svg-icon icon="ri:more-2-fill" />
            </button>
            <template #content>
              <a-doption value="copyText">
                {{ t('chat.copy') }}
              </a-doption>
              <a-doption value="delete">
                {{ t('chat.delete') }}
              </a-doption>
              <a-doption v-if="!props.inversion" value="toggleRenderType">
                {{ asRawText ? t('chat.preview') : t('chat.showRawText') }}
              </a-doption>
            </template>
          </a-dropdown>
        </div>
      </div>
    </div>
  </div>
</template>
