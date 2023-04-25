// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue, {createApp} from 'vue'
import * as uiv from 'uiv'
import VueCookies from 'vue-cookies'
import App from './App'
import VueI18n, {createI18n} from 'vue-i18n'
import international from './i18n'
import moment from 'moment'
import uivLang from '../../../library/utilities/uivi18n'
import {getRundeckContext} from '@/library'

let messages = international.messages
let language = window._rundeck.language || 'en_US'
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
// Create VueI18n instance with options
const i18n = createI18n({
  silentTranslationWarn: true,
  locale: locale, // set locale
  messages // set locale messages,

})

const app = createApp({
  components: {
    App
  },
  template: '<App/>'
})
app.use(VueCookies)
app.use(uiv)
app.use(i18n)
app.use(VueCookies)
app.mount('#user-summary-vue')

const rootStore = getRundeckContext().rootStore
rootStore.ui.addItems([
    {
        section: 'user-summary',
        location: 'main',
        visible: true,
        widget: app
    }
])