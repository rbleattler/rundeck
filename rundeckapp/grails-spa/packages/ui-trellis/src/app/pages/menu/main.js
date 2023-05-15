// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import {createApp} from 'vue'
import * as uiv from 'uiv'
import VueCookies from 'vue-cookies'
import moment from 'moment'

import App from './App.vue'
import {initI18n} from "../../utilities/i18n"
import {getRundeckContext} from '../../../library'

let locale = window._rundeck.locale || 'en_US'
moment.locale(locale)

// Create VueI18n instance with options
const i18n = initI18n()

const app = createApp({
  components: {
    App
  },
  template: '<App/>'
})
app.use(VueCookies)
app.use(uiv)
app.use(i18n)
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