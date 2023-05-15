<template>
    <div v-show="isActive" class="tab-pane">
        <slot></slot>
    </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {webhookui} from "../../../stores/Webhooks";

export default defineComponent({
    name: 'Tab',
    props:{
        index: {
            type: Number,
            default: 0
        },
        title: {
            type: String,
            default: 'patabs--standard'
        }
    },
    data() {
        return {
            webhookui
        }
    },
    computed: {
        isActive() {
            return this.webhookui.activeTab === this.index
        }
    }
})
</script>

<style scoped>

</style>
