<template>
    <div class="rundeck-info-widget">
        <div class="rundeck-info-widget__group">
            <div class="rundeck-info-widget__more-link">
                <a :href="welcomeUrl()">
                    <RundeckLogo v-if="props.appInfo.title === 'Rundeck'"/>
                    <PagerdutyLogo v-else/>
                </a>
            </div>
            <div class="rundeck-info-widget__header">
                <RundeckVersion :app="false" :logo="false" :title="props.appInfo.title" :logocss="props.appInfo.logocss" :number="version.number" :tag="version.tag"/>
            </div>
            <div>
                <VersionDisplay :text="`${version.name} ${version.color} ${version.icon}`" :icon="version.icon" :color="version.color" />
            </div>
            <div>
                <span class="server-display">
                    <ServerDisplay
                        :name="server.name"
                        :glyphicon="server.icon"
                        :uuid="server.uuid"
                    />
                </span>
            </div>
        </div>
        <div v-if="latest" class="rundeck-info-widget__group" style="border-top: solid 1px grey;">
            <div class="rundeck-info-widget__heading">Latest Release</div>
            <div class="rundeck-info-widget__latest">
                <RundeckVersion :app="false" :logo="false" :number="latest.full" :tag="latest.tag"/>
            </div>
        </div>
        <div class="rundeck-info-widget__group" style="display: flex; flex-direction: column-reverse; flex-grow: 1;">
            <Copyright/>
        </div>
    </div>
</template>

<script setup lang="ts">

import {VersionInfo, ServerInfo, AppInfo} from '../../../stores/System'
import { Release } from '../../../stores/Releases'

import {getRundeckContext, url} from '../../../rundeckService'

import RundeckLogo from '../../svg/RundeckLogo.vue'
import PagerdutyLogo from '../../svg/PagerdutyLogo.vue'

import Copyright from '../../version/Copyright.vue'
import RundeckVersion from '../../version/RundeckVersionDisplay.vue'
import VersionDisplay from '../../version/VersionIconNameDisplay.vue'
import ServerDisplay from '../../version/ServerDisplay.vue'
import {onMounted} from "vue";

const props = defineProps( {
    version: VersionInfo,
    latest: Release,
    server: ServerInfo,
    appInfo: AppInfo
})

    onMounted(() => {
        const context = getRundeckContext()
    })

    function welcomeUrl() {
        return url('menu/welcome').toString()
    }
</script>

<style scoped lang="scss">
.rundeck-info-widget {
    display: flex;
    align-content: center;
    flex-direction: column;
    padding: 15px;
    width: 400px;
    overflow: hidden;

    > div:not(:first-child) {
        margin-top: 10px;
        padding-top: 10px;
    }

    &__more-link {
        display: flex;
        width: 100%;
        align-content: center;
        align-items: center;
        justify-content: center;
        a {
            width: 80%;
            display: block;
        }
    }

    &__latest * {
        font-size: 18px;
    }
}

.rundeck-info-widget__group {
    > :not(:first-child) {
        margin-top: 10px;
    }
}

.rundeck-info-widget__heading {
    font-weight: bold;
}
.server-display{
    background-color: var(--grey-400);
    padding: 1px 8px;
    border-radius: 1em;
}

</style>