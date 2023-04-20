<!--
  - Copyright 2018 Rundeck, Inc. (http://rundeck.com)
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->
<template>
  <pagination v-model="currentPage" :total-pages="totalPages" @change="changePage($event)" :disabled="disabled" v-if="pagination.total">
    <template v-if="showPrefix" v-slot:prefix>
    <span>
      <span class="text-info">{{props.pagination.offset + 1}}-{{props.pagination.offset + props.pagination.max}}</span>
      <span class="text-muted">of {{props.pagination.total}}</span>
    </span>
    </template>
  </pagination>
</template>
<script setup lang="ts">
import {computed, onMounted, ref} from 'vue'
import Pagination from './Pagination.vue'
import {Pageable} from "../../interfaces/UiTypes";

  export interface Props {
    pagination: Pageable,
    showPrefix?:boolean,
    disabled?:boolean
  }

  const currentPage = ref<number>(0)

  const props = withDefaults(defineProps<Props>(),{
      showPrefix: true,
      disabled: false
  })

  const totalPages = computed(() => {
    return Math.ceil(props.pagination.total / props.pagination.max)
  })

  onMounted(() => {
    currentPage.value = pageNumberForOffset(props.pagination.offset)
  })

  const emit = defineEmits(['change'])

  function changePage (page: number) {
      emit('change', pageOffset(page))
  }
  function pageOffset (page: number) {
    return (page - 1) * props.pagination.max
  }

  function pageNumberForOffset(offset: number) {
    return 1 + (offset / props.pagination.max)
  }
</script>
