<template>
  <div class="row">
    <div class="col">
      <div v-bind:style="patientColumnStyle" class="col-md-12">
        <b-dropdown text="Select a patient" class="m-md-3">
          <b-dropdown-item v-for="patient in patients" @click="test(patient.id)">
            {{patient.label}}
          </b-dropdown-item>
        </b-dropdown>

        <router-view></router-view>
      </div>
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
  import {FETCH_PATIENT_TABLES} from '../store/actions'

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
      },
      test: function (patient) {
        this.$router.push({name: 'patient-detail', params: {entityTypeId: patient}})
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
