<template>
    <span v-if="items">
        <slot v-if="items.length<1"></slot>
        <template v-for="i in items">
            <template v-if="i.text">{{ i.text }}</template>
            <span v-else-if="i.html" v-html="i.html"></span>
            <component v-else-if="i.widget" :is="i.widget" :event-bus="eventBus" :item-data="itemData"/>
        </template>
    </span>
</template>
<script setup lang="ts">

import Vue, {onMounted, onUnmounted, ref} from 'vue'

import {
    getRundeckContext,
} from '../../rundeckService'
import {UIItem, UIWatcher} from '../../stores/UIStore'
import {RundeckContext} from "../../interfaces/rundeckWindow";


const rootStore = (getRundeckContext() as RundeckContext).rootStore

    const props = defineProps<{
        section: string,
        location: string,
        eventBus?: Vue,
        socketData?: any
    }>()

    const items = ref<UIItem[]>([])

    function loadItems() {
        items.value = rootStore.ui.itemsForLocation(props.section, props.location).filter((a) => a.visible)
    }

    onUnmounted(() => {
        if(uiwatcher.value){
            rootStore.ui.removeWatcher(uiwatcher.value)
        }
    })
    function itemData(){
      if(typeof this.socketData === 'string'){
        try{
          return JSON.parse(this.socketData)
        }catch (e){
          return this.socketData
        }
      }
      return this.socketData
    }

    const uiwatcher = ref<UIWatcher|null>(null)

    onMounted(() => {
        loadItems()
        uiwatcher.value = {
            section: props.section,
            location: props.location,
            callback: (items: UIItem[])=>{
                items=items
            }
        } as UIWatcher
        rootStore.ui.addWatcher(uiwatcher.value)
    })
</script>
