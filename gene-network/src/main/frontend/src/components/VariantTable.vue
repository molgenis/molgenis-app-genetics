<template>
  <b-table hover :items="variants" :fields="fields">
  </b-table>
</template>

<script>
  export default {
    name: 'variant-table',
    props: ['variants'],
    data: function () {
      return {
        fields: {
          '#CHROM': {
            label: 'Chromosome'
          },
          'POS': {
            label: 'Position'
          },
          'REF': {
            label: 'Reference allele'
          },
          'ALT': {
            label: 'Alternative allele'
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
