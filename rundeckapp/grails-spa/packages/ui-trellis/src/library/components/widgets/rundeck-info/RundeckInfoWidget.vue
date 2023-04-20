<template>
    <InfoDisplay v-if="loaded" 
        :version="system.versionInfo"
        :latest="releases.releases[0]"
        :server="system.serverInfo"
        :app-info="system.appInfo"
    />
</template>


<script setup lang="ts">
import  {onBeforeMount, onMounted, ref} from 'vue'

import { Releases } from '../../../stores/Releases'
import { SystemStore } from '../../../stores/System'

import InfoDisplay from './RundeckInfo.vue'
import {getRundeckContext} from "../../../rundeckService";
import {RundeckContext} from "../../../interfaces/rundeckWindow";

    const system = ref<SystemStore>()

    const releases = ref<Releases>()

    const loaded = ref<boolean>(false)

    onBeforeMount(() => {
        const rootStore = (getRundeckContext() as RundeckContext).rootStore
        system.value = rootStore.system
        releases.value = rootStore.releases
    })

    onMounted(async () => {

        releases.value.load()
        try {
            await Promise.all([
                system.value.load(),
            ])
        } catch(e) {}
        this.loaded = true
    })

</script>