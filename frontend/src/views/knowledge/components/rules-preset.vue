<template>
  <div class="list-wrap">
    <a-typography-title class="block-title" :heading="6">
      {{ $t('cardList.tab.title.preset') }}
    </a-typography-title>
    <a-row class="list-row" :gutter="24">
      <a-col
        v-for="item in renderData"
        :key="item.id"
        :xs="12"
        :sm="12"
        :md="12"
        :lg="6"
        :xl="6"
        :xxl="6"
        class="list-col"
        style="min-height: 140px"
      >
        <CardWrap
          :loading="loading"
          :title="item.name"
          :description="item.introduction"
          :default-value="true"
          :tag-text="$t('cardList.preset.tag')"
          @click="open(item)"
        >
          <template #skeleton>
            <a-skeleton :animation="true">
              <a-skeleton-line :widths="['100%', '40%']" :rows="2" />
              <a-skeleton-line :widths="['40%']" :rows="1" />
            </a-skeleton>
          </template>
        </CardWrap>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import { queryApplicationList, ApplicationRecord } from '@/api/application';
  import useRequest from '@/hooks/request';
  import router from '@/router';
  import CardWrap from './card-wrap.vue';

  const defaultValue: ApplicationRecord[] = new Array(6).fill({});
  const { loading, response: renderData } = useRequest<ApplicationRecord[]>(
    queryApplicationList,
    defaultValue
  );

  const open = (application: ApplicationRecord) => {
    router.replace(`/conversation/${application.id}`);
  };
</script>

<style scoped lang="less"></style>
