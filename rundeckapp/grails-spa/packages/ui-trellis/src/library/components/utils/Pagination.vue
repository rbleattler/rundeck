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
  <div v-if="totalPages>1" class="paging_links">
    <nav aria-label="Paging">
      <ul class="pagination pagination-sm">
        <li class="pagination-text" v-if="$slots['prefix']">
          <slot name="prefix"></slot>
        </li>
        <li :class="{disabled:!hasPreviousButton||disabled}">
          <a href="#"
             @click.prevent="changePage(value-1)"
             title="Previous Page"
             :class="navigationClass">
            <slot name="prevPage"><i class="glyphicon glyphicon-arrow-left"></i></slot>
          </a>
        </li>
        <li v-for="(page, index) in pageList" :key="page.page+'/'+index" :class="{[skipClass]:page.skip,active:page.page===value,disabled:disabled}">
          <span v-if="page.skip"><slot name="skip">&hellip;</slot></span>
          <a v-else-if="page.page!==value"
             href="#"
             @click.prevent="changePage(page.page)"
             :class="navigationClass"
             :title="'Page '+page.page">{{page.page}}</a>
          <span v-else
                :class="navigationClass"
                :title="'Page '+page.page">{{page.page}}</span>
        </li>
        <li :class="{disabled:!hasNextButton||disabled}">
          <a href="#"
             @click.prevent="changePage(value+1)"
             :class="navigationClass"
             title="Next Page">
            <slot name="nextPage"><i class="glyphicon glyphicon-arrow-right"></i></slot>
          </a>
        </li>
      </ul>
    </nav>
  </div>
</template>
<script setup lang="ts">
import {computed} from 'vue'

  export interface Props {
      modelValue: number,
      totalPages: number,
      disabled?: boolean,
      navigationClass?: string
      navigationDisabledClass?: string
      currentPageClass?: string
      skipClass?: string
      pagingWindowSize?: number
  }

  const props = withDefaults(defineProps<Props>(),{
      disabled: false,
      navigationClass:'page_nav_btn',
      navigationDisabledClass:'page_nav_btn_disabled',
      currentPageClass: 'page_current',
      skipClass: 'text-muted',
      pagingWindowSize: 7
  })
  const value = computed({
      get() {
          return props.modelValue
      },
      set(value) {
          emit('update:modelValue', value)
      }
  })
  const emit = defineEmits(['update:modelValue','change'])
  function changePage(page: number) {
    if (!props.disabled && page > 0 && page <= props.totalPages && page !== value) {
      value.value = page
      emit('change', page)
    }
  }

  const maxPagesDisplay = computed(() => {
    return Math.min(props.totalPages, props.pagingWindowSize)
  })

  const windowLeftPage = computed(() => {
    const leftNum = Math.floor(maxPagesDisplay / 2)
    const windowLeft = this.value - leftNum

    const adjustL = windowLeft < 1 ? 1 - windowLeft : 0

    return windowLeft + adjustL
  })

  const windowRightPage = computed(() => {
    return windowLeftPage + (maxPagesDisplay - 1)
  })

  /**
   * Create list of page links to display
   */
  const pageList = computed(() => {
    const pages: any[] = []
    let skipped = false
    const curPage = this.value

    // creates sliding window of size pagingWindowSize


    const totalPages1 = this.totalPages

    // assume total pages >1 because we do not show paging otherwise
    // and always show last page

    const minPage = this.windowLeftPage
    const maxPage = this.windowRightPage

    // always add first page
    pages.push({page: 1})

    for (let i = 2; i < totalPages1; i++) {
      let shouldSkip = false
      if (i < minPage || i > maxPage) {
        shouldSkip = true
      }
      if (!skipped && shouldSkip) {
        skipped = true
        pages.push({skip: true})
        continue
      } else if (skipped && !shouldSkip) {
        skipped = false
      }
      if (!skipped) {
        pages.push({page: i})
      }
    }

    // always add last page
    pages.push({page: totalPages1})

    return pages
  })

  const hasPreviousButton = computed<boolean>(() => {
    return this.value > 1
  })

  const hasNextButton = computed<boolean>(() => {
    return this.value < this.totalPages
  })

</script>
<style scoped lang="scss">
.pagination > li > a,
.pagination > li > span {
  height: 2em;
  min-width: 3em;
  padding: 0;
}
.pagination > li.pagination-text > span {
  border: none;
  margin-top: 1px;
  margin-right: 1em;
}
</style>
