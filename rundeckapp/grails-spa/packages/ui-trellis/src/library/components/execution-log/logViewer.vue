<template>
  <div class="execution-log"
    :class="[`execution-log--${colorTheme()}`]"
  >
    <rd-drawer
        title="Settings"
        placement="left"
        :mask="false"
        :visible="settingsVisible"
        :closable="true"
        :get-container="false"
        :wrap-style="{ position: 'absolute' }"
        @close="() => {settingsVisible = false}"
    >
      <form style="padding: 10px;">
        <div class="form-group">
          <label>Theme</label>
          <select class="form-control select"
            v-model="settings.theme"
          >
            <option v-for="themeOpt in themes" :key="themeOpt.value" :value="themeOpt.value">{{themeOpt.label}}</option>
          </select>
        </div>
        <div class="checkbox">
          <input type="checkbox" v-model="settings.gutter" id="logview_gutter">
          <label for="logview_gutter">Display Gutter</label>
        </div>
        <div class="checkbox">
          <input type="checkbox" v-model="settings.timestamps" id="logview_timestamps">
          <label for="logview_timestamps">Display Timestamps</label>
        </div>
        <div class="checkbox" >
          <input type="checkbox" v-model="settings.command" id="logview_command">
          <label for="logview_command">Display Command</label>
        </div>
        <div class="checkbox">
          <input type="checkbox" v-model="settings.nodeBadge" id="logview_nodeBadge">
          <label for="logview_nodeBadge">Display Node Badge</label>
        </div>
        <div class="checkbox">
          <input type="checkbox" v-model="settings.ansiColor" id="logview_ansiColor">
          <label for="logview_ansiColor">Render ANSI Colors</label>
        </div>
        <div class="checkbox">
          <input type="checkbox" v-model="settings.lineWrap" id="logview_lineWrap">
          <label for="logview_lineWrap">Wrap Long Lines</label>
        </div>
        <div class="checkbox">
          <input type="checkbox" v-model="settings.stats" id="logview_stats">
          <label for="logview_stats">Display Stats</label>
        </div>
        <ui-socket section="execution-log-viewer" location="settings"
                   :event-bus="eventBus"/>
      </form>
    </rd-drawer>
    <div 
      ref="scroller"
      class="execution-log__scroller" v-bind:class="{
      'execution-log--no-transition': this.logLines > 1000,
      'ansicolor-on': settings.ansiColor
    }">
      <div ref="log">
        <div v-if="showSettings" class="execution-log__settings"  style="margin-left: 5px; margin-right: 5px;">
          <btn-group>
            <btn size="xs" @click="(e) => {settingsVisible = !settingsVisible; e.target.blur();}">
              <i class="fas fa-cog"/>Settings
            </btn>
            <btn size="xs" @click="(e) => {this.follow = !this.follow; e.target.blur();}">
              <i :class="[followIcon]"/>Follow
            </btn>
          </btn-group>
          <transition name="fade">
            <div class="execution-log__progress-bar" v-if="showProgress">
              <progress-bar v-model="barProgress" :type="progressType" :label-text="progressText" label min-width striped active @click="() => {this.consumeLogs = !this.consumeLogs}"/>
            </div>
          </transition>
        </div>
        <div class="execution-log__warning" v-if="overSize">
          <h3> 🐋 {{+(logSize / 1048576).toFixed(2)}}MiB is a whale of a log! 🐋 </h3>
          <h4> Select a download option above to avoid sinking the ship. </h4>
          <h5> Log content may be truncated </h5>
        </div>
        <div class="execution-log__warning" v-if="errorMessage">
          <h4>{{errorMessage}}</h4>
        </div>
        <div class="execution-log__warning" v-if="completed && logLines == 0">
          <h5>No output</h5>
        </div>
      </div>
    </div>
    <div class="stats" v-if="settings.stats">
      <span>Following:{{follow}} Lines:{{logLines.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}} Size:{{logSize}}b TotalTime:{{totalTime/1000}}s</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import {CancellationTokenSource, CancellationToken} from 'prex'

import {ExecutionLog, EnrichedExecutionOutput} from '../../utilities/ExecutionLogConsumer'
import Vue, {ComponentOptions, computed, onBeforeMount, onMounted, ref, watch} from 'vue'
import {LogBuilder} from './logBuilder'
import { RootStore } from '../../stores/RootStore'
import RdDrawer from '../containers/drawer/Drawer.vue'
import { ExecutionOutput, ExecutionOutputEntry } from '../../stores/ExecutionOutput'
import { Observer } from 'mobx-vue-lite'
import UiSocket from "../utils/UiSocket.vue";
import {getRundeckContext} from "../../rundeckService";
import {RundeckContext} from "../../interfaces/rundeckWindow";

const CONFIG_STORAGE_KEY='execution-viewer'

interface IEventViewerSettings {
  theme: string
  stats: boolean
  timestamps: boolean
  command: boolean
  gutter: boolean
  nodeBadge: boolean
  ansiColor: boolean
  lineWrap: boolean
}

const props = withDefaults(defineProps<{
    executionId: number
    node?: string
    stepCtx?: string
    showStats: boolean
    showSettings: boolean
    follow: boolean
    jumpToLine?: number
    theme?:string
    maxLogSize?: number
    trimOutput: number
    config?: IEventViewerSettings
    useUserSettings: boolean
}>(), {
    showStats: true,
    showSettings: true,
    follow: false,
    theme: 'dark',
    maxLogSize: 3145728,
    useUserSettings: true
})

    // @Inject({default: undefined})
    // private readonly executionLogViewerFactory?: (execId: string) => Promise<ExecutionLog>

    const rootContext = getRundeckContext() as RundeckContext
    const rootStore: RootStore = rootContext.rootStore
  
    const themes = [
      {label: 'Rundeck Theme', value: 'rundeck'},
      {label: 'Light', value: 'light'},
      {label: 'Dark', value: 'dark'},
      {label: 'None', value: 'none'}
    ]

    const scrollTolerance = 5

    const batchSize = 200

    const totalTime = 0

    const progress = 0

    const settings = ref<IEventViewerSettings>({
      theme: 'rundeck',
      stats: false,
      timestamps: false,
      command: true,
      gutter: true,
      ansiColor: true,
      nodeBadge: true,
      lineWrap: true
    })
    
    let eventBus: Vue

    watch(settings, (newVal: any, oldVal: any) => {
      if (props.useUserSettings && settingsVisible.value)
        saveConfig()
    }, {deep:true})

    watch(settingsChange, (newVal: IEventViewerSettings, oldVal: IEventViewerSettings) => {
      const updatableProps: Array<keyof IEventViewerSettings> = ['nodeBadge', 'gutter', 'timestamps', 'lineWrap', 'command']

      for (const prop of updatableProps) {
        if (newVal[prop] != oldVal[prop]) {
          this.$options.vues.forEach(v => v[prop] = newVal[prop])
        }
      }

      logBuilder.value.updateProps({
        node: this.node,
        stepCtx: this.stepCtx,
        nodeIcon: settings.value.nodeBadge,
        maxLines: 20000,
        command: {
          visible: settings.value.command
        },
        time: {
          visible: settings.value.timestamps
        },
        gutter: {
          visible: settings.value.gutter
        },
        content: {
          lineWrap: settings.value.lineWrap
        }
      })
    },{deep: true})

    const consumeLogs = ref<boolean>(true)

    const completed = false

    const errorMessage = ''

    const execCompleted = true

    const settingsVisible = ref<boolean>(false)

    const overSize = false

    const jumped = false

    const viewer = ref<ExecutionOutput>()

    const logBuilder = ref<LogBuilder>()

    const logEntries = ref<Array<{log?: string, id: number}>>([])

    const scrollCount = 0

    const startTime = 0

    const logSize = ref(0)

    const logLines = 0

    const resp = ref<Promise<ExecutionOutputEntry[]>>

    const populateLogsProm = ref<Promise<void>>()

    const cancelProgress = ref<CancellationTokenSource>()

    const nextProgress = 0

    const selected: any

    const percentLoaded: number = 0

    const options = ref<ComponentOptions<Vue> & {
        vues: any[]
    }>()
  
    const settingsChange = computed(() => {
      return Object.assign({}, settings.value)
    })

    const followIcon = computed(() => {
      return this.follow ? 'fas fa-eye' : 'fas fa-eye-slash'
    })

    const barProgress= computed(() => {
      return this.execCompleted ? this.progress : 100
    })

    const progressType= computed(() => {
      return consumeLogs.value ? 'info' : 'warning'
    })

    const progressText= computed(() => {
      const loadingText = `${barProgress}% ${consumeLogs.value ? 'Pause' : 'Resume'}`
      const runningText = `${consumeLogs.value ? 'Pause' : 'Resume'}`

      return this.execCompleted ? loadingText : runningText
    })

    const showProgress = computed<boolean>(() => {
      return (!this.completed || !this.execCompleted)
    })
    

    watch(logSize, (val: number, oldVal: number) => {
      if (val > this.maxLogSize) {
        this.overSize = true
        this.nextProgress = 100
        this.completed = true
      }
    },{})

    watch(consumeLogs, (val: boolean, oldVal: boolean) => {
      if(val)
        this.updateProgress()
      else if(this.cancelProgress)
        this.cancelProgress.cancel()

      if(val && !this.populateLogsProm) {
        this.populateLogsProm = this.populateLogs()
      }
    },{})

    onBeforeMount(() => {
      /* Get event bus*/
      eventBus = rootContext.eventBus
      /** Load here so theme does not change afer visible */
      loadConfig()
    })

    onMounted(async () => {
        options.value.vues = []

        const scroller = this.$refs["scroller"] as HTMLElement
        const log = this.$refs["log"] as HTMLElement

        this.viewer = rootStore.executionOutputStore.createOrGet(this.executionId.toString())

        this.logBuilder = new LogBuilder(this.viewer, log, this.eventBus,{
          node: this.node,
          stepCtx: this.stepCtx,
          nodeIcon: settings.value.nodeBadge,
          maxLines: 20000,
          command: {
            visible: settings.value.command
          },
          time: {
            visible: settings.value.timestamps
          },
          gutter: {
            visible: settings.value.gutter
          },
          content: {
            lineWrap: settings.value.lineWrap
          }
        })

        this.logBuilder.onNewLines(this.handleNewLine)

        this.startTime = Date.now()
        this.addScrollBlocker()

        this.updateProgress()

        const {viewer} = this
        await viewer.init()

        this.execCompleted = viewer.execCompleted
        this.follow = !viewer.execCompleted

        if (viewer.execCompleted && viewer.size > this.maxLogSize) {
          this.logSize = viewer.size
          this.nextProgress = 0
          this.updateProgress(100)
          return
        }

        this.populateLogsProm = this.populateLogs()
    })

    function loadConfig() {
      if (props.useUserSettings) {
        const _settings = localStorage.getItem(CONFIG_STORAGE_KEY)

        if (_settings) {
          try {
            const config = JSON.parse(_settings)
            Object.assign(settings.value, config)
          } catch (e) {
            localStorage.removeItem(CONFIG_STORAGE_KEY)
          }
        }
      }

      Object.assign(settings.value, props.config || {})
    }

    function saveConfig() {
      localStorage.setItem(CONFIG_STORAGE_KEY, JSON.stringify(settings.value))
    }

    /**
     * Allows us to prevent scrolling unless a certain amount of "resistence" is produced
     */
    function addScrollBlocker() {
        const scroller = this.$refs["scroller"] as HTMLElement
        scroller.addEventListener('wheel', (ev: UIEvent) => {
            this.scrollCount++

            if (this.follow) {
                ev.preventDefault()
                ev.returnValue = false
            }

            if (this.scrollCount > this.scrollTolerance)
                this.follow = false
        }, {passive: false})
    }

    function handleExecutionLogResp() {
      if (!this.trimOutput) return

      if (this.viewer.offset > this.trimOutput) {
        const removeSize = this.logBuilder.dropChunk()
        for (let x = 0; x < removeSize; x++) {
          const scapeGoat = this.$options.vues.shift()
        }
      }
    }

    function updateProgress(delay: number = 0) {
      if (this.cancelProgress)
        this.cancelProgress.cancel()

      this.cancelProgress = new CancellationTokenSource();

      (async (cancel: CancellationToken) => {
        const update = () => {this.progress = this.nextProgress}
        setTimeout(update, delay)
        while(!cancel.cancellationRequested) {
          await new Promise((res, rej) => {setTimeout(res, 1000)})
          if (this.progress == 100)
            this.cancelProgress?.cancel()
          update()
        }
      })(this.cancelProgress.token)
    }

    function scrollToLine(n: number | string) {
        const scroller = this.$refs["scroller"] as HTMLElement

        const target = options.value.vues[Number(n)-1].$el
        let parent = target.parentNode

        let offset = target.offsetTop

        // Traverse to root and accumulate offset
        while (parent != scroller) {
          offset += parent.offsetTop
          parent = parent.parentNode
        }

        scroller.scrollTop = offset - 24 // Insure under stick header
    }

    /**
     * Handle line select events from the log entries and re-emit.
     * Emits a line-deselect if the event is for the currently selected line.
     */
    function handleLineSelect(e: any) {
      if (this.overSize) {
        alert('Line-linking is not supported for over-sized logs')
        return
      }

      const line = options.value.vues[e-1]

      if (this.selected) {
        this.selected.selected = false
      }

      if (this.selected === line) {
        this.selected = undefined
        this.$emit('line-deselect', e)
        return
      }

      line.selected = true
      this.selected = line

      this.$emit('line-select', e)
    }

    function handleNewLine(entries: Array<any>) {
      for (const vue of entries) {
        // @ts-ignore
        const selected = vue.logEntry.lineNumber == this.jumpToLine
        vue.$on('line-select', this.handleLineSelect)
        if (selected) {
          this.selected = vue
          vue.selected = true
        }
      }

      options.value.vues.push(...entries)

      if (this.jumpToLine && this.jumpToLine <= options.value.vues.length && !this.jumped) {
        this.follow = false
        this.scrollToLine(this.jumpToLine)
        this.jumped = true
      }

      if (this.follow) {
        this.scrollToLine(options.value.vues.length)
      }
    }

    function handleJump(e: string) {
        this.scrollToLine(this.jumpToLine || 0)
    }

    function handleJumpToEnd() {
        this.scrollToLine(options.value.vues.length)
    }

    function handleJumpToStart() {
        this.scrollToLine(1)
    }

    async function populateLogs() {
        while(this.consumeLogs) {
            if (!this.resp)
              this.resp = this.viewer.getOutput(this.batchSize)
            const res = await this.resp
            this.resp = undefined

            if (!this.viewer.completed) {
              this.resp = this.viewer.getOutput(this.batchSize)
              await new Promise<void>((res, rej) => setTimeout(() => {res()},0))
            }

            this.execCompleted = this.viewer.execCompleted
            this.completed = this.viewer.completed

            this.nextProgress = Math.round(this.viewer.percentLoaded)

            if (this.viewer.error)
              this.errorMessage = this.viewer.error

            this.logSize = this.viewer.offset
            this.logLines = this.viewer.entries.length
            this.handleExecutionLogResp()

            if (this.viewer.completed)
                break
        }
        this.totalTime = Date.now() - this.startTime
        this.populateLogsProm = undefined
    }

    function colorTheme() {
      if (settings.value.theme == 'rundeck')
        return rootStore.theme.theme
      else
        return settings.value.theme

    }

</script>

<style lang="scss">
@import './ansi.css';
@import './theme-light.scss';
@import './theme-dark.scss';

.anticon {
  display: inline-block;
  color: inherit;
  font-style: normal;
  line-height: 0;
  text-align: center;
  text-transform: none;
  vertical-align: -0.125em;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.execution-log__progress-bar {
  display: inline-block;
  height: min-content;
  flex: 1;
  cursor: pointer;
}

.execution-log__progress-bar * {
  transition: all .3s ease;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
}
.fade-enter, .fade-leave-to * {
  opacity: 0;
}

.execution-log--no-transition * {
  transition: none !important;
}

.execution-log__node-chunk * {
  transition: all 0.3s ease;
}

.execution-log__node-chunk {
  contain: layout;
}

.execution-log__chunk {
  contain: layout;
}

.progress{
  height: 20px;
  margin:0;
}

</style>

<style lang="scss" scoped>

.execution-log {
  display: flex;
  flex-direction: column;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.execution-log__settings {
  display: flex;
  align-items: center;
  position: sticky;
  top: -2px;
  z-index: 1;
  transition: top .3s;

  .btn i {
    margin-right: 5px;
  }
}

.execution-log__settings:hover, .execution-log__settings:focus-within {
  top: -2px;
}

.execution-log__stats {
  flex: 0 0 1em;
}

.execution-log__scroller {
  // height: 100%;
  overflow-y: auto;
  /** Causing FireFox scroll issues: translate3d(0,0,0) fixes but conflicts with modals */
  // will-change: transform;
  width: 100%;
  flex: 1;
}

.execution-log__warning {
  width: 100%;
  text-align: center;
}

</style>
