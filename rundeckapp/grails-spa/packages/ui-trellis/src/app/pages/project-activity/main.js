// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import {createApp} from 'vue'
import VueCookies from 'vue-cookies'
import ActivityList from '../../components/activity/activityList.vue'
import ActivityRunningIndicator from '../../components/activity/activityRunningIndicator.vue'
import * as uiv from 'uiv'
import international from './i18n'
import {createI18n} from 'vue-i18n'
import moment from 'moment'
import {
  EventBus
} from '../../../library/utilities/vueEventBus'

import uivLang from '../../../library/utilities/uivi18n'

let messages = international.messages
let locale = window._rundeck.locale || 'en_US'
let lang = window._rundeck.language || 'en'
moment.locale(locale)

// include any i18n injected in the page by the app
messages =
    {
      [locale]: Object.assign(
          {},
          uivLang[locale] || uivLang[lang] || {},
          window.Messages,
          messages[locale] || messages[lang] || messages['en_US'] || {}
      )
    }


const els = document.body.getElementsByClassName('vue-project-activity')

for (var i = 0; i < els.length; i++) {
  const e = els[i]

  // Create VueI18n instance with options
  const i18n = createI18n({
    silentTranslationWarn: true,
    locale: locale, // set locale
    messages // set locale messages,

  })
  /* eslint-disable no-new */
  let app = createApp({
    data(){
      return{
        EventBus: EventBus
      }
    },
    components: { ActivityList, ActivityRunningIndicator }
  })
  app.use(uiv)
  app.use(i18n)
  app.use(VueCookies)
  app.mount(e)
}
