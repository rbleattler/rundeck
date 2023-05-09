// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import {createApp} from 'vue'
import VueCookies from 'vue-cookies'
import App from './App'
import {initI18n} from "../../utilities/i18n";

const els = document.body.getElementsByClassName('dynamic-form-vue')
const i18n = initI18n()

for (var i = 0; i < els.length; i++) {
  const el = els[i];

  /* eslint-disable no-new */
  const app = createApp({
    render(h) {
      return h(App, {
        props: {
          fields: this.$el.attributes.fields.value,
          options: this.$el.attributes.options.value,
          hasOptions: this.$el.attributes.hasOptions.value,
          element: this.$el.attributes.element.value,
          name: this.$el.attributes.name.value,
        }
      })
    },
  });
  app.use(VueCookies)
  app.use(i18n)
  app.mount(el)
}

