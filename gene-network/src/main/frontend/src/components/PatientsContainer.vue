<template>
  <div class="row">
    <div class="col-md-3" v-show="!collapsed">
      <b-nav vertical>
        <b-nav-item>
          <span class="text-muted">Patients</span>
          <b-button class="btn btn-sm collapse-button" @click="collapsed=true" style="float: right;">hide</b-button>
        </b-nav-item>
        <b-nav-item v-for="patient in patients">
          <router-link :to="{ name: 'patient-detail', params: { entityTypeId: patient.id }}">{{patient.label}}
          </router-link>
        </b-nav-item>
      </b-nav>
    </div>
    <div  class="col-lg-1" v-show="collapsed">
      <b-nav vertical>
        <b-nav-item>
          <b-button class="btn btn-sm collapse-button" @click="collapsed=false">show</b-button>
        </b-nav-item>
      </b-nav>
    </div>
    <div class="col">
      <h4>Patients</h4>
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
  import PhenotypeSelectionContainer from './PatientsContainer.vue'
  import AlertContainer from './AlertContainer'
  import { FETCH_PATIENT_TABLES } from '../store/actions'

  export default {
    name: 'patients-container',
    data: function () {
      return {
        collapsed: false,
        patientColumnStyle: {
          display: 'block'
        },
        columnWidth: 'col'
      }
    },
    computed: {
      entityTypeId: {
        get: function () {
          return this.$route.params.entityTypeId
        }
      },
      showAlert: {
        get: function () {
          return this.$store.state.showAlert
        }
      },
      patients: {
        get: function () {
          return this.$store.state.patients
        }
      }
    },
    components: {
      AlertContainer,
      PhenotypeSelectionContainer
    },
    created: function () {
      this.$store.dispatch(FETCH_PATIENT_TABLES)
    }
  }
</script>
