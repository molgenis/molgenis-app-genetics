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
    data: function () {
      return {
        fields: {
          '#CHROM': {
            label: 'Chromosome'
          },
          'Gene_Name': {
            label: 'Gene'
          },
          'cDNA': {
            label: 'cDNA'
          },
          'pChange': {
            label: 'Protein change'
          },
          'gavinReason': {
            label: 'Gavin Reason'
          }
        },
        filter: null
      }
    }
  }
</script>
