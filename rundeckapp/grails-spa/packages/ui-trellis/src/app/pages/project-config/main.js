// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import {createApp} from 'vue'
import VueCookies from 'vue-cookies'
import PageConfirm from '../../../library/components/utils/PageConfirm'
import * as uiv from 'uiv'
import {createI18n} from 'vue-i18n'
import international from './i18n'
import uivLang from '../../../library/utilities/uivi18n'
import {getRundeckContext} from '../../../library'
import PluginSetConfig from './PluginSetConfig'
import ProjectPluginGroups from "./ProjectPluginGroups";

let messages = international.messages
let locale = window._rundeck.locale || 'en_US'
let lang = window._rundeck.language || 'en'

// include any i18n injected in the page by the app
messages = {
    [locale]: Object.assign({},
        uivLang[locale] || uivLang[lang] || {},
        window.Messages,
        messages[locale] || messages[lang] || messages['en_US'] || {}
    )
}
const context = getRundeckContext()
// Create VueI18n instance with options
/* eslint-disable no-new */
const els = document.body.getElementsByClassName('project-config-plugins-vue')

for (var i = 0; i < els.length; i++) {
    const e = els[i]

    const i18n = createI18n({
        silentTranslationWarn: false,
        locale: locale, // set locale
        messages // set locale messages,

    })
    const app = createApp({
        data() {
            return {
                EventBus: context.eventBus
            }
        },
        components: {
            PageConfirm,
            PluginSetConfig,
            ProjectPluginGroups
        }
    })
    app.use(VueCookies)
    app.use(uiv)
    app.use(i18n)
    app.mount(e)
}
