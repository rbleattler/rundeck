import {createApp} from 'vue'
import VueCookies from 'vue-cookies'
import VueScrollTo from 'vue-scrollto'
import * as uiv from 'uiv'

import App from './App.vue'
import {createI18n} from 'vue-i18n'
import {getRundeckContext} from '@/library'
import uivLang from '../../../library/utilities/uivi18n'
import AceEditor from '../../../library/components/utils/AceEditor.vue'
import international from './i18n'

const rootStore = getRundeckContext().rootStore

let messages = international.messages
let locale = window._rundeck.locale || 'en_US'
let lang = window._rundeck.language || 'en'

messages =
  {
    [locale]: Object.assign(
      {},
      uivLang[locale] || uivLang[lang] || {},
      messages[locale] || messages[lang] || messages['en_US'] || {}
    )
  }

const i18n = createI18n({
  silentTranslationWarn: true,
  locale: locale, // set locale
  messages // set locale messages,
})

const app = createApp({
  name:"WebhookApp",
  components: { App },
  provide: {rootStore},
  template: "<App/>"
})
app.component('rd-ace-editor', AceEditor)
app.use(VueCookies)
app.use(VueScrollTo)
app.use(uiv)
app.use(i18n)
app.mount('#webhook-vue')
