<template>
    <FilterList v-bind="$attrs" :items="webhooks.webhooksForProject(project)" id-field="uuid" :selected="selected" searchText="Filter Webhooks" :itemSize="40">
        <template v-slot:item="{item}"  >
            <WebhookItem :webhook="item"/>
        </template>
    </FilterList>
</template>


<script lang="ts">
import {defineComponent, onBeforeMount, ref} from 'vue'

import { PluginStore } from '../../../stores/Plugins'
import { WebhookStore } from '../../../stores/Webhooks'

import FilterList from '../../filter-list/FilterList.vue'

import WebhookItem from './WebhookSelectItem.vue'
import {RundeckContext} from "../../../interfaces/rundeckWindow";
import {getRundeckContext} from "../../../rundeckService";

export default defineComponent({
    name: "WebhookSelect",
    components: {
        FilterList,
        WebhookItem
    },
    props: {
        project: {
            type: String,
            required: true
        },
        selected: {
            type: String,
            default: ''
        }
    },
    data() {
        return {
            webhooks: null
        }
    },
    beforeMount() {
        const rootStore = (getRundeckContext() as RundeckContext).rootStore
        this.webhooks = rootStore.webhooks

        this.webhooks.load(this.project)
    }
})
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