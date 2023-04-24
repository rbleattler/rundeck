<!--
  - Copyright 2018 Rundeck, Inc. (http://rundeck.com)
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <div>
    <btn @click="modalOpen=true" :class="btnClass" :size="btnSize" :type="btnType">
      <slot>Choose A Job &hellip;</slot>
    </btn>

    <modal v-model="modalOpen" :title="'Choose A Job'" ref="modal" append-to-body :size="size">

      <div v-if="showProjectSelector"><label>Project:</label><project-picker v-model="project"></project-picker></div>

      <div v-if="showScheduledToggle" class="form-group">

        <select v-model="filterType" id="_job_config_picker_scheduled_filter" class="form-control">
          <option value="">All Jobs</option>
          <option value="scheduled">Scheduled Jobs</option>
          <option value="notscheduled">Non-Scheduled Jobs</option>
        </select>

      </div>
      <div class="list-group" v-for="(item,name) in jobTree.groups" :key="'group'+name">
        <div class="list-group-item" v-if="name && item.jobs.length>0">
           <h4 class="list-group-item-heading">{{item.label}}</h4>
        </div>
        <div v-for="job in item.jobs" :key="job.id"
             class="list-group-item"
             style="overflow:hidden; text-overflow: ellipsis; white-space: nowrap"
             >

             <a href="#" class="" :title="'Choose this job: '+job.id"
                   @click="selectedJob=job">
                 <i class="glyphicon glyphicon-book"></i>
                 {{job.name}}
             </a>


            <span class="text-secondary">
              {{job.description}}
            </span>
            <span class="text-muted" v-if="job.scheduled">
              <i class="glyphicon glyphicon-time"></i>
            </span>

        </div>
      </div>
      <template v-slot:footer>
      <div>
        <btn @click="modalOpen=false">Cancel</btn>
      </div>
      </template>

    </modal>
  </div>
</template>
<script setup lang="ts">
import {onMounted, ref, watch} from 'vue'
import ProjectPicker from './ProjectPicker.vue'
import { JobTree } from '../../types/JobTree'
import { Job } from '@rundeck/client/dist/lib/models'
import { client } from '../../modules/rundeckClient'

const props = defineProps({
    'modelValue': {
        type: String,
        required: false,
        default: ''
    },
    'size': {
        type: String,
        required: false,
        default: ''
    },
    'btnType': {
        type: String,
        required: false,
        default: ''
    },
    'btnSize': {
        type: String,
        required: false,
        default: ''
    },
    'btnClass': {
        type: String,
        required: false,
        default: ''
    },
    'showScheduledToggle': {
        type: Boolean,
        required: false,
        default: true
    },
    'showScheduledDefault': {
        type: Boolean,
        required: false,
        default: false
    }
})

let selectedJob = ref<Job | null>(null)
let modalOpen = ref<boolean>(false)
let jobs = ref<Job[]>([])
let jobTree = ref(new JobTree())
let project = ref('')
let showProjectSelector = ref(true)
let filterType = ref<string>(props.showScheduledDefault?'scheduled':'')

watch([project, filterType], () => {
    if(project.value != '') {
        let params:{[name: string ] : any} = {}

        if(filterType.value!=''){
            params['scheduledFilter']=(filterType.value==='scheduled')
        }

        client.jobList(project.value,params ).then(result => {
            jobTree.value = new JobTree()
            jobs.value =  result
            jobs.value.forEach(job => jobTree.value.insert(job))
        })
    }
})

const emit = defineEmits(['update:modelValue'])

watch(selectedJob, () => {
    modalOpen.value = false
    emit('update:modelValue', selectedJob.value ? selectedJob.value.id : '')
})

onMounted(() => {
    //@ts-ignore
    if(window._rundeck.projectName) {
        showProjectSelector.value = false
        //@ts-ignore
        project.value = window._rundeck.projectName
    }
})

</script>
<style lang="scss">
</style>
