<template>
  <span ><slot v-if="display && needsConfirm" :confirm="confirmData" :needs-confirm="needsConfirm">{{message}}</slot></span>
</template>
<script setup lang="ts">
import Vue, {computed, onMounted, ref} from 'vue'

  const confirmData = ref<string[]>([])
  const props = defineProps<{
      eventBus: Vue
      message: string
      display: boolean
  }>()

  function setConfirm(name:string){
    const loc=confirmData.value.indexOf(name)
    if(loc<0){
      confirmData.value.push(name)
    }
  }

  function resetConfirm(name:string){
    const loc=confirmData.value.indexOf(name)
    if (loc >= 0) {
        confirmData.value.splice(loc, 1)
    } else if (name === '*') {
        confirmData.value.splice(0, confirmData.value.length)
    }
  }

  const needsConfirm = computed<boolean>(() => {
    return confirmData.value.length>0
  })

  onMounted(() => {
      this.eventBus.$on('page-modified', this.setConfirm)
      this.eventBus.$on('page-reset', this.resetConfirm)

      const orighandler = window.onbeforeunload
      const self = this
      window.onbeforeunload = (ev: BeforeUnloadEvent) => {
          if (this.needsConfirm) {
              return self.message || 'confirm'
          }
          if (typeof (orighandler) === 'function') {
              //@ts-ignore
              return orighandler(ev)
          }
      }
  })
</script>
