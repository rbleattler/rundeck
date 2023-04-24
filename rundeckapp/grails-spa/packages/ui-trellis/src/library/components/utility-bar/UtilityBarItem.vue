<template>
    <UtilActionItem v-if="item.type == 'action'" :item="item"/>
    <UtilWidgetItem v-else-if="item.type == 'widget'" :item="item"/>
</template>

<script lang="ts">
import {defineComponent, PropType} from 'vue'

import {UtilityItem} from '../../stores/UtilityBar'

import UtilActionItem from './UtilityActionItem.vue'
import UtilWidgetItem from './UtilityWidgetItem.vue'

export default defineComponent({
    components: {
        UtilActionItem,
        UtilWidgetItem
    },
    props: {
        item: Object as PropType<UtilityItem>
    }
})
</script>
