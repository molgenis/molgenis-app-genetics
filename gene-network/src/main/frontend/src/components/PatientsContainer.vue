<template>
  <div class="row">
    <div class="col-md-3">
      <b-nav vertical>
        <b-nav-item><span class="text-muted">Patients</span></b-nav-item>
        <b-nav-item v-for="patient in patients">
          <router-link :to="{ name: 'patient-detail', params: { entityTypeId: patient.id }}">{{patient.label}}
          </router-link>
          <b-btn class="patient-delete-btn" size="sm" variant="danger" @click="deletePatient(patient)">remove</b-btn>
        </b-nav-item>
      </b-nav>
    </div>
    <div class="col-md-9">
      <router-view></router-view>
    </div>
  </div>
</template>
<style>
  .patient-delete-btn {
    float: right
  }
</style>
<script>
  import PhenotypeSelectionContainer from './PatientsContainer.vue'
  import AlertContainer from './AlertContainer'
  import { FETCH_PATIENT_TABLES, DELETE_PATIENT } from '../store/actions'

  export default {
    name: 'patients-container',
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
    methods: {
      deletePatient: function (patient) {
        this.$store.dispatch(DELETE_PATIENT, patient.id)
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
