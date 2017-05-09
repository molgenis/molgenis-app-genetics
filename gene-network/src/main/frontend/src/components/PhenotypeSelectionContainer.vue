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
          return this.$store.state.variants
        }
      }
    },
    created: function () {
      const entityTypeId = this.$route.params.entityTypeId
      this.$store.dispatch(GET_PATIENT, entityTypeId)
    }
  }
</script>
