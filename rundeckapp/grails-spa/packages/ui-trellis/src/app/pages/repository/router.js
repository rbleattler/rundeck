import Vue from 'vue';
import Router, {createRouter, createWebHistory} from 'vue-router';

import PluginRepositoryView from "./views/PluginRepositoryView"
import PluginConfigurationView from './views/PluginConfigurationView'
import UploadPluginView from './views/UploadPluginView'

var ctx = window._rundeck.context;
if(ctx !== "/") ctx += "/";

const router = createRouter({
  history: createWebHistory(),
  linkExactActiveClass: 'is-active',
  routes: [{
      path: ctx+'artifact/index/repositories',
      name: 'repositories',
      component: PluginRepositoryView
    },
    {
      path: ctx+'artifact/index/configurations',
      name: 'configurations',
      component: PluginConfigurationView
    },
    {
      path: ctx+'artifact/index/upload',
      name: 'upload',
      component: UploadPluginView
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: ctx+'artifact/index/configurations'
    }
  ]
});
export default router