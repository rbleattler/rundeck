<template>
    <div class="widget-wrapper">
        <div class="widget-section" style="flex-grow: 1; flex-shrink: 1;">
            <div style="padding: 10px 10px 0 10px;">
                <div class="form-group form-group-sm has-feedback has-search">
                    <i class="fas fa-search form-control-feedback"/>
                    <input
                        ref="search"
                        type="text" 
                        class="form-control form-control-sm"
                        v-model="searchTerm"
                        placeholder="Search all projects"/>
                </div>
            </div>
            <Skeleton :loading="!projects.loaded">
                <RecycleScroller
                    ref="scroller"
                    :items="projects.search(searchTerm)"
                    :item-size="25"
                    :key="projects.search(searchTerm).length"
                    v-slot:default="{ item }"
                    key-field="name"
                    class="scroller"
                >
                    <a role="button" tabindex="0" class="scroller__item" :title="item.name"
                       :href="itemHref(item)">
                        <span class="text-ellipsis">{{item.label || item.name}}<span v-if="searchTerm && item.label && item.label!==item.name" class="text-muted"> {{item.name}}</span></span>
                    </a>
                </RecycleScroller>
            </Skeleton>
        </div>
             <div class="btn-group btn-group-justified" style="height: 40px; flex-grow: 0; flex-shrink: 0; border-top: solid 1px grey;">
                <a :href="allProjectsLink" role="button" tabindex="0" class="btn btn-default scroller__subbutton" style="border-radius: 0px; border: 0px; border-right: solid 1px grey;">
                <i class="far fa-eye"></i>
                View All</a>
                <a :href="createProjectLink" role="button" tabindex="0" class="btn btn-default scroller__subbutton" style="border-radius: 0px; border: 0px;">
                <i class="fas fa-plus-circle"></i>
                Create Project</a>
        </div>
    </div>
</template>

<script setup lang="ts">
import {computed, onBeforeMount, onMounted, ref, nextTick, getCurrentInstance } from 'vue'
import { autorun } from 'mobx'
import PerfectScrollbar from 'perfect-scrollbar'
import { RecycleScroller } from 'vue-virtual-scroller'
import 'vue-virtual-scroller/dist/vue-virtual-scroller.css'

import {getAppLinks, getRundeckContext} from '../../../rundeckService'
import { ProjectStore } from '../../../stores/Projects'
import Skeleton from '../../skeleton/Skeleton.vue'
import {url} from '../../../rundeckService'
import {RundeckContext} from "../../../interfaces/rundeckWindow";

const scroller = ref(null)

RecycleScroller.updated = function() {
    if (!ps.value)
        nextTick().then(() => {ps.value = new PerfectScrollbar(scroller.value.$el, {minScrollbarLength: 20})})
    else
        ps.value.update()
}

const unmount = RecycleScroller.beforeUnmount
RecycleScroller.beforeUnmount = function() {
    unmount.bind(this)()
    if (ps.value) {
        try {
            ps.value.destroy()
            ps.value = null
        } catch {}
    }
}

    const instance = getCurrentInstance()

    const projects = ref<ProjectStore>()

    const ps = ref<PerfectScrollbar>()

    const searchTerm = ref<string>('')

    const allProjectsLink = computed<string>(() => {
        return getAppLinks().menuHome
    })
    const createProjectLink = computed(() => {
        return url('resources/createProject')
    })
    function itemHref(project:any){
        return url(`?project=${project.name}`).href
    }

    onBeforeMount(() => {
        projects.value = (getRundeckContext() as RundeckContext).rootStore.projects
        projects.value.load()
    })

    const search = ref(null);
    onMounted(() => {
        autorun(() => {
            if (projects.value.projects.length) {
                /** May be necessary for virtual scroller to update */
                instance?.proxy?.$forceUpdate()
            }
        })
        nextTick().then(() => {
            (<HTMLElement>search.value).focus()
        })
    })

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
        border-left: 3px solid var(--brand-color);
        margin-left: -10px;
    }
}

a.scroller__item {
    color: var(--font-color);
    display: block;

    &:hover {
        text-decoration: none;
    }
}

a.scroller__subbutton:focus {
    text-decoration: underline;
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
.text-ellipsis{
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
}
</style>