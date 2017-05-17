<template>
  <div>
    <br>
    <h3>Variant Table</h3>
    <p>
      Select phenotypes to retrieve gene network scores for every variant.
      Variants are sorted based on <em>cumulative</em> gene network score.
    </p>

    <div>
      <b-form-fieldset horizontal label="Filter" class="table-search-field" :label-size="2">
        <b-form-input v-model="filter" placeholder="Type to Search"></b-form-input>
      </b-form-fieldset>
      <b-btn class="table-download-button" variant="info" @click="download">Download all variants</b-btn>
    </div>


    <b-table hover :items="variants" :fields="fields" :filter="filter"></b-table>
  </div>
</template>

<style>
  .table-search-field {
    float: left
  }
  .table-download-button {
    float: right
  }
</style>
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
