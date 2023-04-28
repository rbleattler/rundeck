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
      props.eventBus.on('page-modified', setConfirm)
      props.eventBus.on('page-reset', resetConfirm)

      const orighandler = window.onbeforeunload
      window.onbeforeunload = (ev: BeforeUnloadEvent) => {
          if (needsConfirm) {
              return props.message || 'confirm'
          }
          if (typeof (orighandler) === 'function') {
              //@ts-ignore
              return orighandler(ev)
          }
      }
  })
</script>
