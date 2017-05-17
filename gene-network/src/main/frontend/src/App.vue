<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12">
        <b-card show-header class="mb-2">
          <h2 align="center" slot="header" class="text-muted">Diagnostics platform <span class="badge badge-default">BETA VERSION</span>
          </h2>
          GeneNetwork Variant analysis.
          <span v-if="patientLabel != null">Currently viewing patient <strong>{{patientLabel}}</strong></span>
        </b-card>
      </div>
    </div>
    <alert-container v-if="showAlert"></alert-container>
    <div class="row main-nav">
      <div class="col">
        <b-nav tabs>
          <b-nav-item to="/patients">
            <a>Patients</a>
          </b-nav-item>
          <b-nav-item to="/upload">
            <a>Import data</a>
          </b-nav-item>
          <b-nav-item to="/setup">
            <a>Setup</a>
          </b-nav-item>
        </b-nav>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<style>
  .main-nav {
    padding-top: 1rem;
    padding-bottom: 2rem;
  }

  .card-header {
    background-color: #b5d592 !important;
  }

  .card-block {
    text-align: center;
  }
</style>

<script>
  import AlertContainer from './components/AlertContainer'
  import { FETCH_PATIENT_TABLES } from './store/actions'

  export default {
    name: 'molgenis-app',
    computed: {
      patientLabel: {
        get: function () {
          const entityTypeId = this.$route.params.entityTypeId
          if (entityTypeId === undefined || this.$store.state.patients.length === 0) {
            return null
          }

          const patient = this.$store.state.patients.find(function (patient) {
            return patient.id === entityTypeId
          })
          return patient.label
        }
      },
      showAlert: {
        get: function () {
          return this.$store.state.showAlert
        }
      },
      patients: {
        // TODO Add Delete button
        get: function () {
          return this.$store.state.patients
        }
      }
    },
    components: {
      AlertContainer
    },
    created: function () {
      this.$store.dispatch(FETCH_PATIENT_TABLES)
    }
  }
</script>
