<template>
  <div>
    <hpo-select></hpo-select>
    <hr>
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
          // Sort variants on GeneNetwork score before passing it to variant table
          const variants = this.$store.state.variants.slice()
          return variants.sort((a, b) => Number(b.totalScore) - Number(a.totalScore))
        }
      }
    },
    created: function () {
      const entityTypeId = this.$route.params.entityTypeId
      this.$store.dispatch(GET_PATIENT, entityTypeId)
    }
  }
</script>
