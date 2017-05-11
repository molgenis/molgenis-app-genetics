import Vue from 'vue'
import VueRouter from 'vue-router'

import HomeContainer from 'components/HomeContainer'
import UploadContainer from 'components/UploadContainer'
import PhenotypeSelectionContainer from 'components/PhenotypeSelectionContainer'
import { INITIAL_STATE } from '../store/state'

Vue.use(VueRouter)
export default new VueRouter({
  mode: 'history',
  base: INITIAL_STATE.baseUrl,
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeContainer
    },
    {
      path: '/upload',
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
