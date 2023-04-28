<template>
  <div>
      <slot></slot>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
    name: "rd-tab",
    props: {
        title: String,
        index: Number,
        active: {type: Boolean, default: false},
        keep: {type: Boolean, default: true}
    }
})
</script>

<style scoped>

</style>