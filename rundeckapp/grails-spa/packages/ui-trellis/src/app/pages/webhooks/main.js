import Vue, {createApp} from 'vue'
import VueCookies from 'vue-cookies'
import VueScrollTo from 'vue-scrollto'
import VueFuse from 'vue-fuse'
import * as uiv from 'uiv'
import App from './App.vue'
import VueI18n, {createI18n} from 'vue-i18n'

import {getRundeckContext} from '@/library'
import uivLang from '../../../library/utilities/uivi18n'

import AceEditor from '../../../library/components/utils/AceEditor.vue'

import international from './i18n'

const rootStore = getRundeckContext().rootStore

let messages = international.messages
let language = window._rundeck.language || 'en_US'
let locale = window._rundeck.locale || 'en_US'
let lang = window._rundeck.language || 'en'

messages =
  {
    [locale]: Object.assign(
      {},
      uivLang[locale] || uivLang[lang] || {},
      window.Messages,
      messages[locale] || messages[lang] || messages['en_US'] || {}
    )
  }

const i18n = createI18n({
  silentTranslationWarn: true,
  locale: locale, // set locale
  messages // set locale messages,
})

// eslint-disable-next-line no-new
const app = createApp({
  name:"WebhookApp",
  components: { App },
  provide: {rootStore},
  template: "<App/>"
})
app.component('rd-ace-editor', AceEditor)
app.use(VueCookies)
app.use(VueScrollTo)
app.use(VueFuse)
app.use(uiv)
app.use(i18n)
app.mount('#webhook-vue')