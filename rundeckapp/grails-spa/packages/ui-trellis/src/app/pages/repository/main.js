// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue, {createApp} from 'vue'
import VueCookies from 'vue-cookies'
import VueScrollTo from 'vue-scrollto'
import VueFuse from 'vue-fuse'
import VueI18n, {createI18n} from 'vue-i18n'
import uivLang from '../../../library/utilities/uivi18n'
import * as uiv from 'uiv'

import store from './stores'
import router from './router'
import App from './App'

let locale = window._rundeck.locale || 'en_US'
let lang = window._rundeck.language || 'en'

// include any i18n injected in the page by the app
let messages = {
  [locale]: Object.assign({},
    uivLang[locale] || uivLang[lang] || {},
    window.Messages
  )
}

const i18n = createI18n({
  silentTranslationWarn: true,
  locale, // set locale
  messages // set locale messages,
})

/* eslint-disable no-new */
const app = createApp({
  name: "RepositoryApp",
  components: {
    App
  },
  template: '<App/>'
})
app.use(store)
app.use(router)
app.use(VueCookies)
app.use(VueScrollTo)
app.use(VueFuse)
app.use(uiv)
app.use(i18n)
app.mount('#repository-vue')
