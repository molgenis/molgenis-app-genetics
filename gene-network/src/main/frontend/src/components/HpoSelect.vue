<template>
  <div class="form phenotype-selection-container">
    <h3 for="selected-phenotype-list">Phenotypes</h3>

    <div class="row form-group">
      <div class="col">
        <label for="hpo-select">Add HPO</label>
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

    <div class="row form-group">
      <div class="col">
        <ul id="selected-phenotype-list">
          <li v-for="filter in phenotypeFilters" class="row">
            <div class="col">
              {{ filter.ontologyTermName }}
            </div>
            <b-form-checkbox :checked="filter.isActive" @change="activationChanged(filter.id)" class="col">
              active
            </b-form-checkbox>
          </li>
          <li v-show="phenotypeFilters.length === 0" class="row">
            <em class="col">No phenotypes selected</em>
          </li>
        </ul>
      </div>
    </div>
  </div>

</template>

<style scoped>
  #selected-phenotype-list {
    /* indent within select to indicate child relation */
    margin-left: 1rem;
  }

  .phenotype-selection-container {
    /* add space to indicate is a seperate component */
    padding-bottom: 2rem;
  }
</style>

<script>
  import vSelect from 'vue-select'
  import { get } from '../MolgenisApi'
  import { SET_PHENOTYPE_FILTERS, TOGGLE_ACTIVE_PHENOTYPE_FILTERS } from '../store/mutations'
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
        console.log(selectedPhenotypeFilter)
        this.$store.commit(SET_PHENOTYPE_FILTERS, selectedPhenotypeFilter)
        this.$store.dispatch(COMPUTE_SCORE, selectedPhenotypeFilter)
      },
      activationChanged (phenotypeId) {
        // FIXME Recompute score on active toggle as well
        this.$store.commit(TOGGLE_ACTIVE_PHENOTYPE_FILTERS, phenotypeId)
      }
    },
    components: {
      vSelect
    }
  }
</script>
