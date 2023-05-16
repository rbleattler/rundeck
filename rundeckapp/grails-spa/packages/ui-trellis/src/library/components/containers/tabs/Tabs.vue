<template>
    <div class="patabs" :class="[type]">
        <div class="patabs-list">
            <div v-for="(tab, index) in tabs" :key="tab.title" @click="selectTab(index)" class="patab-item"
                 :class="{ 'patab-item--active': index === selectedIndex}">
                {{ tab.props.title }}
            </div>
        </div>
        <div ref="tabs">
        <slot></slot>
        </div>
    </div>
</template>

<script>
import {defineComponent} from 'vue'

export default defineComponent({
    data(){
        return {
            selectedIndex: 0,
            tabs: []
        }
    },
    props:{
        type: {
            type: String,
            default: 'patabs--standard'
        },
    },
    methods: {
        selectTab (i) {
            this.selectedIndex = i
        }
    },
    mounted () {
        this.selectTab(0)
    },
    created (){
        this.tabs = this.$slots.default()
    }
})
</script>