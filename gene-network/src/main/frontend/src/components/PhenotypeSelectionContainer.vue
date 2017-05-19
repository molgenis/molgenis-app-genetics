<template>
  <div>
    <hpo-select></hpo-select>
    <variant-table :variants="variants"></variant-table>
  </div>
</template>

<script>
  import { GET_PATIENT } from '../store/actions'
  import VariantTable from './VariantTable'
  import HpoSelect from './HpoSelect'

  export default {
    name: 'phenotype-selection-container',
    components: {VariantTable, HpoSelect},
    computed: {
      variants: {
        get: function () {
          const variants = this.$store.state.variants.slice().filter(function (variant) {
            if (variant.classification === undefined) {
              return variant
            }
            return variant.classification === 'Pathogenic' || variant.classification === 'Likely pathogenic'
          })

          return variants.sort(this.sortVariants)
        }
      }
    },
    created: function () {
      this.updatePatient()
    },
    watch: {
      // call again the method if the route changes
      '$route': 'updatePatient'
    },
    methods: {
      updatePatient () {
        const entityTypeId = this.$route.params.entityTypeId
        this.$store.dispatch(GET_PATIENT, entityTypeId)
      },
      sortVariants (a, b) {
        // default sort in case of no score
        if (a.totalScore === undefined || b.totalScore === undefined) {
          return b.Gene_Name.localeCompare(a.Gene_Name)
        }
        return b.totalScore - a.totalScore
      }
    }
  }
</script>
