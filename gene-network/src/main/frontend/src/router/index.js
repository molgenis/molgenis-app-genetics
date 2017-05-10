import Vue from 'vue'
import VueRouter from 'vue-router'

import UploadContainer from 'components/UploadContainer'
import PhenotypeSelectionContainer from 'components/PhenotypeSelectionContainer'

Vue.use(VueRouter)
export default new VueRouter({
  mode: 'history',
  base: window.__INITIAL_STATE__.baseUrl,
  routes: [
    {
      path: '/',
      name: 'upload',
      component: UploadContainer
    },
    {
      path: '/view/:entityTypeId',
      name: 'view',
      component: PhenotypeSelectionContainer
    }
  ]
})
