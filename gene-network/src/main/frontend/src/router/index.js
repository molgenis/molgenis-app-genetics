import Vue from 'vue'
import VueRouter from 'vue-router'

import HomeContainer from 'components/HomeContainer'
import UploadContainer from 'components/UploadContainer'
import PhenotypeSelectionContainer from 'components/PhenotypeSelectionContainer'
import PatientsContainer from 'components/PatientsContainer'
import PatientsHelpText from 'components/PatientsHelpText'
import { INITIAL_STATE } from '../store/state'

Vue.use(VueRouter)
export default new VueRouter({
  mode: 'history',
  base: INITIAL_STATE.baseUrl,
  routes: [
    {
      path: '/home',
      name: 'home',
      component: HomeContainer
    },
    {
      path: '/upload',
      name: 'upload',
      component: UploadContainer
    },
    {
      path: '/patients',
      name: 'patients',
      component: PatientsContainer,
      children: [{
        path: ':entityTypeId',
        name: 'patient-detail',
        component: PhenotypeSelectionContainer
      },
      {
        path: '',
        name: 'patient-help',
        component: PatientsHelpText
      }]
    },
    // catch all redirect
    { path: '/', redirect: '/home' }
  ]
})
