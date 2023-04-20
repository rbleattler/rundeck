<template>
    <details :class="detailsCss">
      <summary  :class="linkCss">
        <slot name="link" :open="open">
          <slot name="label">More...</slot>
          <span class="more-indicator-verbiage more-info-icon"><slot name="more"><i class="glyphicon glyphicon-chevron-right"></i></slot></span>
          <span class="less-indicator-verbiage more-info-icon"><slot name="less"><i class="glyphicon glyphicon-chevron-down"></i></slot></span>
        </slot>
      </summary>

      <slot></slot>

    </details>
</template>
<script setup lang="ts">
import {computed, ref} from 'vue'

 const props = defineProps({
    options: {
        type: Object as () => { open: boolean, iconOpen: string, iconClosed: string, linkCss: string, css: string },
        default: () => {
            return {open: false}
        }
    }
 })

  const open = ref<boolean>(!!props.options.open)
  const linkCss = ref<string>(props.options.linkCss || '')

  const css = ref<string>(props.options.css || '')

  const detailsCss = computed(() => {
      return ['more-info','details-reset',this.css]
  })

</script>
