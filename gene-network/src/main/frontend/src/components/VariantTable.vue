<template>
  <b-table hover :items="variants" :fields="fields">
  </b-table>
</template>

<script>
  export default {
    name: 'variant-table',
    props: ['variants'],
    // TODO Add columns
    // Gavin reason
    // TODO Relevant variants and all variants
    // TODO sort on score
    data: function () {
      return {
        fields: {
          '#CHROM': {
            label: 'Chrom'
          },
          'POS': {
            label: 'Position'
          },
          'REF': {
            label: 'REF'
          },
          'ALT': {
            label: 'ALT'
          },
          'Gene_Name': {
            label: 'Gene'
          },
          'totalScore': {
            label: 'Score'
          }
        },
        filter: null
      }
    }

  }
</script>
