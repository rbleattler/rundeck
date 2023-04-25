import uivLang from '../../../library/utilities/uivi18n'
import {getRundeckContext, getSynchronizerToken, RundeckBrowser} from '../../../library'
import { EventBus } from '../../../library/utilities/vueEventBus'

import { RootStore } from '../../../library/stores/RootStore'

type UivLangKey = keyof typeof uivLang

const win = window as any
let locale: UivLangKey = win._rundeck.locale || 'en_US'
let lang: UivLangKey = win._rundeck.language || 'en'

win.Messages = {
    [lang]: {
        ...(win.Messages || {}),
        ...(uivLang[locale] || uivLang[lang])
    }
}

const context = getRundeckContext()
const token = getSynchronizerToken()

context.rundeckClient = new RundeckBrowser(token.TOKEN, token.URI, context.rdBase)
console.log("setting event bus object")
context.eventBus = EventBus
context.rootStore = new RootStore(context.rundeckClient, context.appMeta)