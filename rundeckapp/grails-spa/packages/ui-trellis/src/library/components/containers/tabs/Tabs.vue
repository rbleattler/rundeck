<template>
    <div class="patabs" :class="[type]">
        <div class="patabs-list">
            <div v-for="(tab, index) in tabs" :key="tab.title" @click="selectTab(index)" class="patab-item"
                 :class="[index === webhookui.activeTab ? 'patab-item--active' : '']">
                {{ tab.props.title }}
            </div>
        </div>
        <slot></slot>
    </div>
</template>

<script>
import {defineComponent} from 'vue'
import { webhookui} from "@/library/stores/Webhooks";

export default defineComponent({
    data(){
        return {
            webhookui,
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
            this.webhookui.setActiveTab(i)
            // this.selectedIndex = i
            // // loop over all the tabs
            // this.tabs.forEach((tab, index) => {
            //     tab.isActive = (index === i)
            // })
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