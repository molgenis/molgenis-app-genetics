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
    <a id="download-anchor" style="display:none"></a>
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
  import csv from 'to-csv'
  import { TextEncoder } from 'text-encoding'
  import { fromByteArray } from 'base64-js'

  // https://developer.mozilla.org/en-US/docs/Web/API/WindowBase64/Base64_encoding_and_decoding#Solution_2_–_rewrite_the_DOMs_atob()_and_btoa()_using_JavaScript's_TypedArrays_and_UTF-8
  function base64EncodingUTF8 (str) {
    const encoded = new TextEncoder('utf-8').encode(str)
    return fromByteArray(encoded)
  }

  export default {
    name: 'variant-table',
    props: ['variants'],
    methods: {
      download: function () {
        const data = []
        this.variants.map(function (variant) {
          data.push({
            '#CHROM': variant['#CHROM'],
            'Gene_Name': variant['Gene_Name'],
            'cDNA': variant['cDNA'],
            'pChange': variant['pChange'],
            'gavinReason': variant['gavinReason']
          })
        })

        const result = csv(data).replace(/\r\n/g, '\n')

        // HTML workaround because all available file system packages on npm are broken
        const dlAnchorElem = document.getElementById('download-anchor')
        dlAnchorElem.setAttribute('href', 'data:text/csv;charset=utf-8;base64,' + base64EncodingUTF8(result))
        dlAnchorElem.setAttribute('download', 'patient_' + this.$route.params.entityTypeId + '.csv')
        dlAnchorElem.click()
      }
    },
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
