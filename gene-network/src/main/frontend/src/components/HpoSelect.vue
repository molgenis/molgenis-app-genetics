<template>
  <div class="form phenotype-selection-container">
    <h3 for="selected-phenotype-list">Phenotypes</h3>
    <div class="row form-group">
      <div class="col-md-12">
        <ul id="selected-phenotype-list">
          <li v-for="filter in selectedOptions" class="row">
            <div class="col">
              {{ filter.label }}
            </div>
            <b-form-checkbox :checked="filter.isActive" @change="activationChanged(filter.id)" class="col">
              active
            </b-form-checkbox>
          </li>
          <li v-show="selectedOptions.length === 0" class="row"><em>No phenotypes selected</em></li>
        </ul>
      </div>
    </div>

    <div class="row form-group">
      <div class="col">
        <label for="hpo-select">Select phenotypes retrieve gene network scores for every variant. <br>Variants are sorted based on <em>cumulative</em> gene network score.</label>
        <v-select
          id="hpo-select"
          :on-search="queryOntologies"
          :options="phenotypes"
          :value.sync="selectedOptions"
          multiple
          placeholder="Search HPO Ontology..."
          label="label"
        >
        </v-select>
      </div>
    </div>
  </div>
</template>

<script>
  import vSelect from 'vue-select'
  import { get } from '../molgenisApi'
  import {
    SET_PHENOTYPE_FILTERS,
    TOGGLE_ACTIVE_PHENOTYPE_FILTERS,
    CREATE_ALERT,
    REMOVE_GENE_NETWORK_SCORES,
    UPDATE_VARIANT_SCORES,
    CLEAR_GENE_NETWORK_SCORES
  } from '../store/mutations'
  import { COMPUTE_SCORE } from '../store/actions'

  export default {
    name: 'hpo-select',
    data: function () {
      return {
        phenotypes: [],
        selectedOptions: []
      }
    },
    computed: {
      phenotypeFilters: {
        get: function () {
          return this.$store.state.phenotypeFilters
        }
      }
    },
    methods: {
      queryOntologies (query) {
        // FIXME Searching for HP_0100280 retrieves all items.
        // FIXME Searching for 0100280 does retrieve 1 item but it is not shown in dropdown
        get(this.$store.state.session.server, '/v2/sys_ont_OntologyTerm?q=ontology.ontologyName=like=hp_;(ontologyTermName=q=' + query + ',ontologyTermSynonym.ontologyTermSynonym=q=' + query + ',ontologyTermIRI=q=' + query + ')')
          .then(response => {
            this.phenotypes = response.items.map(function (phenotype) {
              const ontologyTermIRI = phenotype.ontologyTermIRI
              const phenotypeId = ontologyTermIRI.substring(ontologyTermIRI.lastIndexOf('/') + 1).replace(':', '_')
              return {'id': phenotypeId, 'label': phenotype.ontologyTermName, 'isActive': true}
            })
          })
      },
      selectionChanged (selectedPhenotypeFilters) {
        const previousFilters = this.$store.state.phenotypeFilters
        this.$store.commit(SET_PHENOTYPE_FILTERS, selectedPhenotypeFilters)

        const phenotypeFilter = previousFilters
          .filter(x => selectedPhenotypeFilters.indexOf(x) < 0)
          .concat(selectedPhenotypeFilters.filter(x => previousFilters.indexOf(x) < 0))[0]

        if (selectedPhenotypeFilters.length === 0) {
          // Reset
          this.$store.commit(CLEAR_GENE_NETWORK_SCORES)
          this.$store.commit(UPDATE_VARIANT_SCORES)
        } else if (selectedPhenotypeFilters.length > previousFilters.length) {
          // Add one
          this.$store.dispatch(COMPUTE_SCORE, phenotypeFilter)
        } else {
          // Remove one
          this.$store.commit(REMOVE_GENE_NETWORK_SCORES, phenotypeFilter)
          this.$store.commit(UPDATE_VARIANT_SCORES)
        }
      },
      activationChanged (phenotypeId) {
        this.$store.commit(TOGGLE_ACTIVE_PHENOTYPE_FILTERS, phenotypeId)
        this.$store.commit(UPDATE_VARIANT_SCORES)
      },
      resetComponent () {
        this.phenotypes.splice(0)
        this.selectedOptions.splice(0)
      }
    },
    components: {
      vSelect
    },
    created: function () {
      get(this.$store.state.session.server, '/v2/sys_ont_OntologyTerm?q=ontology.ontologyName==hp').then(response => {
        if (response.total === 0) {
          this.$store.commit(CREATE_ALERT, {
            'message': 'The HPO ontology has not been uploaded yet. Download the OWL file here: http://human-phenotype-ontology.github.io/downloads.html',
            'type': 'warning'
          })
        }
      })
    },
    watch: {
      // call again the method if the route changes
      $route: function () {
        this.resetComponent()
      },
      selectedOptions: function (val) {
        this.selectionChanged(val)
      }
    }
  }
</script>
