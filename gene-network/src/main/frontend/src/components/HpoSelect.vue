<template>
  <div class="form phenotype-selection-container">
    <h3 for="selected-phenotype-list">Phenotypes</h3>
    <div class="row form-group">
      <div class="col-md-12">
        <ul id="selected-phenotype-list">
          <li v-for="filter in phenotypeFilters" class="row">
            <div class="col">
              {{ filter.ontologyTermName }}
            </div>
            <b-form-checkbox :checked="filter.isActive" @change="activationChanged(filter.id)" class="col">
              active
            </b-form-checkbox>
          </li>
          <li v-show="phenotypeFilters.length === 0" class="row"><em>No phenotypes selected</em></li>
        </ul>
      </div>
    </div>

    <div class="row form-group">
      <div class="col">
        <label for="hpo-select">Select phenotypes to calculate variant relevance</label>
        <v-select
          id="hpo-select"
          :on-search="queryOntologies"
          :options="phenotypes"
          :onChange="selectionChanged"
          multiple
          placeholder="Search HPO Ontology..."
          label="ontologyTermName"
        >
        </v-select>
      </div>
    </div>
  </div>
</template>

<script>
  import vSelect from 'vue-select'
  import { get } from '../MolgenisApi'
  import {
    SET_PHENOTYPE_FILTERS,
    TOGGLE_ACTIVE_PHENOTYPE_FILTERS,
    CREATE_ALERT,
    CLEAR_GENE_NETWORK_SCORES
  } from '../store/mutations'
  import { COMPUTE_SCORE } from '../store/actions'

  export default {
    name: 'hpo-select',
    data: function () {
      return {
        phenotypes: []
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
        get(this.$store.state.session.server, '/v2/sys_ont_OntologyTerm?q=ontology.ontologyName==hp;(ontologyTermName=q=' + query + ',ontologyTermSynonym.ontologyTermSynonym=q=' + query + ',ontologyTermIRI=q=' + query + ')')
          .then(response => {
            this.phenotypes = response.items.map(function (phenotype) {
              phenotype.isActive = true
              return phenotype
            })
          })
      },
      selectionChanged (selectedPhenotypeFilter) {
        this.$store.commit(SET_PHENOTYPE_FILTERS, selectedPhenotypeFilter)
        const lastSelectedItem = selectedPhenotypeFilter.slice(-1).pop()
        if (lastSelectedItem !== undefined) {
          this.$store.dispatch(COMPUTE_SCORE, lastSelectedItem)
        } else {
          this.$store.commit(CLEAR_GENE_NETWORK_SCORES)
        }
      },
      activationChanged (phenotypeId) {
        // FIXME Recompute score on active toggle as well
        this.$store.commit(TOGGLE_ACTIVE_PHENOTYPE_FILTERS, phenotypeId)
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
    }
  }
</script>
