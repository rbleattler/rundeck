<template>
    <div id="utility-bar" class="utility-bar">
        <ul>
            <template v-for="item in utilityBar.containerGroupItems('root', 'left')">
                <UtilItem :item="item" :key="item.id" />
            </template>
        </ul>
        <ul style="flex-grow: 1; flex-direction: row-reverse;">
            <template v-for="item in utilityBar.containerGroupItems('root', 'right')">
                <UtilItem :item="item" :key="item.id" />
            </template>
        </ul>
    </div>
</template>


<script setup lang="ts">
import {onBeforeMount, ref} from 'vue'

import {UtilityBar} from '../../stores/UtilityBar'
import UtilItem from './UtilityBarItem.vue'
import {getRundeckContext} from "../../rundeckService";
import {RundeckContext} from "../../interfaces/rundeckWindow";


    const utilityBar = ref<UtilityBar>()

    const open = ref<boolean>(false)

    onBeforeMount(() => {
        const context: RundeckContext = getRundeckContext() as RundeckContext
        utilityBar.value = context.rootStore.utilityBar
    })

    function handleClick() {
        open.value = !open.value
    }

</script>

<style scoped lang="scss">
.utility-bar {
    position: relative;
    background-color: var(--utility-bar-background-color);
    box-shadow: 0px -1px grey;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    align-content: center;
}

ul {
    height: 100%;
    display: flex;
    align-content: center;
    align-items: center;
    list-style: none;
    margin: 0;
    padding: 0px 10px;
}

</style>

<style lang="scss">
.utility-bar__item {
    display: flex;
    align-items: center;
    height: 100%;
    margin: 0;
    // color: #808080;
    color: var(--font-color);
    padding: 2px 5px;
    cursor: pointer;

    &:hover {
        color: var(--font-color);
        background-color: var(--background-color-accent-lvl2);
    }

    >span {
        margin-left: 5px;
        font-weight: 700;
    }

    & &-icon.rdicon {
        background-size: 14px 14px;
        height: 14px;
        width: 14px;
    }

    &-counter {
        height: 100%;
        min-width: 19px;
        padding: 0 5px;
        border-radius: 50%;
        // background-color: #808080;
        background-color: var(--font-color);
        text-align: center;
        color: white;
        font-size: 12px;
    }

    &:hover &-counter {
        background-color: var(--font-color);
    }
}
</style>