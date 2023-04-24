<template>
    <div class="execution-log__line" v-bind:class="{'execution-log__line--selected': selected}">
        <div v-if="displayGutter" class="execution-log__gutter" v-on:click="lineSelect"
            v-bind:class="{
                'execution-log__gutter--slim': (timestamps && !command)
            }"
        >
            <span class="gutter line-number">
                <span class="execution-log_gutter-entry" :pseudo-content="timestamps ? logEntry.time : ''" />
                <!-- {{timestamps ? logEntry.time : ''}} -->
                <i v-if="command" class="rdicon icon-small" v-bind:class="[logEntry.stepType]"></i>
                <span class="execution-log_gutter-entry" :pseudo-content="command ? logEntry.stepLabel : ''" />
                <!-- {{command ? logEntry.stepLabel : ''}} -->
            </span>
        </div>
        <div class="execution-log__content" v-bind:class="[`execution-log__content--level-${logEntry.level.toLowerCase()}`,
            {
                'execution-log__content--html': logEntry.logHtml
            }]"
        >
            <ui-socket section="execution-log-line" location="badges" :event-bus="eventBus" :socket-data="{
              prevEntry: this.prevEntry,
              logEntry: this.logEntry
            }"/>
            <span v-if="displayNodeBadge" class="execution-log__node-badge"><i class="fas fa-hdd"/><span :pseudo-content="logEntry.node" /></span>
            <span v-if="logEntry.logHtml" class="execution-log__content-text" v-bind:class="{'execution-log__content-text--overflow': !lineWrap}" v-html="logEntry.logHtml"/>
            <span v-if="!logEntry.logHtml" class="execution-log__content-text" v-bind:class="{'execution-log__content-text--overflow': !lineWrap}">{{logEntry.log}}</span
        ></div
    ></div>
</template>

<script setup lang="ts">
import Vue, {computed, onBeforeMount, ref} from 'vue'
import UiSocket from '../utils/UiSocket.vue'
import {IBuilderOpts} from "./logBuilder"

const props = withDefaults(defineProps<{
    eventBus?: Vue
    selected?: boolean
    config: IBuilderOpts
    prevEntry?: any
    logEntry: any
}>(), {
    selected: false
})

const cfg = ref<IBuilderOpts>(props.config)

    const timestamps = computed(() => {
      return (cfg.value.time?.visible) ? cfg.value.time.visible : false
    })
  
    const command = computed(() => {
      return (cfg.value.command?.visible) ? cfg.value.command.visible : false
    })
  
    const gutter= computed(() => {
      return (cfg.value.gutter?.visible) ? cfg.value.gutter.visible : false
    })
  
    const nodeBadge= computed(() => {
      return (cfg.value.nodeIcon) ? cfg.value.nodeIcon : false
    })
  
    const lineWrap= computed(() => {
      return (cfg.value.content?.lineWrap) ? cfg.value.content.lineWrap : false
    })
  
    const displayNodeBadge= computed(() => {
        return cfg.value.nodeIcon && (this.prevEntry == undefined || this.logEntry.node != this.prevEntry.node)
    })

    const displayGutter= computed(() => {
        return cfg.value.gutter?.visible && (cfg.value.time?.visible || cfg.value.command?.visible)
    })

    const emit = defineEmits(['line-select'])

    function lineSelect() {
        emit('line-select', props.logEntry.lineNumber)
    }

    onBeforeMount(() => {
      props.eventBus.$on("execution-log-settings-changed", this.handleSettingsChanged)
    })

    function handleSettingsChanged(newSettings: any) {
      Object.assign(cfg.value, newSettings);
    }


</script>

<style lang="scss" scoped>
/** Only place critical layout and control here!
 * Theming is handled at a higher level!
 */

.execution-log__line {
  display: flex;
  width: 100%;
  font-family: monospace;
}

.execution-log__gutter {
    flex-shrink: 0;
    flex-grow: 0;
    flex-basis: 200px;
    user-select: none;
    display: flex;
    align-items: center;
    min-width: 0;

    & i {
        margin-left: 0.5em;
        margin-right: 0.2em;
    }
}

.execution-log__gutter--slim {
    flex-basis: 100px;
}

.execution-log__gutter span {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}

.execution-log__content {
    flex-grow: 1;
    white-space: pre-wrap;
    min-width: 0;
}

.execution-log__content-text {
    word-break: break-word;
}

.execution-log__content-text--overflow {
    overflow: hidden;
    white-space: pre;
    text-overflow: ellipsis;
    display: block;
    word-break: normal;
}

.execution-log__content--html {
    flex-grow: 1;
    white-space: pre-wrap;
}

.execution-log__line-number {
    cursor: pointer;
}

.execution-log__node-badge {
    float: right;
    user-select: none;
    & i {
        margin-left: 0.2em;
        margin-right: 0.2em;
    }
}

[pseudo-content]::before {
    content: attr(pseudo-content);
}

</style>
