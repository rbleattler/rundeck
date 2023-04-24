<template>
    <div class="widget-wrapper" ref="root">
        <div class="widget-section" style="flex-grow: 1; flex-shrink: 1;">
            <div>
                <div class="form-group form-group-sm has-feedback has-search">
                    <i class="fas fa-search form-control-feedback"/>
                    <input
                        ref="search"
                        type="text" 
                        class="filter-list__input form-control form-control-sm"
                        v-model="searchTerm"
                        :placeholder="searchText"/>
                </div>
            </div>
            <Skeleton :loading="loading">
                <RecycleScroller
                    ref="scroller"
                    :items="filtered()"
                    :item-size="itemSize"
                    :key="items.length"
                    v-slot:default="{ item }"
                    key-field="id"
                    class="scroller"
                >
                    <div style="height: 100%;" :ref="item[idField]" role="button" tabindex="0" class="scroller__item" :class="{'scroller__item--selected': item[idField] == selected}" @click="() => itemClicked(item)" @keypress.enter="itemClicked(item)">
                        <slot name="item" :item="item" default-scope="item"/>
                    </div>
                </RecycleScroller>
            </Skeleton>
        </div>
        <div class="widget-section" style="height: 40px; flex-grow: 0; flex-shrink: 0; padding-left: 10px">
            <slot name="footer"/>
            <!-- <a class="text-info" :href="allProjectsLink" @click@keypress.enter="handleSelect">View All Projects</a> -->
        </div>
    </div>
</template>

<script setup lang="ts">
import  {nextTick, onMounted, ref} from 'vue'
import { autorun } from 'mobx'
import PerfectScrollbar from 'perfect-scrollbar'
import { RecycleScroller } from 'vue-virtual-scroller'
import 'vue-virtual-scroller/dist/vue-virtual-scroller.css'

RecycleScroller.updated = function() {
    if (!ps.value)
        nextTick().then(() => {ps.value = new PerfectScrollbar(scroller.value, {minScrollbarLength: 20})})
    else
        ps.value.update()
}

const destroy = RecycleScroller.beforeDestroy
RecycleScroller.beforeDestroy = function() {
    destroy.bind(this)()
    if (ps.value) {
        try {
            ps.value.destroy()
            ps.value = null
        } catch {}
    }
}
    const ps = ref<PerfectScrollbar>()
    const root = ref<>()
    const search = ref<>()
    const scroller = ref<>()
    const searchTerm = ref<string>('')


const props = withDefaults(defineProps<{
    loading: boolean
    searchText: string
    items: object[]
    itemSize: Number
    selected: string
    idField: string
}>(), {
    loading: false,
    searchText: '',
    itemSize: 25,
    selected: '',
    idField: 'id'
})

    function filtered() {
        return this.items.filter(i => i.name.includes(searchTerm.value))
    }

    onMounted(() => {
        autorun(() => {
            if (props.items.length) {
                /** May be necessary for virtual scroller to update */
                root.value.$forceUpdate()
            }
        })
        nextTick().then(() => {
            (<HTMLElement>search.value).focus()
        })
    })

    const emit = defineEmits(['item:selected'])
    function itemClicked(item: any) {
        (<HTMLElement>root.value.$refs[item[this.idField]]).blur()
        emit('item:selected', item)
    }

</script>

<style scoped lang="scss">
.widget-wrapper {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    height: 100%;
    max-width: 500px;
    overflow: hidden;
    min-height: 0;
}

.widget-section {
    display: flex;
    flex-direction: column;
    min-height: 0;
    justify-content: center;
}


.scroller {
    height: 100%;
    overflow-x: hidden;
    padding-right: 5px;
    flex-grow: 1;
}

.filter-list__input {
    border-width: 0.2em;
}

.scroller__item {
    position: relative;
    padding-left: 10px;
    width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    cursor: pointer;

    outline: none;

    &:hover::before, &:focus::before {
        position: absolute;
        content: "";
        height: 100%;
        border-left: 3px solid #F73F39;
        margin-left: -10px;
    }
}

.has-search .form-control-feedback {
    right: initial;
    left: 0;
    top: 8px;
}

.has-search .form-control {
    padding-right: 12px;
    padding-left: 34px;
}

.skeleton {
    --skel-color: #eeeeee !important;
    margin: 0 10px 0 10px;
}

</style>