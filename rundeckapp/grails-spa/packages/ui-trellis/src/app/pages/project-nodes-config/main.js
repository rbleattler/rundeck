// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import {createApp} from 'vue'
import VueCookies from 'vue-cookies'
import * as uiv from 'uiv'

import ProjectPluginConfig from './ProjectPluginConfig'
import ProjectNodeSourcesConfig from './ProjectNodeSourcesConfig'
import ProjectNodeSourcesHelp from './ProjectNodeSourcesHelp'
import WriteableProjectNodeSources from './WriteableProjectNodeSources'
import PageConfirm from '../../../library/components/utils/PageConfirm.vue'
import {getRundeckContext} from '../../../library'
import {initI18n} from "../../utilities/i18n"


// include any i18n injected in the page by the app

const context = getRundeckContext()
// Create VueI18n instance with options
const els = document.body.getElementsByClassName('project-plugin-config-vue')

for (var i = 0; i < els.length; i++) {
  const e = els[i]
  const i18n = initI18n()

  const app = createApp({
    name: "ProjectNodeApp",
    data() {
      return {
        EventBus: context.eventBus
      }
    },
    components: {
      ProjectPluginConfig,
      ProjectNodeSourcesConfig,
      WriteableProjectNodeSources,
      ProjectNodeSourcesHelp,
      PageConfirm
    }
  })
  app.use(VueCookies)
  app.use(uiv)
  app.use(i18n)
  app.mount(e)

}
