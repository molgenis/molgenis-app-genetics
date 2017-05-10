<template>
  <div class="container">
    <div class="row">
      <div class="col-md-12">
        <b-card show-header class="mb-2">
          <h2 align="center" slot="header" class="text-muted">Diagnostics platform</h2>
          Diagnostics platform created with MOLGENIS, used by UMCG Genetics department.
          <span v-if="entityTypeId != null">Currently viewing patient <strong>{{entityTypeId}}</strong></span>
        </b-card>
      </div>
    </div>
    <alert-container v-if="showAlert"></alert-container>
    <div class="row">
      <div class="col-md-3">
        <b-nav tabs>
          <b-nav-item>
            <router-link to="/">
              <a>Home</a>
            </router-link>
          </b-nav-item>
          <b-nav-item>
            <router-link to="/upload">
              <a>Import data</a>
            </router-link>
          </b-nav-item>
        </b-nav>
        <b-nav vertical>
          <b-nav-item><span class="text-muted">Patients</span></b-nav-item>
          <b-nav-item v-for="patient in patients">
            <router-link :to="{ name: 'view', params: { entityTypeId: patient.id }}">{{patient.label}}</router-link>
          </b-nav-item>
        </b-nav>
      </div>
      <div class="col-md-9">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<style>
  .card-header {
    background-color: #b5d592!important;
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
