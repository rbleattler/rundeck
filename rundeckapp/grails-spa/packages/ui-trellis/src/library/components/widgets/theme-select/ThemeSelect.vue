<template>
    <div style="padding: 20px;">
        <div class="form-group">
          <label>Theme</label>
          <select class="form-control select"
            v-model="theme"
          >
            <option v-for="themeOpt in themes" :key="themeOpt">{{themeOpt}}</option>
          </select>
        </div>
    </div>
</template>

<script setup lang="ts">
import  {onBeforeMount, ref, watch} from 'vue'

import { ThemeStore } from '../../../stores/Theme'
import {getRundeckContext} from "../../../rundeckService";
import {RundeckContext} from "../../../interfaces/rundeckWindow";

    const themes = ['system', 'light', 'dark']

    const theme = ref<string>('')

    const themeStore = ref<ThemeStore>()

    onBeforeMount(() => {
        themeStore.value = (getRundeckContext() as RundeckContext).rootStore.theme
        this.theme = themeStore.value.userPreferences.theme!
    })

   watch(theme, (newVal: any) => {
       themeStore.value.setUserTheme(newVal)
   })

</script>