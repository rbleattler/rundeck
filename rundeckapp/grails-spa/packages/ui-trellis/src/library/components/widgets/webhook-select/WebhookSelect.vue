<template>
    <FilterList v-on="$listeners" :items="webhooks.webhooksForProject(project)" id-field="uuid" :selected="selected" searchText="Filter Webhooks" :itemSize="40">
        <template v-slot:item="{item}"  >
            <WebhookItem :webhook="item"/>
        </template>
    </FilterList>
</template>


<script setup lang="ts">
import {onBeforeMount, ref} from 'vue'

import { PluginStore } from '../../../stores/Plugins'
import { WebhookStore } from '../../../stores/Webhooks'

import FilterList from '../../filter-list/FilterList.vue'

import WebhookItem from './WebhookSelectItem.vue'
import {RundeckContext} from "../../../interfaces/rundeckWindow";
import {getRundeckContext} from "../../../rundeckService";

    const props = withDefaults(defineProps<{
        project: string,
        selected: string
    }>(), { selected: ''})


    const plugins = ref<PluginStore>()

    const webhooks = ref<WebhookStore>()

    onBeforeMount(() => {
        const rootStore = (getRundeckContext() as RundeckContext).rootStore
        plugins.value = rootStore.plugins
        webhooks.value = rootStore.webhooks

        webhooks.value.load(props.project)
    })

    function itemUpdated() {
        console.log('Updated webhook select')
    }

</script>

<style scoped lang="scss">
::v-deep .plugin-icon {
    height: 20px !important;
    width: 20px !important;
}

::v-deep .scroller__item {
    border-radius: 5px;
    padding-left: 10px;

    &--selected, &:hover {
        background-color: var(--background-color-accent);
    }

    &:before {
        content: none !important;
    }
}
</style>