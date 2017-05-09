<template>
  <div>
    <div class="justify-content-centermy-1 row">
      <b-form-fieldset horizontal label="Filter" class="col-6 offset-md-6" :label-size="2">
        <b-form-input v-model="filter" placeholder="Type to Search"></b-form-input>
      </b-form-fieldset>
    </div>
    <b-table hover :items="variants" :fields="fields" :filter="filter"></b-table>
  </div>
</template>

<script>
  export default {
    name: 'variant-table',
    props: ['variants'],
    // TODO Add columns: Gavin reason
    // TODO Relevant variants and all variants
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
