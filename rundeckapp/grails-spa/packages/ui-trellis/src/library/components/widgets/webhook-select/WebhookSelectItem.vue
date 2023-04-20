<template>
    <div class="webhook-select__item" style="height: 40px;display: flex; align-items: center;">
        <PluginInfo :detail="webhook.eventPlugin" :showTitle="false" :showDescription="false"/>
        <span style="margin-left: 10px; font-weight: 600;">{{webhook.name}}</span>
    </div>
</template>

<script setup lang="ts">
import { Webhook } from '../../../stores/Webhooks'
import PluginInfo from '../../plugins/PluginInfo.vue'

const props = defineProps<{
    webhook: Webhook
}>()

</script>

<style scoped lang="scss">
::v-deep .plugin-icon {
    border-radius: 4px;
    height: 20px;
    width: 20px;
}
</style>