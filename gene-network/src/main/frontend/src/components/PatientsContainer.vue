<template>
  <div class="row">
    <div v-bind:style="patientColumnStyle" class="col-md-3">
      <b-nav vertical>
        <b-nav-item><span class="text-muted">Patients</span></b-nav-item>
        <b-nav-item v-for="patient in patients">
          <router-link :to="{ name: 'patient-detail', params: { entityTypeId: patient.id }}">{{patient.label}}
          </router-link>
        </b-nav-item>
      </b-nav>
    </div>
    <div class="col">
      <img class="collapse-button " @click="collapse" width="40" height="40" src="../assets/collapse_icon.png">
      <router-view></router-view>
    </div>
  </div>
</template>

<style>
  .collapse-button {
    float: right
  }
</style>

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
    methods: {
      collapse: function () {
        this.collapsed = !this.collapsed
        if (this.collapsed) {
          this.patientColumnStyle.display = 'none'
        } else {
          this.patientColumnStyle.display = 'block'
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
